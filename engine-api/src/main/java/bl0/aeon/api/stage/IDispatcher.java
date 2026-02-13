package bl0.aeon.api.stage;

import bl0.aeon.api.core.IEngineContext;
import bl0.bjs.common.core.event.action.Action;

public interface IDispatcher {
    public void dispatch(Stage stage, Action<IEngineContext> action);

    public void dispatchUnique(Stage stage, String tag, Action<IEngineContext> action);
}
