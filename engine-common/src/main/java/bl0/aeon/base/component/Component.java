package bl0.aeon.base.component;

import bl0.aeon.base.scene.IComponentContainer;

public interface Component {
    void onAdded(IComponentContainer ct);
    void onRemoved(IComponentContainer ct);

    boolean isEnabled();
    void setEnabled(boolean enabled);
}
