package bl0.aeon.render.api.data.render.scene;

import bl0.aeon.render.api.data.light.DirectionalLight;
import bl0.aeon.render.api.data.light.PointLight;
import bl0.aeon.render.api.data.render.IRenderable;
import bl0.aeon.render.api.resource.Texture;

import java.util.List;

public interface ISceneRenderable extends IRenderable {
    Texture getTexture();
    List<PointLight> getPointLights();
    DirectionalLight getDirectionalLight();
    boolean isDepthTestEnabled();

    /**
     * False for translucent surfaces: they are tested against the depth buffer but do not write it, so what is drawn after them
     * (further translucent layers, sorted back to front) still shows through. Default true.
     */
    default boolean isDepthWriteEnabled() {
        return true;
    }
}
