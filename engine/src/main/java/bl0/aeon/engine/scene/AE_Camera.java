package bl0.aeon.engine.scene;

import bl0.aeon.render.api.data.render.Camera;
import bl0.bjs.common.core.relations.FactorizedObject;
import bl0.bjs.common.core.relations.ObservableObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AE_Camera implements Camera {
    public final ObservableObject<Vector3f> position = new ObservableObject<Vector3f>(new Vector3f(0.0f, 0.0f, 5.0f));
    public final ObservableObject<Vector3f> direction = new ObservableObject<Vector3f>(new Vector3f(0.0f, 0.0f, -1.0f));
    public final ObservableObject<Vector3f> up = new ObservableObject<Vector3f>(new Vector3f(0.0f, 1.0f, 0.0f));
    public ObservableObject<Float> aspectRatio = new ObservableObject<Float>(1.3333334f);

    public FactorizedObject<Matrix4f> viewMatrix = new FactorizedObject<Matrix4f>(new Matrix4f(), (e) -> {
        Vector3f target = new Vector3f();
        this.position.get().add(this.direction.get(), target);
        return new Matrix4f().lookAt(this.position.get(), target, this.up.get());
    }, this.position, this.direction, this.up);

    public final ObservableObject<Float> fov = new ObservableObject<>(60.0f);

    public FactorizedObject<Matrix4f> projectionMatrix =
            new FactorizedObject<>(new Matrix4f(),
                    e -> new Matrix4f().perspective((float) Math.toRadians(this.fov.get()),
                            this.aspectRatio.get(), 0.1f, 1000.0f),
                    this.aspectRatio, this.fov);
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

    @Override
    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    @Override
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio.set(aspectRatio);
    }
}

