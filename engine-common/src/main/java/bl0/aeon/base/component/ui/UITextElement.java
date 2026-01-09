package bl0.aeon.base.component.ui;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.render.common.resource.Font;
import bl0.aeon.render.common.resource.Mesh;

public interface UITextElement extends UIElement {
    String getText();
    Font getFont();
    Material getTextMaterial();

    Mesh getTextMesh();
}

