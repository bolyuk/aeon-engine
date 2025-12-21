package bl0.aeon.gl.core;

import bl0.aeon.render.common.core.RenderEngine;
import bl0.aeon.render.common.core.RenderFrame;
import bl0.aeon.render.common.resource.IDisposable;
import bl0.aeon.gl.base.CoreException;
import bl0.aeon.gl.graphic.Window;
import bl0.bjs.common.base.BJSBaseClass;
import bl0.bjs.common.base.IContext;
import bl0.bjs.common.core.event.Action;
import bl0.bjs.common.core.event.ActionController;
import bl0.bjs.common.core.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class GLEngine extends BJSBaseClass implements IDisposable, RenderEngine {

    private final ActionController<Pair<Integer, Integer>> windowSizeChangedController = new ActionController<>();

    private Window window;

    public GLEngine(IContext ctx) {
        super(ctx);
    }

    private void initGL() {
        GLFW.glfwInit();
        GLFW.glfwWindowHint(139266, 3);
        GLFW.glfwWindowHint(139267, 3);
        GLFW.glfwWindowHint(139272, 204801);
        GLFW.glfwMakeContextCurrent(window.ID);
        GL.createCapabilities();
        GL11.glViewport(0, 0, window.width, 600);
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(2884);
        GL11.glCullFace(1029);
        GL11.glFrontFace(2305);
        GL11.glDepthFunc(513);
    }

    public double getTime() {
        return GLFW.glfwGetTime();
    }

    public void setOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener) {
        windowSizeChangedController.register(onWindowSizeChangedListener);
    }

    public void remOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener) {
        windowSizeChangedController.unregister(onWindowSizeChangedListener);
    }

    @Override
    public void render(RenderFrame renderContext) {
        if(!GLFW.glfwWindowShouldClose(window.ID)) {
            GL11.glClear(16640);

            GLFW.glfwSwapBuffers(window.ID);
            GLFW.glfwPollEvents();
        }
    }


    @Override
    public void dispose() {
        GLFW.glfwTerminate();
    }

    @Override
    public void initialize(String title, int width, int height) {
        try {
            this.window = new Window(width, height, title);

            window.ID = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
            if (window.ID == 0L) {
                throw new CoreException("Failed to create the GLFW window");
            }
            initGL();
        } catch (Exception e) {
            l.err(e);
            GLFW.glfwTerminate();
            return;
        }

        GLFW.glfwSetFramebufferSizeCallback(window.ID, (win, w, h) -> {
            GL11.glViewport(0, 0, w, h);
            windowSizeChangedController.invoke(Pair.of(w, h));
        });
    }
}

