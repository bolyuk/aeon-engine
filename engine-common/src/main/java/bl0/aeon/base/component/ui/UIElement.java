package bl0.aeon.base.component.ui;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.interfaces.IClickable;
import bl0.aeon.render.common.base.IName;
import bl0.aeon.render.common.resource.Mesh;
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
