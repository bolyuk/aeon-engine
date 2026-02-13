package bl0.aeon.gl.graphic;

import bl0.aeon.gl.base.CoreException;
import org.lwjgl.glfw.GLFW;

public class Window {
    public long ID;
    public int width;
    public int height;

    public boolean fullscreen;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
    }
}

