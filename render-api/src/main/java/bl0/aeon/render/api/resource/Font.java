package bl0.aeon.render.api.resource;

import bl0.aeon.render.api.base.IResource;
import org.joml.Vector2f;

public interface Font extends IResource {
    int getFontSize();
    Vector2f calculateSize(String text);
}
