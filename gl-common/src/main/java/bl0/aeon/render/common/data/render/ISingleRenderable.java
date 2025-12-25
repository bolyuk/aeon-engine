package bl0.aeon.render.common.data.render;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public interface ISingleRenderable extends IRenderable {
    Matrix4f getMatrix();
}
