package bl0.aeon.prefabs.data;

import bl0.aeon.prefabs.base.PrefabData;

public class JsonData extends PrefabData {
    public final String data;


    public JsonData(String propertyName, String propertyClassName, String data) {
        super(propertyName, propertyClassName);
        this.data = data;
    }
}
