package bl0.aeon.al;

import bl0.aeon.audio.api.IAudioManager;
import bl0.aeon.audio.api.NullAudioManager;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.IntBuffer;

/** Opens the sound: the default device through OpenAL, or nothing when that does not work. {@code -Daeon.noAudio=true} acts as if there were no device. */
public final class ALBackend {
    private ALBackend() {}

    /** Never fails: without a device, or without the native library, the result does nothing. */
    public static IAudioManager open() {
        if (Boolean.getBoolean("aeon.noAudio")) {
            System.out.println("[audio] off (-Daeon.noAudio)");
            return new NullAudioManager();
        }
        long device = 0L, context = 0L;
        try {
            device = ALC10.alcOpenDevice((CharSequence) null);
            if (device == 0L) {
                System.out.println("[audio] no sound device, playing without sound");
                return new NullAudioManager();
            }
            context = startContext(device, null);
            String name = ALC10.alcGetString(device, ALC10.ALC_DEVICE_SPECIFIER);
            ALAudioManager manager = new ALAudioManager(device, context);
            System.out.println("[audio] device: " + name + ", " + manager.sourceCount() + " sources");
            return manager;
        } catch (Throwable e) {         // no native library for this machine is a LinkageError
            System.out.println("[audio] could not start, playing without sound: " + e);
            if (context != 0L) ALC10.alcDestroyContext(context);
            if (device != 0L) ALC10.alcCloseDevice(device);
            return new NullAudioManager();
        }
    }

    /** Makes a context on this device current for the whole process. */
    static long startContext(long device, IntBuffer attributes) {
        long context = ALC10.alcCreateContext(device, attributes);
        if (context == 0L) throw new IllegalStateException("no OpenAL context: " + ALC10.alcGetString(device, ALC10.alcGetError(device)));
        if (!ALC10.alcMakeContextCurrent(context)) {
            ALC10.alcDestroyContext(context);
            throw new IllegalStateException("could not make the OpenAL context current");
        }
        ALCCapabilities capabilities = ALC.createCapabilities(device);
        AL.createCapabilities(capabilities);
        return context;
    }
}
