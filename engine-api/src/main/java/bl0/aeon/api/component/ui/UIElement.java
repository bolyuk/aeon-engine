package bl0.aeon.api.component.ui;

import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.api.interfaces.IClickable;
import bl0.aeon.api.scene.properties.QuaternionfProperty;
import bl0.aeon.api.scene.properties.Vector2fProperty;
import bl0.aeon.api.scene.properties.Vector4fProperty;
import bl0.aeon.render.api.base.IName;
import bl0.aeon.render.api.resource.Mesh;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;

public interface UIElement extends IClickable, IName {

    UIContainer getParent();
    void setParent(UIContainer parent);

    Vector2f getPosition();
    Vector2fProperty positionProperty();

    Vector2f getSize();
    Vector2fProperty sizeProperty();

    Quaternionf getRotation();
    QuaternionfProperty rotationProperty();

    Vector4f getPadding();
    Vector4fProperty paddingProperty();

}
