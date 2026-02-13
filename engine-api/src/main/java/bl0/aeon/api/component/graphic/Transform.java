package bl0.aeon.api.component.graphic;

import bl0.aeon.api.component.Component;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface Transform extends Component {
    Matrix4f getMatrix();

    Vector3f getPosition();
    Quaternionf getRotation();

    void setRotation(Quaternionf q);
    void setPosition(Vector3f pos);
}
