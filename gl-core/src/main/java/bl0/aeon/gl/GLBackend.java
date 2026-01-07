package bl0.aeon.gl;

import bl0.aeon.gl.backend.GLRenderManager;
import bl0.aeon.gl.backend.GLResourceFabric;
import bl0.aeon.gl.backend.GLWindowManager;
import bl0.aeon.render.common.backend.BackendContainer;
import bl0.bjs.common.base.IContext;

public class GLBackend {

    private final BackendContainer container;

    public GLBackend(IContext ctx) {
        var glState = new GLState();
        container = new BackendContainer(new GLRenderManager(glState, ctx),
                new GLResourceFabric(),
                new GLWindowManager(glState, ctx));
    }

    public BackendContainer get() {
        return container;
    }
}
