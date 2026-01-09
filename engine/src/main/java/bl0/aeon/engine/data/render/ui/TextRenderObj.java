package bl0.aeon.engine.data.render.ui;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.render.common.data.render.ui.ITextRenderable;
import bl0.aeon.render.common.resource.Font;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TextRenderObj extends UIRenderObj implements ITextRenderable {

    private final Mesh textMesh;
    private final Material textMaterial;

    private final String text;

    private final Font font;

    public TextRenderObj(Vector2f position, Vector2f size, Material material, Mesh mesh, Font font, String text, Material textMaterial, Mesh textMesh) {
        super(position, size, material, mesh);
        this.textMesh = textMesh;
        this.textMaterial = textMaterial;
        this.text = text;
        this.font = font;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public Vector4f getTextColor() {
        return textMaterial.getColor();
    }

    @Override
    public ShaderProgram getTextShader() {
        return textMaterial.getShaderProgram();
    }

    @Override
    public Mesh getTextMesh() {
        return textMesh;
    }
}
