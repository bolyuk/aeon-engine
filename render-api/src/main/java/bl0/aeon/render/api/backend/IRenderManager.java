package bl0.aeon.render.api.backend;

import bl0.aeon.render.api.data.render.RenderFrame;

public interface IRenderManager {
    void render(RenderFrame renderContext);
    double getTime();
    void swapBuffers();
}
