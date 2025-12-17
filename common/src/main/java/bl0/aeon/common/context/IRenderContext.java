package bl0.aeon.common.context;

import bl0.aeon.common.base.IRenderable;
import bl0.aeon.common.behavior.Camera;

import java.util.List;

public interface IRenderContext {
    public Camera getCamera();
    public List<IRenderable> getRenderables();
}
