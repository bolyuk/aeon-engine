package bl0.aeon.common.data.component.light;

import bl0.aeon.common.data.component.Component;
import org.joml.Vector3f;

public interface Light extends Component {
    Vector3f getAmbient();
    Vector3f getDiffuse();
    Vector3f getSpecular();
}
