/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.core.components.light;

import org.bl0.aeon.core.c.Uniforms;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.joml.Vector3f;

public class DirectionalLight
implements ILightComponent {
    public Vector3f direction;
    public Vector3f ambient;
    public Vector3f diffuse;
    public Vector3f specular;

    public DirectionalLight(Vector3f direction, Vector3f ambient, Vector3f diffuse, Vector3f specular) {
        this.direction = direction;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    @Override
    public void setUniforms(ShaderProgram program) {
        program.setUniform(Uniforms.Light.Directional.DIRECTION, this.direction);
        program.setUniform(Uniforms.Light.Directional.AMBIENT, this.ambient);
        program.setUniform(Uniforms.Light.Directional.DIFFUSE, this.diffuse);
        program.setUniform(Uniforms.Light.Directional.SPECULAR, this.specular);
    }

    @Override
    public Class<? extends IComponent> getComponentClass() {
        return DirectionalLight.class;
    }

    @Override
    public void OnBeforeRender(DrawContext drawCtx, GameContext gameCtx) {
    }

    @Override
    public void OnBeforeUpdate(GameContext gameCtx) {
    }
}

