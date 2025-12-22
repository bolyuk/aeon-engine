package bl0.aeon.engine.core;

import bl0.aeon.base.core.IFrameContext;

public class FrameContext implements IFrameContext {
    public double deltaTime;
    public int width;
    public int height;

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
}
