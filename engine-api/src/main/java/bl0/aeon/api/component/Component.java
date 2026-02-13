package bl0.aeon.api.component;

import bl0.aeon.api.scene.IComponentContainer;

public interface Component {
    void onAdded(IComponentContainer ct);
    void onRemoved(IComponentContainer ct);

    boolean isEnabled();
    void setEnabled(boolean enabled);
}
