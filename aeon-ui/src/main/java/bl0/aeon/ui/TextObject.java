package bl0.aeon.ui;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.base.scene.SceneObject;
import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.data.render.ISingleRenderable;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.List;

public class TextObject extends SceneObject implements ISingleRenderable {

    public String text;

    public Material material;

    public Transform transform;

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
    public Texture getTexture() {
        return material.getTexture();
    }

    @Override
    public Vector4f getColor() {
        return material.getColor();
    }

    @Override
    public List<PointLight> getPointLights() {
        return List.of();
    }

    @Override
    public DirectionalLight getDirectionalLight() {
        return null;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public boolean isDepthTestEnabled() {
        return false;
    }

    @Override
    public Matrix4f getMatrix() {
        return transform.getMatrix();
    }
}
