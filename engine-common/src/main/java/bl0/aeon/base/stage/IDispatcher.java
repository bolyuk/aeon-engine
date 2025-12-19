package bl0.aeon.base.stage;

import bl0.aeon.base.core.IEngineContext;
import bl0.bjs.common.core.event.Action;

public interface IDispatcher {
    public void dispatch(Stages stage, Action<IEngineContext> action);
}
