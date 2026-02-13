package bl0.aeon.api.core;

import bl0.aeon.api.scene.Scene;
import bl0.aeon.api.stage.IDispatcher;
import bl0.aeon.render.api.backend.IResourceFabric;
import bl0.bjs.eventbus.IEventBus;
import bl0.bjs.logging.ILogger;

public interface IEngineContext {
    IDispatcher getDispatcher();
    IResourceManager getResourceManager();
    IResourceFabric getResourceFabric();
    Scene getScene();
    void setScene(Scene scene);
    GameInfo getInfo();

    IFrameContext getFrameContext();

    ILogger getDefaultLogger();

    IEventBus getEventBus();
}
