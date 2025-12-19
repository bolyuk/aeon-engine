package bl0.aeon.engine.data.component;

import bl0.aeon.base.component.IComponentContainer;
import bl0.aeon.common.data.component.base.Component;

public class BaseComponent implements Component {
    private IComponentContainer parent;

    @Override
    public void onAdded(IComponentContainer ct) {
        parent = ct;
    }

    @Override
    public void onRemoved(IComponentContainer ct) {
        parent = null;
    }
}
