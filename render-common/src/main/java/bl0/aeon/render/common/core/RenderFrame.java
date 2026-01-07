package bl0.aeon.render.common.core;

import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.data.render.ISingleRenderable;
import bl0.aeon.render.common.data.render.Camera;

import java.util.ArrayList;
import java.util.List;

public class RenderFrame {
    private final List<IRenderable> renderables;
    private final Camera camera;

    public RenderFrame(Camera camera, List<IRenderable> renderables) {
        this.camera = camera;
        this.renderables = renderables;
    }
    public Camera getCamera(){
        return this.camera;
    }
    public List<IRenderable> getRenderables(){
        return this.renderables;
    }
}
