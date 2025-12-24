package bl0.aeon.framework.components.xyz;

import bl0.aeon.base.component.graphic.Material;
import bl0.aeon.base.component.graphic.Model;
import bl0.aeon.base.component.graphic.Transform;
import bl0.aeon.base.component.interfaces.InstancesContainerComponent;
import bl0.aeon.base.component.interfaces.UpdateConsumerComponent;
import bl0.aeon.base.core.IEngineContext;
import bl0.aeon.base.core.IFrameContext;
import bl0.aeon.engine.data.component.BaseComponent;
import org.joml.Matrix4f;
import org.joml.Random;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

// complete class was written by chatGPT (im lazy + deadline are soon)
public class ParticleSpawnerComponent extends BaseComponent implements InstancesContainerComponent, UpdateConsumerComponent {

    private static final int FLOATS_PER_INSTANCE = 16;

    // render binding
    private Material material;
    private Model model;

    // instance buffer
    private FloatBuffer matrices;
    private int instanceCount = 0;

    // particle sim
    private final Particle[] particles;
    private int alive = 0;

    // emitter params
    public float emissionRate = 200f;
    public int maxParticles;
    public float lifeMin = 0.3f;
    public float lifeMax = 1.2f;

    public float sizeMin = 0.03f;
    public float sizeMax = 0.12f;

    public float speedMin = 0.6f;
    public float speedMax = 2.2f;

    public Vector3f direction = new Vector3f(0, 1, 0);
    public float spread = 0.6f;

    public Vector3f gravity = new Vector3f(0, -2.0f, 0);
    public float drag = 0.0f;

    public Vector3f localOffset = new Vector3f(0, 0, 0);

    public boolean useParentRotation = true;
    public boolean rotateParticlesWithParent = false;
    public boolean gravityInLocalSpace = false;

    private final Random rnd = new Random();
    private float emitAcc = 0f;

    public ParticleSpawnerComponent(int maxParticles) {
        this.maxParticles = Math.max(1, maxParticles);
        this.particles = new Particle[this.maxParticles];
        for (int i = 0; i < this.maxParticles; i++) particles[i] = new Particle();

        this.isEnabled = true;
        this.matrices = BufferUtils.createFloatBuffer(this.maxParticles * FLOATS_PER_INSTANCE);
    }

    @Override
    public int getInstanceCount() {
        return instanceCount;
    }

    @Override
    public FloatBuffer getInstanceMatrices() {
        FloatBuffer view = matrices.duplicate();
        view.position(0);
        view.limit(instanceCount * FLOATS_PER_INSTANCE);
        return view;
    }

    @Override
    public Material getMaterial() { return material; }

    @Override
    public void setMaterial(Material material) { this.material = material; }

    @Override
    public Model getModel() { return model; }

    @Override
    public void setModel(Model model) { this.model = model; }

    // ---- Update ----

    @Override
    public void update(IFrameContext fCtx, IEngineContext eCtx) {
        if (parent == null) return;

        float dt = (float) fCtx.getDeltaTime();
        if (dt <= 0f) return;

        emitAcc += emissionRate * dt;
        int toSpawn = (int) emitAcc;
        if (toSpawn > 0) emitAcc -= toSpawn;

        Vector3f origin = new Vector3f();
        Vector3f worldDir = new Vector3f(direction);

        Vector3f worldGravity = new Vector3f(gravity);
        Transform tr = parent.hasComponent(Transform.class) ? parent.getComponent(Transform.class) : null;
        if (gravityInLocalSpace && tr != null) {
            tr.getRotation().transform(worldGravity);
        }
        if (tr != null) {
            origin.set(tr.getPosition());

            if (useParentRotation) {
                // origin += rot * localOffset
                Vector3f off = new Vector3f(localOffset);
                tr.getRotation().transform(off);
                origin.add(off);

                // worldDir = rot * direction
                tr.getRotation().transform(worldDir);
            } else {
                origin.add(localOffset);
            }
        }

        worldDir.normalize();
        for (int i = 0; i < toSpawn; i++) spawn(origin, worldDir, tr);

        for (int i = 0; i < alive; ) {
            Particle p = particles[i];
            p.life -= dt;

            if (p.life <= 0f) {
                // swap-remove
                particles[i].set(particles[alive - 1]);
                particles[alive - 1].reset();
                alive--;
                continue;
            }

            // v += g*dt
            p.vel.fma(dt, worldGravity);

            // drag
            if (drag > 0f) {
                float k = Math.max(0f, 1f - drag * dt);
                p.vel.mul(k);
            }

            // pos += v*dt
            p.pos.fma(dt, p.vel);

            // rotation (optional)
            p.rot += p.rotSpeed * dt;

            i++;
        }

        rebuildMatrices(eCtx);
    }

    private void spawn(Vector3f origin, Vector3f worldDir, Transform parentTr) {
        if (alive >= maxParticles) return;

        Particle p = particles[alive++];
        p.reset();

        p.pos.set(origin);

        p.life = randRange(lifeMin, lifeMax);
        p.size = randRange(sizeMin, sizeMax);

        Vector3f dir = new Vector3f(worldDir);
        dir.add(randSigned() * spread, randSigned() * spread, randSigned() * spread).normalize();

        float speed = randRange(speedMin, speedMax);
        p.vel.set(dir).mul(speed);

        p.rot = rnd.nextFloat() * (float)(Math.PI * 2.0);
        p.rotSpeed = randSigned() * 6.0f;

        if (rotateParticlesWithParent && parentTr != null) {
            p.rot = (float)Math.atan2(dir.y, dir.x);
        }
    }

    private void rebuildMatrices(IEngineContext eCtx) {
        instanceCount = 0;
        matrices.clear();

        Matrix4f m = new Matrix4f();

        for (int i = 0; i < alive; i++) {
            Particle p = particles[i];

            m.identity().translation(p.pos);
            m.rotateZ(p.rot);
            m.scale(p.size, p.size, p.size);

            m.get(matrices);
            matrices.position(matrices.position() + 16);
            instanceCount++;
        }
    }

    private float randRange(float a, float b) {
        return a + rnd.nextFloat() * (b - a);
    }

    private float randSigned() {
        return rnd.nextFloat() * 2f - 1f;
    }

    // ---- internal particle ----
    private static class Particle {
        final Vector3f pos = new Vector3f();
        final Vector3f vel = new Vector3f();
        float life = 0f;
        float size = 1f;
        float rot = 0f;
        float rotSpeed = 0f;

        void reset() {
            pos.set(0, 0, 0);
            vel.set(0, 0, 0);
            life = 0f;
            size = 1f;
            rot = 0f;
            rotSpeed = 0f;
        }

        void set(Particle other) {
            pos.set(other.pos);
            vel.set(other.vel);
            life = other.life;
            size = other.size;
            rot = other.rot;
            rotSpeed = other.rotSpeed;
        }
    }
}
