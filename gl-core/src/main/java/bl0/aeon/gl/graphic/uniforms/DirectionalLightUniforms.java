package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.common.data.component.light.DirectionalLight;
import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.shaders.GLShaderProgram;

public class DirectionalLightUniforms {
    public static void setUniforms(DirectionalLight obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform(Uniforms.Light.Directional.DIRECTION, obj.getDirection());
        shaderProgram.setUniform(Uniforms.Light.Directional.AMBIENT, obj.getAmbient());
        shaderProgram.setUniform(Uniforms.Light.Directional.DIFFUSE, obj.getDiffuse());
        shaderProgram.setUniform(Uniforms.Light.Directional.SPECULAR, obj.getSpecular());
    }
}
