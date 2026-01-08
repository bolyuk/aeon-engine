package bl0.aeon.ui;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.base.scene.SceneObject;
import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.data.render.ui.ITextRenderable;
import bl0.aeon.render.common.resource.Font;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class TextObject extends SceneObject implements ITextRenderable {

    public String text;

    public Material material;

    public Font font;

    public Vector2f position = new Vector2f();

    public Mesh mesh;

    public TextObject(String name) {
        super(name);
    }

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {

    }
    @Override
    public ShaderProgram getShaderProgram() {
        return material.getShaderProgram();
    }
    @Override
    public Vector4f getColor() {
        return material.getColor();
    }

    @Override
    public Mesh getMesh() {
        return mesh;
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
    public Vector2f getPosition() {
        return position;
    }
}
