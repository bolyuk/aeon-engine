package bl0.aeon.render.api.data.render.ui;

import bl0.aeon.render.api.data.render.IRenderable;
import org.joml.Vector2f;
import org.joml.Vector4f;

public interface IUIRenderable extends IRenderable {
    Vector2f getPosition();
    Vector2f getSize();
    Vector4f getBackgroundColor();

    default Vector4f getColor(){
        return getBackgroundColor();
    }
}
