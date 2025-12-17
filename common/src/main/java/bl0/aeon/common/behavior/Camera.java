package bl0.aeon.common.behavior;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface Camera {
    Matrix4f getViewMatrix();
    Matrix4f getProjectionMatrix();
    Vector3f getPosition();
}
