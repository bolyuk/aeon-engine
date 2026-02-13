package bl0.aeon.render.api.backend;

import bl0.aeon.render.api.data.input.InputData;
import bl0.bjs.common.core.event.action.Action;
import bl0.bjs.common.core.tuple.Pair;

public interface IWindowManager {
    void initialize(String title, int width, int height);
    void bindContext();

    void pollEvents();


    InputData pollInputData();

    void terminate();

    void captureCursor(boolean flag);

    void changeViewPort(int width, int height, float aspectRatio);

    void setWindowSize(int width, int height);

    void setFullscreen(boolean value);

    boolean isFullscreen();

    void setOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener);

    void remOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener);
}
