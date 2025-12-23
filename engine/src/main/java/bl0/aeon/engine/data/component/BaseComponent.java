package bl0.aeon.engine.data.component;

import bl0.aeon.base.component.Component;
import bl0.aeon.base.scene.IComponentContainer;

public class BaseComponent implements Component {
    protected IComponentContainer parent;

    @Override
    public void onAdded(IComponentContainer ct) {
        parent = ct;
    }

    @Override
    public void onRemoved(IComponentContainer ct) {
        parent = null;
    }
}
