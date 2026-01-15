package bl0.aeon.engine.data.component;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.component.graphic.Model;
import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.component.interfaces.InstancesContainerComponent;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class AE_InstancedContainerComponent extends BaseComponent implements InstancesContainerComponent {

    private static final int FLOATS_PER_INSTANCE = 16;

    private transient FloatBuffer matrices;
    private int instanceCount = 0;
    private Material material;
    private Model model;

    public AE_InstancedContainerComponent(int initialCapacity) {
        int cap = Math.max(1, initialCapacity);
        this.matrices = BufferUtils.createFloatBuffer(cap * FLOATS_PER_INSTANCE);
    }

    public void clear() {
        instanceCount = 0;
        matrices.clear();
    }

    @Override
    public int getInstanceCount() {
        return instanceCount;
    }

    public void passData(Transform transform) {
        if (transform == null) return;
        passMatrix(transform.getMatrix());
    }

    @Override
    public FloatBuffer getInstanceMatrices() {
        FloatBuffer view = matrices.duplicate();
        view.position(0);
        view.limit(instanceCount * 16);
        return view;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    private void ensureCapacity(int neededInstances) {
        int neededFloats = neededInstances * FLOATS_PER_INSTANCE;
        if (neededFloats <= matrices.capacity()) return;

        int newCapFloats = matrices.capacity();
        while (newCapFloats < neededFloats) newCapFloats *= 2;

        FloatBuffer newBuf = BufferUtils.createFloatBuffer(newCapFloats);
        matrices.position(0);
        matrices.limit(instanceCount * 16);
        newBuf.put(matrices);
        matrices = newBuf;
    }

    public void passMatrix(Matrix4f m) {
        ensureCapacity(instanceCount + 1);

        int base = instanceCount * 16;
        matrices.position(base);
        m.get(matrices);
        matrices.position(base + 16);

        instanceCount++;
    }
}