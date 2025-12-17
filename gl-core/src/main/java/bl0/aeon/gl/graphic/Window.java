package bl0.aeon.gl.graphic;

import bl0.aeon.gl.base.CoreException;
import org.lwjgl.glfw.GLFW;

public class Window {
    public long ID;
    public int width;
    public int height;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.ID = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
        if (this.ID == 0L) {
            throw new CoreException("Failed to create the GLFW window");
        }
    }
}

