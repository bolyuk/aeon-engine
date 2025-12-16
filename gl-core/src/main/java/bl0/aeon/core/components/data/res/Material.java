/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.core.components.data.res;

import org.bl0.aeon.core.graphic.Texture;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;
import org.bl0.aeon.engine.interfaces.BaseGLResource;
import org.bl0.aeon.engine.interfaces.IUniformsSetter;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.joml.Vector4f;

public class Material
extends BaseGLResource
implements IUniformsSetter,
IComponent {
    public ShaderProgram shaderProgram;
    public Vector4f color;
    public Texture texture;

    public Material() {
        super(-1);
    }

    @Override
    protected void disposeCall() {
        this.shaderProgram.dispose();
        if (this.texture != null) {
            this.texture.dispose();
        }
    }

    @Override
    protected void bindCall() {
        if (this.shaderProgram == null) {
            throw new IllegalStateException("Material: shaderProgram is null");
        }
        if (this.texture != null) {
            this.texture.bind();
        }
        this.shaderProgram.bind();
    }

    @Override
    protected void unbindCall() {
        this.shaderProgram.unbind();
    }

    @Override
    public void setUniforms(ShaderProgram program) {
        if (this.texture != null) {
            this.shaderProgram.setUniform("texture", 0);
        }
        if (this.color != null) {
            this.shaderProgram.setUniform("color", this.color);
        }
    }

    @Override
    public Class<? extends IComponent> getComponentClass() {
        return Material.class;
    }
}

