package bl0.aeon.render.common.data.render.ui;

import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.resource.Font;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface ITextRenderable extends IRenderable {
    String getText();
    Font getFont();
    Vector4f getTextColor();
    ShaderProgram getTextShader();
    Mesh getTextMesh();
}
