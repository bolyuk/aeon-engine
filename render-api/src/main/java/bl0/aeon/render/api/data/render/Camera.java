package bl0.aeon.render.api.data.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface Camera {
    Matrix4f getViewMatrix();
    Matrix4f getProjectionMatrix();
    Vector3f getPosition();
    void setPosition(Vector3f position);
    void setAspectRatio(float aspectRatio);
}
