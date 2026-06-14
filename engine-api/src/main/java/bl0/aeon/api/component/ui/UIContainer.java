package bl0.aeon.api.component.ui;

import bl0.aeon.api.scene.properties.Vector2fProperty;

import java.util.List;

public interface UIContainer extends UIElement {
    List<UIElement> getUIElements();
}
