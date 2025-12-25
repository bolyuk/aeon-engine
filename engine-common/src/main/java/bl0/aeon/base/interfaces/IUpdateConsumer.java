package bl0.aeon.base.interfaces;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;

public interface IUpdateConsumer {
    void update(IFrameContext fCtx, IEngineContext eCtx);
}
