package bl0.aeon.engine.core;

import bl0.aeon.api.core.IFrameContext;

public class FrameContext implements IFrameContext {
    public double deltaTime;
    public int width;
    public int height;

    public double fps;

    @Override
    public double getDeltaTime() {
        return deltaTime;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public double getFPS() {
        return fps;
    }
}
