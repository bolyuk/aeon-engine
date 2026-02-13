package bl0.aeon.api.component.graphic;

import bl0.aeon.render.api.base.IResource;
import bl0.aeon.api.component.Component;
import bl0.aeon.render.api.resource.ShaderProgram;
import bl0.aeon.render.api.resource.Texture;
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
