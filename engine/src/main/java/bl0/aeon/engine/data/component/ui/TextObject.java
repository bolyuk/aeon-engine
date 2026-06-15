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
import bl0.bjs.common.core.relations.ObservableObject;
import bl0.bjs.common.core.relations.v2.Property;
import bl0.bjs.common.core.relations.v2.xtra.StringProperty;
import org.joml.Quaternionf;
import org.joml.Vector2f;

public class TextObject extends BaseUIElement implements UITextElement {
    public final StringProperty text = new StringProperty();
    public final Property<Font> font = new Property<>();
    public Material textMaterial = new AE_Material();
    public Mesh textMesh;

    public TextObject(String name, IEngineContext eCtx) {
        super(name, eCtx);
        font.set(eCtx.getResourceManager().getResource(Fonts.DEFAULT, Font.class));
        mesh = eCtx.getResourceFabric().createPlane(name+"_mesh");
        textMesh = eCtx.getResourceFabric().createUITextMesh(name+"_text");
        textMaterial.setShaderProgram(eCtx.getResourceManager().getResource(ShaderPrograms.TEXT_SOLID));
        textMaterial.setColor(Colors.WHITE);
        text.addListener(x -> recalculateTextSize());
        font.addListener(x -> recalculateTextSize());
        material.setDepthTestEnabled(false);
    }

    public TextObject(String name) {
        super(name);
    }

    private void recalculateTextSize() {
        Vector2f calculated = font.get().calculateSize(text.get());

        // запоминаем чистый размер текста
        baseSize.set(calculated);

        // если есть поворот — сразу считаем AABB, иначе ставим как есть
        Quaternionf q = rotation.get();
        if (q != null && (q.z != 0f || q.x != 0f || q.y != 0f)) {
            recalculateSizeForRotation();
        } else {
            size.set(calculated);
        }
    }

    @Override
    public String getText() {
        return text.get();
    }

    @Override
    public Mesh getTextMesh() {
        return textMesh;
    }

    @Override
    public Font getFont() {
        return font.get();
    }

    @Override
    public Material getTextMaterial() {
        return textMaterial;
    }
}
