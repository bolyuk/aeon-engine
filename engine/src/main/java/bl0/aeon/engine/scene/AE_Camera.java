package bl0.aeon.engine.scene;

import bl0.aeon.render.common.data.render.Camera;
import bl0.bjs.common.core.relations.BoundObject;
import bl0.bjs.common.core.relations.NotifyObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AE_Camera implements Camera {
    public final NotifyObject<Vector3f> position = new NotifyObject<Vector3f>(new Vector3f(0.0f, 0.0f, 5.0f));
    public final NotifyObject<Vector3f> direction = new NotifyObject<Vector3f>(new Vector3f(0.0f, 0.0f, -1.0f));
    public final NotifyObject<Vector3f> up = new NotifyObject<Vector3f>(new Vector3f(0.0f, 1.0f, 0.0f));
    public float fov = 60.0f;
    public NotifyObject<Float> aspectRatio = new NotifyObject<Float>(1.3333334f);

    public BoundObject<Matrix4f> viewMatrix = new BoundObject<Matrix4f>(new Matrix4f(), (e) -> {
        Vector3f target = new Vector3f();
        this.position.get().add(this.direction.get(), target);
        return new Matrix4f().lookAt(this.position.get(), target, this.up.get());
    }, this.position, this.direction, this.up);

    public BoundObject<Matrix4f> projectionMatrix = new BoundObject<Matrix4f>(new Matrix4f(), (e) -> new Matrix4f().perspective((float)Math.toRadians(this.fov), this.aspectRatio.get().floatValue(), 0.1f, 1000.0f), this.aspectRatio);


    @Override
    public Matrix4f getViewMatrix() {
        return viewMatrix.get();
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix.get();
    }

    @Override
    public Vector3f getPosition() {
        return position.get();
    }
}

