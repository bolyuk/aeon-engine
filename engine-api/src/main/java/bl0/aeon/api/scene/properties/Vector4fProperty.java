package bl0.aeon.api.scene.properties;

import bl0.bjs.common.core.relations.v2.Property;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Vector4fProperty extends Property<Vector4f> {

    public Vector4fProperty(Vector4f v) {
        this.value = v;
    }

    public Vector4fProperty() {
        this.value = new Vector4f();
    }

    public Vector4fProperty set(float x, float y, float z) {
        this.value.set(x, y, z);
        invokeChangeAction();
        return this;
    }

    public Vector4fProperty set(float x, float y, float z, float w) {
        this.value.set(x, y, z, w);
        invokeChangeAction();
        return this;
    }

    public Vector4fProperty setX(float x) {
        this.value.x = x;
        invokeChangeAction();
        return this;
    }

    public Vector4fProperty setY(float y) {
        this.value.y = y;
        invokeChangeAction();
        return this;
    }

    public Vector4fProperty setZ(float z) {
        this.value.z = z;
        invokeChangeAction();
        return this;
    }

    public Vector4fProperty setW(float w) {
        this.value.w = w;
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

    public float w()
    {
        return value.w;
    }

    public static Vector4fProperty of(float value){
        return new Vector4fProperty(new Vector4f(value, value, value, value));
    }
}
