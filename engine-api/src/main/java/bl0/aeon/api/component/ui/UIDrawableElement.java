package bl0.aeon.api.component.ui;

import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.render.api.resource.Mesh;

public interface UIDrawableElement extends UIElement {
    Material getMaterial();

    Mesh getMesh();
}
