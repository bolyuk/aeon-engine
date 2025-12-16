/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.core.components;

import org.bl0.aeon.core.graphic.shaders.ShaderProgram;
import org.bl0.aeon.engine.interfaces.IUniformsSetter;
import org.bl0.aeon.engine.interfaces.component.IComponent;
import org.bl0.aeon.engine.relations.BoundObject;
import org.bl0.aeon.engine.relations.NotifyObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera
implements IUniformsSetter,
IComponent {
    public final NotifyObject<Vector3f> position = new NotifyObject<Vector3f>(new Vector3f(0.0f, 0.0f, 5.0f));
    public final NotifyObject<Vector3f> direction = new NotifyObject<Vector3f>(new Vector3f(0.0f, 0.0f, -1.0f));
    public final NotifyObject<Vector3f> up = new NotifyObject<Vector3f>(new Vector3f(0.0f, 1.0f, 0.0f));
    public float speed = 30.0f;
    public float fov = 60.0f;
    public NotifyObject<Float> aspectRatio = new NotifyObject<Float>(Float.valueOf(1.3333334f));
    public BoundObject<Matrix4f> viewMatrix = new BoundObject<Matrix4f>(new Matrix4f(), () -> {
        Vector3f target = new Vector3f();
        this.position.get().add(this.direction.get(), target);
        return new Matrix4f().lookAt(this.position.get(), target, this.up.get());
    }, this.position, this.direction, this.up);
    public BoundObject<Matrix4f> projectionMatrix = new BoundObject<Matrix4f>(new Matrix4f(), () -> new Matrix4f().perspective((float)Math.toRadians(this.fov), this.aspectRatio.get().floatValue(), 0.1f, 1000.0f), this.aspectRatio);

    @Override
    public void setUniforms(ShaderProgram program) {
        program.setUniform("projection", this.projectionMatrix);
        program.setUniform("view", this.viewMatrix);
        program.setUniform("viewPos", this.position);
    }

    @Override
    public Class<? extends IComponent> getComponentClass() {
        return Camera.class;
    }
}

