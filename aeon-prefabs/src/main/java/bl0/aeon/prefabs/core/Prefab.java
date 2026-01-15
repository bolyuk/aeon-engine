package bl0.aeon.prefabs.core;

import bl0.aeon.prefabs.base.PrefabData;
import bl0.aeon.render.common.base.IResource;

public class Prefab implements IResource {
    public final String className;
    public final String name;
    public final PrefabData[] data;

    public Prefab(String className, String name, PrefabData [] data) {
        this.className = className;
        this.name = name;
        this.data = data;
    }

    @Override
    public IResource makeCopy(String name) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
