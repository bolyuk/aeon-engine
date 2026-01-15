package bl0.aeon.prefabs.base;

public class PrefabData {
    public final String propertyName;
    public final String propertyClassName;

    public final String prefabDataClassName;

    public PrefabData(String propertyName, String propertyClassName){
        this.propertyName = propertyName;
        this.propertyClassName = propertyClassName;

        prefabDataClassName = this.getClass().getName();
    }
}
