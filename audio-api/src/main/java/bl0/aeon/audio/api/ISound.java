package bl0.aeon.audio.api;

/** Loaded sound data. Belongs to the manager that loaded it and lives as long as that manager. */
public interface ISound {
    String getName();

    /** Length at normal pitch, seconds; 0 for a sound that could not be loaded. */
    float getDuration();

    /** Channels of the data. Only a mono sound can be placed in space: a stereo one plays as it is, from everywhere. */
    int getChannels();
}
