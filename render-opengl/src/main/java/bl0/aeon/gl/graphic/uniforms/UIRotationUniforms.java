package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.render.api.data.render.IRenderable;
import bl0.aeon.render.api.data.render.ui.IUIRenderable;

public class UIRotationUniforms {
    public static void setUniforms(IUIRenderable obj, GLShaderProgram shaderProgram) {
        if(obj.getRotation() != null) {
            shaderProgram.setUniform(Uniforms.ROTATION, obj.getRotation());
            shaderProgram.setUniform(Uniforms.ORIGIN, obj.getPosition().x + obj.getSize().x / 2f, obj.getPosition().y + obj.getSize().y / 2f);
        }
    }
}
