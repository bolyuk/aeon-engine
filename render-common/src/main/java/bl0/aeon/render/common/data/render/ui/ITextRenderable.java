package bl0.aeon.render.common.data.render.ui;

import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.resource.Font;
import org.joml.Vector2f;
import org.joml.Vector3f;

public interface ITextRenderable extends IRenderable {
    String getText();
    Font getFont();
    Vector2f getPosition();
}
