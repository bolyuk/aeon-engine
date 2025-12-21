package bl0.aeon.engine.data.component;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.render.common.resource.IResource;
import bl0.aeon.render.common.resource.ShaderProgram;
import org.joml.Vector4f;

public class AE_Material extends BaseComponent implements Material {
    public ShaderProgram shaderProgram;
    public Vector4f color;

    @Override
    public void setColor(Vector4f color) {
        this.color = color;
    }

    @Override
    public void setShaderProgram(IResource shader) {
        if(shader instanceof ShaderProgram glsp) {
            this.shaderProgram = glsp;
        } else
            throw new IllegalArgumentException("wrong resource type");
    }
}

