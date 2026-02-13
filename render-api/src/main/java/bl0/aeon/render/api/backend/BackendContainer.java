package bl0.aeon.render.api.backend;

public class BackendContainer {
    private final IRenderManager renderEngine;
    private final IResourceFabric resourceFabric;
    private final IWindowManager windowManager;

    public BackendContainer(IRenderManager renderEngine, IResourceFabric resourceFabric, IWindowManager windowManager) {
        this.renderEngine = renderEngine;
        this.resourceFabric = resourceFabric;
        this.windowManager = windowManager;
    }

    public IRenderManager getRenderEngine() {
        return renderEngine;
    }

    public IResourceFabric getResourceFabric() {
        return resourceFabric;
    }

    public IWindowManager getWindowManager() {
        return windowManager;
    }
}
