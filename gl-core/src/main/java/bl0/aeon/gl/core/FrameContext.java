package bl0.aeon.gl.core;

import bl0.aeon.common.context.IFrameContext;
import bl0.aeon.gl.graphic.Window;

public class FrameContext implements IFrameContext {
    public double deltaTime = 0.0;
    public Window window;

    @Override
    public double getDeltaTime() {
        return deltaTime;
    }

    @Override
    public int getWidth() {
        return window.width;
    }

    @Override
    public int getHeight() {
        return window.height;
    }
}

