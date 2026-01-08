package bl0.aeon.render.common.backend;

import bl0.aeon.render.common.data.render.RenderFrame;

public interface IRenderManager {
    void render(RenderFrame renderContext);
    double getTime();
    void swapBuffers();
}
