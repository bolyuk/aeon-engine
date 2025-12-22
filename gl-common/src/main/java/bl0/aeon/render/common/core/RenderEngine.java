package bl0.aeon.render.common.core;


public interface RenderEngine {
    void render(RenderFrame renderContext);

    void initialize(String title, int width, int height);

    double getTime();

    IResourceFabric getFabric();

    void bindContext();
}
