package bl0.aeon.engine.data.render.ui;

import bl0.aeon.api.component.graphic.Material;
import bl0.aeon.render.api.data.render.ui.IUIRenderable;
import bl0.aeon.render.api.resource.Mesh;
import bl0.aeon.render.api.resource.ShaderProgram;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class UIRenderObj implements IUIRenderable {
    private final Vector2f position;
    private final Vector2f size;
    private final Vector4f color;
    private final Quaternionf rotation;
    private final Mesh mesh;
    private final ShaderProgram shader;
    private final Vector4f padding;

    public UIRenderObj(Vector2f position, Vector2f size, Vector4f padding, Quaternionf rotation, Material material, Mesh mesh) {
        this.position = position;
        this.size = size;
        this.rotation = rotation;
        this.color = material.getColor();
        this.mesh = mesh;
        this.shader = material.getShaderProgram();
        this.padding = padding;
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public Vector2f getRenderPosition() {
        return getPosition().add(padding.x, padding.y);
    }

    @Override
    public Vector2f getSize() {
        return size;
    }

    @Override
    public Quaternionf getRotation() {
        return rotation;
    }

    @Override
    public Vector4f getPadding() {
        return padding;
    }

    @Override
    public Vector4f getBackgroundColor() {
        return color;
    }

    @Override
    public ShaderProgram getShaderProgram() {
        return shader;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }
}
