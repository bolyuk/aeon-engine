package bl0.aeon.engine.data.component.ui;

import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.api.core.IEngineContext;
import bl0.aeon.engine.data.component.AE_Material;
import bl0.aeon.render.api.c.Colors;
import bl0.aeon.render.api.c.resources.Fonts;
import bl0.aeon.render.api.c.resources.ShaderPrograms;
import bl0.aeon.render.api.resource.Font;
import bl0.aeon.api.component.ui.UITextElement;
import bl0.aeon.render.api.resource.Mesh;

public class TextObject extends BaseUIElement implements UITextElement {
    public String text;
    public Font font;
    public Material textMaterial = new AE_Material();
    public Mesh textMesh;

    public TextObject(String name, IEngineContext eCtx) {
        super(name, eCtx);
        font = eCtx.getResourceManager().getResource(Fonts.DEFAULT, Font.class);
        textMesh = eCtx.getResourceFabric().createUITextMesh(name+"_text");
        textMaterial.setShaderProgram(eCtx.getResourceManager().getResource(ShaderPrograms.TEXT_SOLID));
        textMaterial.setColor(Colors.WHITE);
    }

    public TextObject(String name) {
        super(name);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Mesh getTextMesh() {
        return textMesh;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public Material getTextMaterial() {
        return textMaterial;
    }
}
