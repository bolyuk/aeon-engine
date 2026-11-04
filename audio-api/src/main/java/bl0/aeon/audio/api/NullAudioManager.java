package bl0.aeon.audio.api;

/** No sound at all: for a machine without a device, or when it is switched off. */
public final class NullAudioManager implements IAudioManager {
    /** What {@link #play} returns: a voice that has already ended. Also for backends, when a sound cannot start. */
    public static final IVoice ENDED_VOICE = new IVoice() {
        @Override public boolean isPlaying() { return false; }
        @Override public void stop() {}
        @Override public void setVolume(float volume) {}
        @Override public void setPitch(float pitch) {}
        @Override public void setLooping(boolean looping) {}
        @Override public void setPosition(float x, float y, float z) {}
    };

    private float masterVolume = 1f;

    @Override public boolean isAvailable() { return false; }

    @Override public ISound loadSound(String name, String resourcePath) { return silent(name); }

    @Override public ISound createSound(String name, short[] pcm, int channels, int sampleRate) { return silent(name); }

    @Override public IVoice play(ISound sound, SoundParams params) { return ENDED_VOICE; }

    @Override public void setListener(float x, float y, float z, float atX, float atY, float atZ, float upX, float upY, float upZ) {}

    @Override public void setMasterVolume(float volume) { masterVolume = volume; }

    @Override public float getMasterVolume() { return masterVolume; }

    @Override public void update() {}

    @Override public int activeVoices() { return 0; }

    @Override public void stopAll() {}

    @Override public void dispose() {}

    /** A sound with nothing in it. */
    public static ISound silent(String name) {
        return new ISound() {
            @Override public String getName() { return name; }
            @Override public float getDuration() { return 0f; }
            @Override public int getChannels() { return 1; }
        };
    }
}
