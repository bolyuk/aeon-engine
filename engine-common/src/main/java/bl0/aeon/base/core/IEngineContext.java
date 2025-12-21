package bl0.aeon.base.core;

import bl0.aeon.base.scene.Scene;
import bl0.aeon.base.stage.IDispatcher;
import bl0.aeon.render.common.core.IResourceManager;

public interface IEngineContext {
    IDispatcher getDispatcher();
    IResourceManager getResourceManager();

    void setScene(Scene scene);
}
