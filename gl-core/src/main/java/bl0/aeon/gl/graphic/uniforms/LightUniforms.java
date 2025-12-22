package bl0.aeon.gl.graphic.uniforms;


import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.data.render.IRenderable;

public class LightUniforms {

    public static void setUniforms(IRenderable e, GLShaderProgram program) {
        DirectionalLight directionalLight = e.getDirectionalLight();

        if (directionalLight != null)
            setUniforms(directionalLight, program);

        program.resetIndex(Uniforms.Light.Point.POINTER);

        for(var lightPoint : e.getPointLights())
        {
            setUniforms(lightPoint, program);
            program.incrementIndex(Uniforms.Light.Point.POINTER);
        }
    }

    public static void setUniforms(DirectionalLight obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform(Uniforms.Light.Directional.DIRECTION, obj.getDirection());
        shaderProgram.setUniform(Uniforms.Light.Directional.AMBIENT, obj.getAmbient());
        shaderProgram.setUniform(Uniforms.Light.Directional.DIFFUSE, obj.getDiffuse());
        shaderProgram.setUniform(Uniforms.Light.Directional.SPECULAR, obj.getSpecular());
    }

    public static void setUniforms(PointLight obj, GLShaderProgram shaderProgram) {
        shaderProgram.setUniform(Uniforms.Light.Point.DIFFUSE_INDEX, obj.getDiffuse());
        shaderProgram.setUniform(Uniforms.Light.Point.SPECULAR_INDEX, obj.getSpecular());
        shaderProgram.setUniform(Uniforms.Light.Point.AMBIENT_INDEX, obj.getAmbient());
        shaderProgram.setUniform(Uniforms.Light.Point.CONSTANT_INDEX, obj.getConstant());
        shaderProgram.setUniform(Uniforms.Light.Point.LINEAR_INDEX, obj.getLinear());
        shaderProgram.setUniform(Uniforms.Light.Point.QUADRATIC_INDEX, obj.getQuadratic());
        shaderProgram.setUniform(Uniforms.Light.Point.POSITION_INDEX, obj.getPosition());
    }
}

