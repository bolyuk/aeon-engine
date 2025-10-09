/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.core.components.light;

import org.bl0.aeon.core.c.Uniforms;
import org.bl0.aeon.engine.context.DrawContext;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.core.graphic.shaders.ShaderProgram;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.joml.Vector3f;

public class PointLight
implements ILightComponent {
    public Vector3f diffuse;
    public Vector3f specular;
    public Vector3f ambient;
    public float constant;
    public float linear;
    public float quadratic;

    public PointLight(Vector3f diffuse, Vector3f specular, Vector3f ambient, float constant, float linear, float quadratic) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.ambient = ambient;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
    }

    @Override
    public Class<? extends IComponent> getComponentClass() {
        return ILightComponent.class;
    }

    @Override
    public void OnBeforeRender(DrawContext drawCtx, GameContext gameCtx) {
    }

    @Override
    public void OnBeforeUpdate(GameContext gameCtx) {
    }

    @Override
    public void setUniforms(ShaderProgram program) {
        program.setUniform(Uniforms.Light.Point.DIFFUSE_INDEX, this.diffuse);
        program.setUniform(Uniforms.Light.Point.SPECULAR_INDEX, this.specular);
        program.setUniform(Uniforms.Light.Point.AMBIENT_INDEX, this.ambient);
        program.setUniform(Uniforms.Light.Point.CONSTANT_INDEX, this.constant);
        program.setUniform(Uniforms.Light.Point.LINEAR_INDEX, this.linear);
        program.setUniform(Uniforms.Light.Point.QUADRATIC_INDEX, this.quadratic);
    }
}

