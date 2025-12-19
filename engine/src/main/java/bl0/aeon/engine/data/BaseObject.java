package bl0.aeon.engine.data;

import bl0.aeon.base.component.Component;
import bl0.aeon.base.scene.SceneObject;
import bl0.aeon.base.component.IComponentContainer;

public class BaseObject implements SceneObject, IComponentContainer {

    @Override
    public <T extends Component> boolean hasComponent(Class<T> type) {
        return false;
    }

    @Override
    public <T extends Component> T getComponent(Class<T> type) {
        return null;
    }

    @Override
    public void addComponent(Component component) {

    }

    @Override
    public void removeComponent(Component component) {

    }

    @Override
    public String getName() {
        return "";
    }
}
