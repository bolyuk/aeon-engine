package bl0.aeon.engine.data.render;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.component.interfaces.InstancesContainerComponent;
import bl0.aeon.render.common.data.light.DirectionalLight;
import bl0.aeon.render.common.data.light.PointLight;
import bl0.aeon.render.common.data.render.IInstancedRenderable;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;
import java.util.List;

public class InstancedRenderObj implements IInstancedRenderable {

    private final DirectionalLight directionalLight;
    private final List<PointLight> pointLights;
    private final Mesh mesh;
    private final boolean isDepthTest;

    private final InstancesContainerComponent icc;
    private final Material material;

    public InstancedRenderObj(Material material,
                      InstancesContainerComponent icc,
                      Mesh mesh,
                      DirectionalLight directionalLight,
                      List<PointLight> pointLights,
                      boolean isDepthTest) {
        this.material = material;
        this.icc = icc;
        this.mesh = mesh;
        this.directionalLight = directionalLight;
        this.pointLights = pointLights;
        this.isDepthTest = isDepthTest;
    }

    @Override
    public Vector4f getColor() {
        return material.getColor();
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
    public List<PointLight> getPointLights() {
        return pointLights;
    }

    @Override
    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public boolean isDepthTestEnabled() {
        return isDepthTest;
    }

    @Override
    public int getInstanceCount() {
        return icc.getInstanceCount();
    }

    @Override
    public FloatBuffer getInstanceMatrices() {
        return icc.getInstanceMatrices();
    }
}
