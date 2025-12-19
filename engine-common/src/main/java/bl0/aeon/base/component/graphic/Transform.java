package bl0.aeon.base.component.graphic;

import bl0.aeon.base.component.Component;
import org.joml.Matrix4f;

public interface Transform extends Component {
    Matrix4f getMatrix();
}
