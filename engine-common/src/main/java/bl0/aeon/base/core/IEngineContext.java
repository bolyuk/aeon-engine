package bl0.aeon.base.core;

import bl0.aeon.base.scene.Scene;
import bl0.aeon.base.stage.IDispatcher;
import bl0.aeon.render.common.core.IResourceFabric;
import bl0.aeon.render.common.core.IResourceManager;
import bl0.bjs.logging.ILogger;

public interface IEngineContext {
    IDispatcher getDispatcher();
    IResourceManager getResourceManager();
    IResourceFabric getResourceFabric();
    Scene getScene();
    void setScene(Scene scene);
    IFrameContext getFrameContext();

    ILogger getDefaultLogger();
}
