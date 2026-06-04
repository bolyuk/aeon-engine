package bl0.aeon.engine.core;

import bl0.aeon.api.core.IFrameContext;
import bl0.aeon.api.scene.properties.Vector2fProperty;

public class FrameContext implements IFrameContext {
    public double deltaTime;
    public final Vector2fProperty size = new  Vector2fProperty();
    public double fps;

    @Override
    public double getDeltaTime() {
        return deltaTime;
    }

    @Override
    public Vector2fProperty sizeProperty() {
        return size;
    }

    @Override
    public double getFPS() {
        return fps;
    }
}
