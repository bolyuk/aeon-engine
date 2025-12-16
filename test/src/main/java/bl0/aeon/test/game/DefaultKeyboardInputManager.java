/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.test.game;

import bl0.bjs.socket.base.IWSBase;
import org.bl0.aeon.core.components.Camera;
import org.bl0.aeon.engine.context.GameContext;
import org.bl0.aeon.engine.interfaces.input.AbstractInputManager;
import org.bl0.aeon.test.com.IGameService;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class DefaultKeyboardInputManager
extends AbstractInputManager {
    private boolean firstMouse = true;
    private double pitch;
    private double last_x;
    private double last_y;
    private double yaw = -90.0;
    private final float sensitivity = 0.1f;
    private GameContext gameContext;
    private final GLFWCursorPosCallbackI mouse_callback = (win, xpos, ypos) -> {
        if (this.firstMouse) {
            this.last_x = xpos;
            this.last_y = ypos;
            this.firstMouse = false;
        }
        double xoffset = xpos - this.last_x;
        double yoffset = this.last_y - ypos;
        this.last_x = xpos;
        this.last_y = ypos;
        this.yaw += xoffset * (double)0.1f;
        this.pitch += yoffset * (double)0.1f;
        if (this.pitch > 89.0) {
            this.pitch = 89.0;
        }
        if (this.pitch < -89.0) {
            this.pitch = -89.0;
        }
        float x = (float)(Math.cos(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)));
        float y = (float)Math.sin(Math.toRadians(this.pitch));
        float z = (float)(Math.sin(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)));
        Camera camera = this.gameContext.scene.getCamera();
        camera.direction.set(new Vector3f(x, y, z).normalize());

        IGameService service = ((IGameService)gameContext.scene);
        service.commitMove(camera.position.get(), camera.direction.get(), null);
    };

    @Override
    public void processInput(GameContext gameContext) {
        long window = gameContext.window.ID;
        Camera camera = gameContext.scene.getCamera();
        float velocity = (float)((double)camera.speed * gameContext.deltaTime);
        if (GLFW.glfwGetKey(window, 87) == 1) {
            camera.position.change(obj -> obj.fma(velocity, (Vector3fc)camera.direction.get()));
        }
        if (GLFW.glfwGetKey(window, 83) == 1) {
            camera.position.change(obj -> obj.fma(-velocity, (Vector3fc)camera.direction.get()));
        }
        if (GLFW.glfwGetKey(window, 65) == 1) {
            Vector3f left = new Vector3f(camera.direction.get()).cross(camera.up.get()).normalize();
            camera.position.change(obj -> obj.fma(-velocity, (Vector3fc)left));
        }
        if (GLFW.glfwGetKey(window, 68) == 1) {
            Vector3f right = new Vector3f(camera.direction.get()).cross(camera.up.get()).normalize();
            camera.position.change(obj -> obj.fma(velocity, (Vector3fc)right));
        }
        if (GLFW.glfwGetKey(window, 256) == 1) {
            gameContext.dispose();
        }
    }

    @Override
    public void bind(GameContext data) {
        this.gameContext = data;
        long window = data.window.ID;
        GLFW.glfwSetInputMode(window, 208897, 212995);
        GLFW.glfwSetCursorPosCallback(window, this.mouse_callback);
    }

    @Override
    public void unbind(GameContext data) {
        this.gameContext = null;
        long window = data.window.ID;
        GLFW.glfwSetCursorPosCallback(window, null);
        GLFW.glfwSetInputMode(window, 208897, 212993);
    }
}

