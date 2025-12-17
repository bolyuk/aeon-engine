package bl0.aeon.common.data.component;

import org.joml.Matrix4f;

public interface Transform extends Component {
    Matrix4f getMatrix();
}
