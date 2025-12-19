package bl0.aeon.base.component.graphic;

import bl0.aeon.render.common.resource.IResource;
import bl0.aeon.base.component.Component;
import org.joml.Vector4f;

public interface Material extends Component {

    void setColor(Vector4f color);
    void setShaderProgram(IResource shader);
}
