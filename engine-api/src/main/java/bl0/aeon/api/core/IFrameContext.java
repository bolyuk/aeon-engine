package bl0.aeon.api.core;

import bl0.aeon.api.scene.properties.Vector2fProperty;

public interface IFrameContext {
    double getDeltaTime();
    Vector2fProperty sizeProperty();

    double getFPS();
}
