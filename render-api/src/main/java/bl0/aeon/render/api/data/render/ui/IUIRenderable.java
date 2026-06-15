package bl0.aeon.render.api.data.render.ui;

import bl0.aeon.render.api.data.render.IRenderable;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;

public interface IUIRenderable extends IRenderable {
    Vector2f getPosition();
    Vector2f getRenderPosition();
    Vector2f getSize();

    Quaternionf getRotation();
    Vector4f getPadding();

    Vector4f getBackgroundColor();

    default Vector4f getColor(){
        return getBackgroundColor();
    }
}
