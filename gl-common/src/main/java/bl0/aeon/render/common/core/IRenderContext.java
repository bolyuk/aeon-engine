package bl0.aeon.render.common.core;

import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.data.render.Camera;

import java.util.List;

public interface IRenderContext {
    public Camera getCamera();
    public List<IRenderable> getRenderables();
}
