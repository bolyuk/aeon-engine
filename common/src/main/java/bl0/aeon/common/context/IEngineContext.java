package bl0.aeon.common.context;

import bl0.aeon.common.core.IDispatcher;
import bl0.aeon.common.core.IResourceManager;

public interface IEngineContext {
    IResourceManager getResourceManager();
    IGameSettings getSettings();
    IFrameContext getFrameContext();
    IDispatcher getDispatcher();
}
