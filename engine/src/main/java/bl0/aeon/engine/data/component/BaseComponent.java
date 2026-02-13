package bl0.aeon.engine.data.component;

import bl0.aeon.api.component.Component;
import bl0.aeon.api.scene.IComponentContainer;

public class BaseComponent implements Component {
    protected IComponentContainer parent;
    protected boolean isEnabled = true;

    @Override
    public void onAdded(IComponentContainer ct) {
        parent = ct;
    }

    @Override
    public void onRemoved(IComponentContainer ct) {
        parent = null;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
