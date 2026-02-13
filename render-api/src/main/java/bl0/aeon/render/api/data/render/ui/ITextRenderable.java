package bl0.aeon.render.api.data.render.ui;

import bl0.aeon.render.api.data.render.IRenderable;
import bl0.aeon.render.api.resource.Font;
import bl0.aeon.render.api.resource.Mesh;
import bl0.aeon.render.api.resource.ShaderProgram;
import org.joml.Vector4f;

public interface ITextRenderable extends IRenderable {
    String getText();
    Font getFont();
    Vector4f getTextColor();
    ShaderProgram getTextShader();
    Mesh getTextMesh();
}
