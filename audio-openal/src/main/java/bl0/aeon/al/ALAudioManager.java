package bl0.aeon.al;

import bl0.aeon.audio.api.IAudioManager;
import bl0.aeon.audio.api.ISound;
import bl0.aeon.audio.api.IVoice;
import bl0.aeon.audio.api.NullAudioManager;
import bl0.aeon.audio.api.SoundParams;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OpenAL behind {@link IAudioManager}: a fixed pool of sources, made once, and one buffer per sound. The context is current for the whole process, so
 * only one manager can be open at a time (it is closed when the process ends if nobody did it before).
 */
public class ALAudioManager implements IAudioManager {
    /** Most sources to ask for; a device may give fewer. */
    private static final int MAX_SOURCES = 48;
    private static final float MIN_PITCH = 0.05f, MAX_PITCH = 4f;
    /** {@code -Daeon.audioLog=true}: a line for every sound started, to see what plays where nobody can listen. */
    private static final boolean LOG = Boolean.getBoolean("aeon.audioLog");

    private final long device, context;
    private final Slot[] slots;
    private final List<ALSound> sounds = new ArrayList<>();
    private final Set<String> warned = new HashSet<>();
    private final Thread closeAtExit = new Thread(this::dispose, "audio-close");
    private float masterVolume = 1f;
    private long started;
    private volatile boolean disposed;

    /** One OpenAL source, and the voice now using it. */
    private static final class Slot {
        final int source;
        Voice voice;

        Slot(int source) {
            this.source = source;
        }
    }

    private record ALSound(String name, int buffer, float duration, int channels) implements ISound {
        @Override public String getName() { return name; }
        @Override public float getDuration() { return duration; }
        @Override public int getChannels() { return channels; }
    }

    /** Takes over a device with its context already current. */
    ALAudioManager(long device, long context) {
        this.device = device;
        this.context = context;
        AL10.alGetError();
        // the plainest attenuation to reason about: full up to the reference distance, silence at the maximum, straight between
        AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
        List<Slot> made = new ArrayList<>();
        for (int i = 0; i < MAX_SOURCES; i++) {
            int source = AL10.alGenSources();
            if (AL10.alGetError() != AL10.AL_NO_ERROR) break;
            made.add(new Slot(source));
        }
        slots = made.toArray(new Slot[0]);
        Runtime.getRuntime().addShutdownHook(closeAtExit);
    }

    public int sourceCount() {
        return slots.length;
    }

    @Override
    public boolean isAvailable() {
        return !disposed;
    }

    @Override
    public ISound loadSound(String name, String resourcePath) {
        if (disposed) return NullAudioManager.silent(name);
        try (InputStream in = open(resourcePath)) {
            if (in == null) throw new IOException("no such resource");
            SoundDecoder.Pcm pcm = SoundDecoder.decode(in.readAllBytes(), resourcePath);
            return createSound(name, pcm.samples(), pcm.channels(), pcm.sampleRate());
        } catch (IOException | RuntimeException e) {
            System.out.println("[audio] could not load " + resourcePath + ": " + e.getMessage());
            return NullAudioManager.silent(name);
        }
    }

