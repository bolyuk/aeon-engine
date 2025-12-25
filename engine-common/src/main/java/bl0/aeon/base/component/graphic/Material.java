package bl0.aeon.base.component.graphic;

import bl0.aeon.render.common.resource.IResource;
import bl0.aeon.base.component.Component;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Vector4f;

public interface Material extends Component {

    void setColor(Vector4f color);
    void setShaderProgram(IResource shader);
    void setTexture(Texture texture);

    ShaderProgram getShaderProgram();
    Vector4f getColor();
    Texture getTexture();

    boolean isDepthTestEnabled();
    void setDepthTestEnabled(boolean enabled);
}
