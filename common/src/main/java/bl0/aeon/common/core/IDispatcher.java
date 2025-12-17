package bl0.aeon.common.core;

import bl0.aeon.common.c.Stages;
import bl0.aeon.common.context.IEngineContext;
import bl0.bjs.common.core.event.Action;

public interface IDispatcher {
    public void dispatch(Stages stage, Action<IEngineContext> action);
}
