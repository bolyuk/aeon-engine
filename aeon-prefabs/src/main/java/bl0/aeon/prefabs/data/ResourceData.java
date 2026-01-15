package bl0.aeon.prefabs.data;

import bl0.aeon.prefabs.base.PrefabData;

public class ResourceData extends PrefabData {
    public final String resourceName;

    public ResourceData(String propertyName, String resourceName) {
        super(propertyName, null);
        this.resourceName = resourceName;
    }
}
