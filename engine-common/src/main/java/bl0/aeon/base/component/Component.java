package bl0.aeon.base.component;

public interface Component {
    void onAdded(IComponentContainer ct);
    void onRemoved(IComponentContainer ct);
}
