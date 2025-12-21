package bl0.aeon.render.common.core;


public interface RenderEngine {
    void render(RenderFrame renderContext);

    void setResourceManager(IResourceManager resourceManager);
    void initialize(String title, int width, int height);
}
