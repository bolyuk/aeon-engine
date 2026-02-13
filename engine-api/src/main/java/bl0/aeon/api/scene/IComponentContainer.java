package bl0.aeon.api.scene;

import bl0.aeon.api.component.Component;

import java.util.List;

public interface IComponentContainer {
    <T extends Component> boolean hasComponent(Class<T> type);
    <T extends Component> T getComponent(Class<T> type);
    <T extends Component> List<T> getEveryComponent(Class<T> type);

    void addComponent(Component component);
    void removeComponent(Component component);
}
