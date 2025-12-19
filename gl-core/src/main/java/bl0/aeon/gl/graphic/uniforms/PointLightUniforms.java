package bl0.aeon.gl.graphic.uniforms;

import bl0.aeon.common.data.component.light.PointLight;
import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLShaderProgram;

public class PointLightUniforms {
    public static void setUniforms(PointLight obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform(Uniforms.Light.Point.DIFFUSE_INDEX, obj.getDiffuse());
        shaderProgram.setUniform(Uniforms.Light.Point.SPECULAR_INDEX, obj.getSpecular());
        shaderProgram.setUniform(Uniforms.Light.Point.AMBIENT_INDEX, obj.getAmbient());
        shaderProgram.setUniform(Uniforms.Light.Point.CONSTANT_INDEX, obj.getConstant());
        shaderProgram.setUniform(Uniforms.Light.Point.LINEAR_INDEX, obj.getLinear());
        shaderProgram.setUniform(Uniforms.Light.Point.QUADRATIC_INDEX, obj.getQuadratic());
    }
}
