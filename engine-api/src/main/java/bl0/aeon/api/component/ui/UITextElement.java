package bl0.aeon.api.component.ui;

import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.render.api.resource.Font;
import bl0.aeon.render.api.resource.Mesh;

public interface UITextElement extends UIElement {
    String getText();
    Font getFont();
    Material getTextMaterial();

    Mesh getTextMesh();
}

