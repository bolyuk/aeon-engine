package bl0.aeon.api.interfaces;

import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.core.IFrameContext;

public interface IUpdateConsumer {
    void update(IFrameContext fCtx, IEngineContext eCtx);
}
