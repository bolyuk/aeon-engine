package bl0.aeon.audio.api;

/**
 * The sound of the engine, without any audio library in the way (see {@code audio-openal}). All calls from one thread, normally the render thread.
 * Nothing here throws because of the sound hardware or a bad file: the game goes on, just quieter.
 */
public interface IAudioManager {
    /** False when there is no device: every call then does nothing, see {@link NullAudioManager}. */
    boolean isAvailable();

    /** Loads a short sound from the classpath, WAV (PCM) or OGG by the extension. A file that cannot be read gives a silent sound and a line in the log. */
    ISound loadSound(String name, String resourcePath);

    /** A sound from samples: 16 bit, interleaved if {@code channels} is 2. */
    ISound createSound(String name, short[] pcm, int channels, int sampleRate);

    /**
     * Starts a sound. When all sources are busy the oldest sound that does not loop gives way; if they all loop, the new one does not play.
     * Always returns a voice, in that case one that has already ended.
     */
    IVoice play(ISound sound, SoundParams params);

    /** Where the ears are and where they look: position, direction ("at") and up. */
    void setListener(float x, float y, float z, float atX, float atY, float atZ, float upX, float upY, float upZ);

    /** 0..1, over everything. */
    void setMasterVolume(float volume);

    float getMasterVolume();

    /** Once a frame: returns the sources of finished sounds to the pool. */
    void update();

    /** How many sounds are playing now. */
    int activeVoices();

    void stopAll();

    /** Stops everything and closes the device. Can be called more than once; afterwards this manager does nothing. */
    void dispose();
}
