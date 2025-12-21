package bl0.aeon.engine;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.stage.IDispatcher;
import bl0.aeon.base.stage.Stage;
import bl0.bjs.common.core.event.Action;
import bl0.bjs.common.core.event.ActionController;

import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher implements IDispatcher {
    ConcurrentHashMap<Stage, ActionController<IEngineContext>> data = new ConcurrentHashMap<>();
    @Override
    public void dispatch(Stage stage, Action<IEngineContext> action) {
        data.computeIfAbsent(stage, (s) -> new ActionController<>());
        data.get(stage).register(action);
    }

    public void fire(Stage stage, IEngineContext ctx) {
        ActionController<IEngineContext> controller = data.get(stage);
        if(controller == null)
            return;

        controller.invoke(ctx);
        controller.clear();
    }
}
