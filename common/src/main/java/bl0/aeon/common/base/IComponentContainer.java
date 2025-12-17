package bl0.aeon.common.base;

import bl0.aeon.common.data.component.Component;

public interface IComponentContainer {
    Component getComponent(String name);
    boolean hasComponent(String name);
    void addComponent(String name, Component component);
    void removeComponent(String name);
    <T extends Component> T getComponent(String name, Class<T> type);
}
