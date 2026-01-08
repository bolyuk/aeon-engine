package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.data.render.scene.ISceneRenderable;

public class ColorUniforms {
    public static void setUniforms(IRenderable obj, GLShaderProgram shaderProgram) {
        if(obj.getColor() != null)
            shaderProgram.setUniform(Uniforms.COLOR, obj.getColor());
    }
}
