package bl0.aeon.render.common.core;


public interface RenderEngine {
    void render(IRenderContext renderContext);
    void initialize(String title, int width, int height);
}