    private static InputStream open(String path) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream in = loader == null ? null : loader.getResourceAsStream(path);
        return in != null ? in : ALAudioManager.class.getClassLoader().getResourceAsStream(path);
    }

    @Override
    public ISound createSound(String name, short[] pcm, int channels, int sampleRate) {
        if (disposed || pcm.length == 0) return NullAudioManager.silent(name);
        AL10.alGetError();
        int buffer = AL10.alGenBuffers();
        AL10.alBufferData(buffer, channels == 2 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16, pcm, sampleRate);
        int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR) {
            System.out.println("[audio] could not make the buffer of " + name + ": " + AL10.alGetString(error));
            AL10.alDeleteBuffers(buffer);
            return NullAudioManager.silent(name);
        }
        ALSound sound = new ALSound(name, buffer, pcm.length / (float) channels / sampleRate, channels);
        sounds.add(sound);
        return sound;
    }

    @Override
    public IVoice play(ISound sound, SoundParams p) {
        if (disposed || !(sound instanceof ALSound s)) return NullAudioManager.ENDED_VOICE;
        Slot slot = freeSlot();
        if (slot == null) {
            warnOnce("all sources are taken by loops, " + s.name + " does not play");
            return NullAudioManager.ENDED_VOICE;
        }
        if (p.positional && s.channels != 1) warnOnce(s.name + " is not mono, so it cannot be placed in space");

        int src = slot.source;
        AL10.alSourceStop(src);
        AL10.alSourcei(src, AL10.AL_BUFFER, s.buffer);
        AL10.alSourcei(src, AL10.AL_SOURCE_RELATIVE, p.positional ? AL10.AL_FALSE : AL10.AL_TRUE);
        AL10.alSource3f(src, AL10.AL_POSITION, p.x, p.y, p.z);
        // a local sound has no distance; in the linear model a rolloff of 0 means full volume everywhere
        AL10.alSourcef(src, AL10.AL_ROLLOFF_FACTOR, p.positional ? 1f : 0f);
        AL10.alSourcef(src, AL10.AL_REFERENCE_DISTANCE, Math.max(0f, p.refDistance));
        AL10.alSourcef(src, AL10.AL_MAX_DISTANCE, Math.max(p.refDistance + 0.01f, p.maxDistance));
        AL10.alSourcef(src, AL10.AL_GAIN, clampVolume(p.volume));
        AL10.alSourcef(src, AL10.AL_PITCH, clampPitch(p.pitch));
        AL10.alSourcei(src, AL10.AL_LOOPING, p.looping ? AL10.AL_TRUE : AL10.AL_FALSE);
        AL10.alSourcePlay(src);
        if (LOG) {
            System.out.printf("[audio] play %s %s (%.1f %.1f %.1f) volume %.2f pitch %.2f%s, state %s, error %d%n", s.name, p.positional ? "at" : "local",
                    p.x, p.y, p.z, p.volume, p.pitch, p.looping ? " loop" : "",
                    AL10.alGetSourcei(src, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING ? "playing" : "NOT PLAYING", AL10.alGetError());
        }

        Voice voice = new Voice(slot, ++started, p.looping);
        slot.voice = voice;
        return voice;
    }

    /** A source not in use, or the one of the oldest sound that does not loop. */
    private Slot freeSlot() {
        Slot oldest = null;
        for (Slot slot : slots) {
            if (slot.voice == null) return slot;
            if (!slot.voice.isPlaying()) {
                slot.voice = null;
                return slot;
            }
            if (!slot.voice.looping && (oldest == null || slot.voice.startedAt < oldest.voice.startedAt)) oldest = slot;
        }
        return oldest;
    }

    @Override
    public void setListener(float x, float y, float z, float atX, float atY, float atZ, float upX, float upY, float upZ) {
        if (disposed) return;
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
        AL10.alListenerfv(AL10.AL_ORIENTATION, new float[]{atX, atY, atZ, upX, upY, upZ});
    }

    @Override
    public void setMasterVolume(float volume) {
        masterVolume = clampVolume(volume);
        if (!disposed) AL10.alListenerf(AL10.AL_GAIN, masterVolume);
    }

    @Override
    public float getMasterVolume() {
        return masterVolume;
    }

    @Override
    public void update() {
        if (disposed) return;
        for (Slot slot : slots) if (slot.voice != null && !slot.voice.isPlaying()) slot.voice = null;
    }

    @Override
    public int activeVoices() {
        update();
        int n = 0;
        for (Slot slot : slots) if (slot.voice != null) n++;
        return n;
    }

    @Override
    public void stopAll() {
        if (disposed) return;
        for (Slot slot : slots) {
            AL10.alSourceStop(slot.source);
            slot.voice = null;
        }
    }

    @Override
    public synchronized void dispose() {
        if (disposed) return;
        disposed = true;
        for (Slot slot : slots) {
            AL10.alSourceStop(slot.source);
            AL10.alSourcei(slot.source, AL10.AL_BUFFER, 0);     // a buffer still attached to a source cannot be deleted
            AL10.alDeleteSources(slot.source);
            slot.voice = null;
        }
        for (ALSound s : sounds) AL10.alDeleteBuffers(s.buffer);
        sounds.clear();
        ALC10.alcMakeContextCurrent(0L);
        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
        AL.setCurrentProcess(null);
        try {
            Runtime.getRuntime().removeShutdownHook(closeAtExit);
        } catch (IllegalStateException atExit) {
            // the process is already ending (this may be that hook): nothing to take off
        }
    }

    /** The AL error since the last call, for checks: {@code AL_NO_ERROR} is 0. */
    public int takeError() {
        return disposed ? AL10.AL_NO_ERROR : AL10.alGetError();
    }

    private void warnOnce(String message) {
        if (warned.add(message)) System.out.println("[audio] " + message);
    }

    private static float clampVolume(float v) {
        return Math.max(0f, Math.min(1f, v));
    }

    private static float clampPitch(float v) {
        return Math.max(MIN_PITCH, Math.min(MAX_PITCH, v));
    }

    /** A handle on a slot, valid while the slot still holds it. */
    private final class Voice implements IVoice {
        final Slot slot;
        final long startedAt;
        boolean looping;

        Voice(Slot slot, long startedAt, boolean looping) {
            this.slot = slot;
            this.startedAt = startedAt;
            this.looping = looping;
        }

        private boolean owns() {
            return !disposed && slot.voice == this;
        }

        @Override
        public boolean isPlaying() {
            return owns() && AL10.alGetSourcei(slot.source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
        }

        @Override
        public void stop() {
            if (!owns()) return;
            AL10.alSourceStop(slot.source);
            slot.voice = null;
        }

        @Override
        public void setVolume(float volume) {
            if (owns()) AL10.alSourcef(slot.source, AL10.AL_GAIN, clampVolume(volume));
        }

        @Override
        public void setPitch(float pitch) {
            if (owns()) AL10.alSourcef(slot.source, AL10.AL_PITCH, clampPitch(pitch));
        }

        @Override
        public void setLooping(boolean looping) {
            if (!owns()) return;
            this.looping = looping;
            AL10.alSourcei(slot.source, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
        }

        @Override
        public void setPosition(float x, float y, float z) {
            if (owns()) AL10.alSource3f(slot.source, AL10.AL_POSITION, x, y, z);
        }
    }
}
