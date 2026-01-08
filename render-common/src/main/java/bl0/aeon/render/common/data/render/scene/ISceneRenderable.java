package bl0.aeon.render.common.data.render.scene;

import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Vector4f;

import java.util.List;

public interface ISceneRenderable extends IRenderable {
    Texture getTexture();
    List<PointLight> getPointLights();
    DirectionalLight getDirectionalLight();
    boolean isDepthTestEnabled();
}
