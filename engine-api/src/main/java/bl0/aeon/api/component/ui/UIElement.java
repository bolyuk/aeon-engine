package bl0.aeon.api.component.ui;

import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.api.interfaces.IClickable;
import bl0.aeon.render.api.base.IName;
import bl0.aeon.render.api.resource.Mesh;
import org.joml.Vector2f;

public interface UIElement extends IClickable, IName {

    UIContainer getParent();

    void setParent(UIContainer parent);

    Vector2f getPosition();
    Vector2f getSize();
    Material getMaterial();

    Mesh getMesh();

    boolean isEnabled();
    void setEnabled(boolean enabled);
}
