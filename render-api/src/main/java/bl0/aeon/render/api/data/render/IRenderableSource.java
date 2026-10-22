package bl0.aeon.render.api.data.render;

import java.util.List;

public interface IRenderableSource {
    void collectRenderables(List<? super IRenderable> out);
}
