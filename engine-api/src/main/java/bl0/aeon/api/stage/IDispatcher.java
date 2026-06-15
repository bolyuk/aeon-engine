package bl0.aeon.api.stage;

import bl0.aeon.api.core.IEngineContext;
import bl0.bjs.common.core.event.action.Action;

public interface IDispatcher {
    void dispatch(Stage stage, Action<IEngineContext> action);
    void schedule(Stage stage, int count, Action<IEngineContext> action);
    void dispatchUnique(Stage stage, String tag, Action<IEngineContext> action);
}
