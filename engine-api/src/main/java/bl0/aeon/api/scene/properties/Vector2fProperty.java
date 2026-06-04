package bl0.aeon.api.scene.properties;

import bl0.bjs.common.core.relations.v2.Property;
import org.joml.Vector2f;

public class Vector2fProperty extends Property<Vector2f> {

    private float aspectRatio = 0;

    public Vector2fProperty(Vector2f v) {
        this.value = v;
    }

    public Vector2fProperty() {
        this.value = new Vector2f();
    }

    public Vector2fProperty set(float x, float y) {
        this.value.set(x, y);
        invokeChangeAction();
        return this;
    }

    public Vector2fProperty setX(float x) {
        this.value.x = x;
        invokeChangeAction();
        return this;
    }

    public Vector2fProperty setY(float y) {
        this.value.y = y;
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

    public float getAspectRatio(){
        return aspectRatio;
    }

    @Override
    public Property<Vector2f> set(Vector2f value) {
        super.set(value);
        calcAspectRatio();
        return this;
    }

    private void calcAspectRatio()
    {
        aspectRatio = x()/y();
    }
}
