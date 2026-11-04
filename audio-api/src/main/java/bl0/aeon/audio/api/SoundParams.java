package bl0.aeon.audio.api;

/**
 * How to play a sound: {@code SoundParams.at(x, y, z).volume(0.5f).reach(4, 60)}.
 * <p>
 * The volume falls in a straight line with the distance: full up to {@code refDistance}, silence at {@code maxDistance} and beyond.
 */
public final class SoundParams {
    public static final float DEFAULT_REF_DISTANCE = 4f, DEFAULT_MAX_DISTANCE = 64f;

    public final boolean positional;
    public float x, y, z;
    public float volume = 1f, pitch = 1f;
    public boolean looping;
    public float refDistance = DEFAULT_REF_DISTANCE, maxDistance = DEFAULT_MAX_DISTANCE;

    private SoundParams(boolean positional, float x, float y, float z) {
        this.positional = positional;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** At a point of the world: it stays there as the listener moves and turns. */
    public static SoundParams at(float x, float y, float z) {
        return new SoundParams(true, x, y, z);
    }

    /** With the listener: no distance, for ambience, the interface, the player's own steps. {@link #offset} moves it off the centre of the head. */
    public static SoundParams local() {
        return new SoundParams(false, 0f, 0f, 0f);
    }

    /** Relative to the listener: x is right, y is up, -z is ahead. Only for {@link #local()}. */
    public SoundParams offset(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public SoundParams volume(float volume) {
        this.volume = volume;
        return this;
    }

    public SoundParams pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public SoundParams looping(boolean looping) {
        this.looping = looping;
        return this;
    }

    /** Full volume up to {@code refDistance}, silence from {@code maxDistance}. */
    public SoundParams reach(float refDistance, float maxDistance) {
        this.refDistance = refDistance;
        this.maxDistance = maxDistance;
        return this;
    }
}
