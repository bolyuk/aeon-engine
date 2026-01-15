package bl0.aeon.prefabs.data;

import bl0.aeon.prefabs.base.PrefabData;

public class ListData extends PrefabData {
    public final PrefabData[] data;

    public ListData(String propertyName, String propertyClassName, PrefabData[] data) {
        super(propertyName, propertyClassName);
        this.data = data;
    }
}
