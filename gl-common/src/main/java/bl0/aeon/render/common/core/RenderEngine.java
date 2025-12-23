package bl0.aeon.render.common.core;


import bl0.aeon.render.common.data.input.InputData;
import bl0.bjs.common.core.event.action.Action;
import bl0.bjs.common.core.tuple.Pair;

public interface RenderEngine {

    void render(RenderFrame renderContext);

    void initialize(String title, int width, int height);

    double getTime();

    IResourceFabric getFabric();

    void bindContext();

    void pollEvents();

    void swapBuffers();

    InputData pollInputData();

    void setOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener);

    void remOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener);
}
