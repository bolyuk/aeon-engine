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
import bl0.bjs.common.core.relations.v2.xtra.BooleanProperty;
import bl0.bjs.common.core.relations.v2.xtra.StringProperty;
import org.joml.Vector4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;

public class TextObject extends BaseUIElement implements UITextElement {
    public final StringProperty text = new StringProperty();
    public final Property<Font> font = new Property<>();
    public Material textMaterial = new AE_Material();
    public Material textHoverMaterial = new AE_Material();
    public Mesh textMesh;

    public TextObject(String name, IEngineContext eCtx) {
        super(name, eCtx);
        font.set(eCtx.getResourceManager().getResource(Fonts.DEFAULT, Font.class));
        mesh = eCtx.getResourceFabric().createPlane(name+"_mesh");
        textMesh = eCtx.getResourceFabric().createUITextMesh(name+"_text");

        textMaterial.setShaderProgram(eCtx.getResourceManager().getResource(ShaderPrograms.TEXT_SOLID));
        textMaterial.setColor(new Vector4f(Colors.WHITE));

        textHoverMaterial.setShaderProgram(eCtx.getResourceManager().getResource(ShaderPrograms.TEXT_SOLID));
        // copy first: JOML's div() mutates in place, and Colors.WHITE is a shared constant (this used to
        // double it for every TextObject ever created, blowing every default material color out to >>1)
        textHoverMaterial.setColor(new Vector4f(Colors.WHITE).div(0.5f));

        text.addListener(x -> recalculateTextSize());
        font.addListener(x -> recalculateTextSize());
        padding.addListener(x -> recalculateTextSize());
        material.setDepthTestEnabled(false);
    }

    public TextObject(String name) {
        super(name);
    }

    private void recalculateTextSize() {
        Vector2f calculated = font.get().calculateSize(text.get()).add(padding.get().x+padding.get().z,padding.get().y+padding.get().w);

        baseSize.set(calculated);

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
        return isMouseHover.get() && textHoverMaterial != null ? textHoverMaterial : textMaterial;
    }
}
