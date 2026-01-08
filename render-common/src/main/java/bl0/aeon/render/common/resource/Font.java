package bl0.aeon.render.common.resource;

import bl0.aeon.render.common.base.IResource;
import org.joml.Vector2f;

public interface Font extends IResource {
    int getFontSize();
    Vector2f calculateSize(String text);
}
