package bl0.aeon.engine.data.component;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.render.common.c.Colors;
import bl0.aeon.render.common.base.IResource;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Vector4f;

public class AE_Material extends BaseComponent implements Material {
    private ShaderProgram shaderProgram;
    private Vector4f color = Colors.WHITE;
    private Texture texture;
    private boolean isDepthTest = true;

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

    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public Vector4f getColor() {
        return color;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public boolean isDepthTestEnabled() {
        return isDepthTest;
    }

    @Override
    public void setDepthTestEnabled(boolean enabled) {
        isDepthTest = enabled;
    }
}

