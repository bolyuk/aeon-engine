package bl0.aeon.base.core;

import bl0.aeon.render.common.resource.IResourceManager;

public interface IEngineContext {
    bl0.aeon.base.core.IResourceManager getResourceManager();
    IFrameContext getFrameContext();
}
