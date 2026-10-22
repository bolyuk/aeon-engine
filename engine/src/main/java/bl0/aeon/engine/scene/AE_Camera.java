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
    /** Near and far clipping planes. Everything farther than {@code far} is cut off by the projection, so a game that draws
     *  further than the default 1000 must raise it (a plane cutting through the scene, turning with the camera). */
    public final ObservableObject<Float> near = new ObservableObject<>(0.1f);
    public final ObservableObject<Float> far = new ObservableObject<>(1000.0f);

    public FactorizedObject<Matrix4f> projectionMatrix =
            new FactorizedObject<>(new Matrix4f(),
                    e -> new Matrix4f().perspective((float) Math.toRadians(this.fov.get()),
                            this.aspectRatio.get(), this.near.get(), this.far.get()),
                    this.aspectRatio, this.fov, this.near, this.far);
    private final Matrix4f view = new Matrix4f(), projection = new Matrix4f();

    /**
     * The view matrix of the current position, direction and up, worked out on every call. (The {@link #viewMatrix} factorized
     * value missed updates - in about half the frames the scene was drawn with the previous frame's camera, which showed as
     * whatever moves with the camera jumping back and forth by a frame's travel.)
     */
    @Override
    public Matrix4f getViewMatrix() {
        Vector3f p = position.get(), d = direction.get(), u = up.get();
        return view.setLookAt(p.x, p.y, p.z, p.x + d.x, p.y + d.y, p.z + d.z, u.x, u.y, u.z);
    }

    /** The projection of the current fov, aspect ratio and clip planes, worked out on every call (see {@link #getViewMatrix}). */
    @Override
    public Matrix4f getProjectionMatrix() {
        return projection.setPerspective((float) Math.toRadians(fov.get()), aspectRatio.get(), near.get(), far.get());
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

