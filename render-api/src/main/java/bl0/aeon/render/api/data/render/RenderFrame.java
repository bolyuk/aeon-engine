package bl0.aeon.render.api.data.render;

import bl0.aeon.render.api.resource.Framebuffer;
import bl0.aeon.render.api.resource.Mesh;
import bl0.aeon.render.api.resource.ShaderProgram;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record RenderFrame(Camera camera,
                          List<IRenderable> renderables,
                          int framebufferWidth,
                          int framebufferHeight,
                          Framebuffer framebuffer) {
}
