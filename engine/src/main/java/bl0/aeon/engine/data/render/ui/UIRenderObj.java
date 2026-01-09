package bl0.aeon.engine.data.render.ui;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.render.common.data.render.ui.IUIRenderable;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class UIRenderObj implements IUIRenderable {
    private final Vector2f position;
    private final Vector2f size;
    private final Vector4f color;
    private final Mesh mesh;
    private final ShaderProgram shader;

    public UIRenderObj(Vector2f position, Vector2f size, Material material, Mesh mesh) {
        this.position = position;
        this.size = size;
        this.color = material.getColor();
        this.mesh = mesh;
        this.shader = material.getShaderProgram();
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public Vector2f getSize() {
        return size;
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
