package bl0.aeon.render.common.data.render.scene;

import org.joml.Matrix4f;

public interface ISingleRenderable extends ISceneRenderable {
    Matrix4f getMatrix();
}
