package bl0.aeon.render.api.data.render;

import java.util.List;

public record RenderFrame(Camera camera, List<IRenderable> renderables, int framebufferWidth, int framebufferHeight) {
}
