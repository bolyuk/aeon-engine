package bl0.aeon.gl.base;

import bl0.aeon.render.common.resource.IDisposable;
import bl0.aeon.render.common.resource.IResource;

public abstract class GLResource implements IResource, IDisposable {
    public final int ID;
    public final String name;

    public GLResource(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
