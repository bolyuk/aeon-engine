package bl0.aeon.engine.data.scene;

import bl0.aeon.api.interfaces.IUpdateConsumer;
import bl0.aeon.api.component.Component;
import bl0.aeon.api.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.api.core.IFrameContext;
import bl0.aeon.api.scene.SceneObject;
import bl0.aeon.api.scene.IComponentContainer;

import java.util.ArrayList;
import java.util.List;

public class Entity extends SceneObject implements IComponentContainer, IUpdateConsumer {
    private final ArrayList<Component> components = new  ArrayList<>();

    public Entity(String name) {
        super(name);
    }

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        for (UpdateConsumerComponent component : getEveryComponent(UpdateConsumerComponent.class)) {
            component.update(fCtx, eCtx);
        }
    }

    @Override
    public <T extends Component> boolean hasComponent(Class<T> type) {
        return components.stream().anyMatch(x -> type.isAssignableFrom(x.getClass()) && x.isEnabled());
    }

    @Override
    public <T extends Component> T getComponent(Class<T> type) {
        return components.stream().filter(x -> type.isAssignableFrom(x.getClass()) && x.isEnabled()).map(x -> (T)x).findFirst().orElse(null);
    }

    @Override
    public <T extends Component> List<T> getEveryComponent(Class<T> type) {
        return components.stream().filter(x -> type.isAssignableFrom(x.getClass()) && x.isEnabled()).map(x -> (T)x).toList();
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
