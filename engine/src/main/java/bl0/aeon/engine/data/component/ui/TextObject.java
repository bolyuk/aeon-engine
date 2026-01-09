package bl0.aeon.engine.data.component.ui;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.engine.data.component.AE_Material;
import bl0.aeon.render.common.c.Colors;
import bl0.aeon.render.common.c.resources.Fonts;
import bl0.aeon.render.common.c.resources.ShaderPrograms;
import bl0.aeon.render.common.resource.Font;
import bl0.aeon.base.component.ui.UITextElement;
import bl0.aeon.render.common.resource.Mesh;

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
