package bl0.aeon.engine;

import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.stage.IDispatcher;
import bl0.aeon.base.stage.Stage;
import bl0.bjs.common.core.event.action.Action;
import bl0.bjs.common.core.event.action.ActionController;
import bl0.bjs.common.core.event.action.TaggedAction;
import bl0.bjs.common.core.tuple.Pair;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Dispatcher implements IDispatcher {
    ConcurrentHashMap<Stage, ActionController<IEngineContext>> data = new ConcurrentHashMap<>();
    @Override
    public void dispatch(Stage stage, Action<IEngineContext> action) {
        data.computeIfAbsent(stage, (s) -> new ActionController<>());
        data.get(stage).register(action);
    }

    @Override
    public void dispatchUnique(Stage stage, String tag, Action<IEngineContext> action) {
        data.computeIfAbsent(stage, (s) -> new ActionController<>());
        data.get(stage).registerSingle(new TaggedAction<IEngineContext>() {

            @Override
            public void invoke(IEngineContext data) {
                action.invoke(data);
            }

            @Override
            public String tag() {
                return tag;
            }
        });
    }

    public void fire(Stage stage, IEngineContext ctx) {
        ActionController<IEngineContext> controller = data.get(stage);
        if(controller == null)
            return;
        try {
            controller.invoke(ctx);
        } catch(Exception e) {
            ctx.getDefaultLogger().err(stage+" err: "+e.getMessage());
        }  finally {
            controller.clear();
        }
    }
}
