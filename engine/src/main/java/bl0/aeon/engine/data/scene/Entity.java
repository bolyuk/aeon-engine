package bl0.aeon.engine.data.scene;

import bl0.aeon.base.IUpdateConsumer;
import bl0.aeon.base.component.Component;
import bl0.aeon.base.component.UpdatableComponent;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.base.scene.SceneObject;
import bl0.aeon.base.component.IComponentContainer;

import java.util.ArrayList;
import java.util.List;

public class Entity extends SceneObject implements IComponentContainer, IUpdateConsumer {
    private final ArrayList<Component> components = new  ArrayList<>();

    public Entity(String name) {
        super(name);
    }

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        for (UpdatableComponent component : getEveryComponent(UpdatableComponent.class)) {
            component.update(fCtx, eCtx);
        }
    }

    @Override
    public <T extends Component> boolean hasComponent(Class<T> type) {
        return components.stream().anyMatch(x -> type.isAssignableFrom(x.getClass()));
    }

    @Override
    public <T extends Component> T getComponent(Class<T> type) {
        return components.stream().filter(x -> type.isAssignableFrom(x.getClass())).map(x -> (T)x).findFirst().orElseThrow();
    }

    @Override
    public <T extends Component> List<T> getEveryComponent(Class<T> type) {
        return components.stream().filter(x -> type.isAssignableFrom(x.getClass())).map(x -> (T)x).toList();
    }

    @Override
    public void addComponent(Component component) {
        components.add(component);
        component.onAdded(this);
    }

    @Override
    public void removeComponent(Component component) {
        components.remove(component);
        component.onRemoved(this);
    }
}
