package bl0.aeon.audio.api;

/**
 * One playback of a sound. Once it has ended, was stopped or lost its source to a newer sound, it stays a harmless handle: {@link #isPlaying()} is
 * false and everything else does nothing.
 */
public interface IVoice {
    boolean isPlaying();

    void stop();

    /** 0..1, on top of the master volume and the distance. */
    void setVolume(float volume);

    /** 1 = normal; also changes the speed. */
    void setPitch(float pitch);

    void setLooping(boolean looping);

    /** Where the sound is now: world position, or relative to the listener if it was started with {@link SoundParams#local()}. */
    void setPosition(float x, float y, float z);
}
