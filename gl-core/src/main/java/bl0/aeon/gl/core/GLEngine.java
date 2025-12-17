package bl0.aeon.gl.core;

import bl0.aeon.common.core.GameEngine;
import bl0.aeon.common.core.SceneRenderer;
import bl0.aeon.common.context.IGameSettings;
import bl0.aeon.common.context.IRenderContext;
import bl0.aeon.common.core.IDispatcher;
import bl0.aeon.gl.base.IDisposable;
import bl0.aeon.gl.graphic.Window;
import bl0.bjs.common.base.BJSBaseClass;
import bl0.bjs.common.base.IContext;
import bl0.bjs.common.core.event.Action;
import bl0.bjs.common.core.event.ActionController;
import bl0.bjs.common.core.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class GLEngine extends BJSBaseClass implements GameEngine, IDisposable, SceneRenderer {

    private final ActionController<Pair<Integer, Integer>> windowSizeChangedController = new ActionController<>();

    private final EngineContext engineCtx;
    private Window window;

    public GLEngine(IContext ctx, IGameSettings settings, IDispatcher dispatcher) {
        super(ctx);
        this.engineCtx = new EngineContext(ctx, settings, dispatcher);
    }

    public void init() {
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

    public void start() {
        throwIfNotInitialized();
        isRunning = true;
        lastFrame = GLFW.glfwGetTime();
        while (isRunning && !GLFW.glfwWindowShouldClose(context.window.ID)) {
            
            synchronized (lock_obj) {
                GL11.glClear(16640);
                double current = GLFW.glfwGetTime();
                double delta = current - lastFrame;
                lastFrame = current;
                if (delta > 1.0 || delta <= 0.0) {
                    delta = 0.016;
                }
                drawContext.deltaTime = delta;
                context.deltaTime = delta;
                doTick();
                GLFW.glfwSwapBuffers(context.window.ID);
                GLFW.glfwPollEvents();
            }
        }
    }


    @Override
    public void render(IRenderContext renderContext) {
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
    public void createWindow(String title, int width, int height) {
        try {
            this.window = new Window(width, height, title);
            ((FrameContext)this.engineCtx.getFrameContext()).window = this.window;
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

