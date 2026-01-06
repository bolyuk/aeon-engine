package bl0.aeon.physic;

import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import bl0.aeon.render.common.c.Vectors;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PhysicBodyComponent extends BaseComponent implements UpdateConsumerComponent {

    public Vector3f velocity = new Vector3f();
    public float mass = 1f;
    public float damping = 1f;

    private final Vector3f accumulatedForce = new Vector3f();
    public boolean manualControl;
    private float accumulatedTorqueY = 0f;

    public float angularVelocityY = 0f;
    public float angularDamping = 1f;
    public float inertia = 1f;

    public boolean faceVelocity = false;
    public float faceVelocityStrength = 6f;
    public Vector3f forwardAxisLocal = new Vector3f(0, 0, 1);

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        if (parent == null || !parent.hasComponent(Transform.class)) return;
        Transform t = parent.getComponent(Transform.class);

        float dt = (float) fCtx.getDeltaTime();
        if (dt <= 0f) return;
        if (mass <= 0f) mass = 1f;
        if (inertia <= 0f) inertia = mass;

        Vector3f accel = new Vector3f(accumulatedForce).mul(1f / mass);
        velocity.fma(dt, accel);

        t.setPosition(new Vector3f(t.getPosition()).fma(dt, velocity));

        float linDamp = (float) Math.pow(damping, dt * 60f);
        velocity.mul(linDamp);

        accumulatedForce.zero();

        float angAcc = accumulatedTorqueY / inertia;
        angularVelocityY += angAcc * dt;

        Quaternionf rot = new Quaternionf(t.getRotation());
        rot.rotateY(-angularVelocityY * dt);

        float angDamp = (float) Math.pow(angularDamping, dt * 60f);
        angularVelocityY *= angDamp;

        accumulatedTorqueY = 0f;

        if (faceVelocity && velocity.lengthSquared() > 1e-8f) {
            Vector3f dir = new Vector3f(velocity.x, 0f, velocity.z).normalize();
            Vector3f fwd = new Vector3f(forwardAxisLocal).normalize();
            rot.transform(fwd);

            float currentYaw = (float) Math.atan2(fwd.x, fwd.z);
            float targetYaw  = (float) Math.atan2(dir.x, dir.z);
            float delta = wrapAngle(targetYaw - currentYaw);

            angularVelocityY += delta * faceVelocityStrength * dt;
        }

        t.setRotation(rot);

    }

    private float wrapAngle(float a) {
        while (a > Math.PI) a -= (float)(Math.PI * 2);
        while (a < -Math.PI) a += (float)(Math.PI * 2);
        return a;
    }

    public void applyForce(Vector3f force) {
        if (force == null) return;
        accumulatedForce.add(force);
    }

    public void applyForceAtPoint(Vector3f force, Vector3f worldPoint) {
        if (force == null || worldPoint == null) return;
        applyForce(force);

        if (parent == null || !parent.hasComponent(Transform.class)) return;
        Transform t = parent.getComponent(Transform.class);

        Vector3f r = new Vector3f(worldPoint).sub(t.getPosition());
        Vector3f cross = r.cross(force, new Vector3f());

        accumulatedTorqueY += cross.z;
    }
}

