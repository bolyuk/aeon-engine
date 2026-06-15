package bl0.aeon.api.scene.properties;

import bl0.bjs.common.core.relations.v2.Property;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vector3fProperty extends Property<Vector3f> {

    public Vector3fProperty(Vector3f v) {
        this.value = v;
    }

    public Vector3fProperty() {
        this.value = new Vector3f();
    }

    public Vector3fProperty set(float x, float y, float z) {
        this.value.set(x, y, z);
        invokeChangeAction();
        return this;
    }

    public Vector3fProperty setX(float x) {
        this.value.x = x;
        invokeChangeAction();
        return this;
    }

    public Vector3fProperty setY(float y) {
        this.value.y = y;
        invokeChangeAction();
        return this;
    }

    public Vector3fProperty setZ(float z) {
        this.value.z = z;
        invokeChangeAction();
        return this;
    }

    public float x()
    {
        return value.x();
    }

    public float y()
    {
        return value.y();
    }

    public float z()
    {
        return value.z();
    }
}
