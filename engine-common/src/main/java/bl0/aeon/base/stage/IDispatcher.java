package bl0.aeon.base.stage;

import bl0.aeon.base.core.IEngineContext;
import bl0.bjs.common.core.event.action.Action;
import bl0.bjs.common.core.event.action.TaggedAction;

import java.util.UUID;

public interface IDispatcher {
    public void dispatch(Stage stage, Action<IEngineContext> action);

    public void dispatchUnique(Stage stage, String tag, Action<IEngineContext> action);
}
