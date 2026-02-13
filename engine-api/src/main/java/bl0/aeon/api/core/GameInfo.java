package bl0.aeon.api.core;

public class GameInfo {
    public final String name;
    public final int version;

    public final String engineName = "AEON-ENGINE";
    public final int engineVersion = 1;

    public GameInfo(String name, int version) {
        this.name = name;
        this.version = version;
    }
}
