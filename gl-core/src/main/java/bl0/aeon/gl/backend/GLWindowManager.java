package bl0.aeon.gl.backend;

import bl0.aeon.gl.GLState;
import bl0.aeon.gl.base.CoreException;
import bl0.aeon.gl.base.GLBaseClass;
import bl0.aeon.gl.data.GLInputData;
import bl0.aeon.gl.graphic.Window;
import bl0.aeon.gl.utils.InputUtils;
import bl0.aeon.gl.utils.ViewportUtil;
import bl0.aeon.render.common.backend.IWindowManager;
import bl0.aeon.render.common.data.input.InputData;
import bl0.aeon.render.common.data.input.Key;
import bl0.bjs.common.base.IContext;
import bl0.bjs.common.core.event.action.Action;
import bl0.bjs.common.core.event.action.ActionController;
import bl0.bjs.common.core.tuple.Pair;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;

public class GLWindowManager extends GLBaseClass implements IWindowManager {
    private final ActionController<Pair<Integer, Integer>> windowSizeChangedController = new ActionController<>();

    private final HashMap<Integer, Key> keyMapping = InputUtils.getDefaultKeyMapping();

    private final HashMap<Integer, Key> mouseMapping = InputUtils.getDefaultMouseMapping();
    private Window window;
    public GLWindowManager(GLState state, IContext ctx) {
        super(state, ctx);
    }

    private final boolean[] mouseDown = new boolean[2];
    private final Vector2d mPos = new Vector2d();
    private final Vector2d mdt = new Vector2d();
    private boolean mouseInitialized = false;

    @Override
    public void initialize(String title, int width, int height) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new CoreException("glfwInit() failed");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_ANY_PROFILE);

        this.window = new Window(width, height, title);

        this.window.ID = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
        if (window.ID == 0L) {
            GLFW.glfwTerminate();
            throw new CoreException("Failed to create the GLFW window");
        }



        // because on KDE FramebufferSize != WindowSize
        setWindowSizeFromFramebuffer();

        glState.isWindowCreated = true;
        glState.windowID = this.window.ID;
    }

    private void setWindowSizeFromFramebuffer() {
        int[] fbw = new int[1];
        int[] fbh = new int[1];

        GLFW.glfwGetFramebufferSize(window.ID, fbw, fbh);
        window.width = fbw[0];
        window.height = fbh[0];
    }

    @Override
    public void bindContext() {
        if(!glState.isWindowCreated) {
            throw new CoreException("GLFW window is not created");
        }

        GLFW.glfwMakeContextCurrent(window.ID);
        GL.createCapabilities();

        int width = window.width;
        int height = window.height;
        // force update (because of KDE)
        changeViewPort(width, height, (float)width/height);

        GL30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glEnable(GL30.GL_CULL_FACE);

        // blending (for transparent textures)
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        GL30.glCullFace(GL30.GL_BACK);
        GL30.glFrontFace(GL30.GL_CCW);
        GL30.glDepthFunc(GL30.GL_LESS);

        GLFW.glfwSetFramebufferSizeCallback(window.ID, (win, w, h) -> {
            window.width = w;
            window.height = h;
            windowSizeChangedController.invoke(Pair.of(w, h));
        });

        GLFW.glfwSetCursorPosCallback(window.ID, (win, x, y) -> {
            if (!mouseInitialized) {
                mPos.set(x, y);
                mouseInitialized = true;
                return;
            }

            mdt.set(x - mPos.x, y - mPos.y);
            mPos.set(x, y);
        });

        GLFW.glfwSetMouseButtonCallback(window.ID, (win, button, action, mods) -> {
            if (button < 0 || button >= mouseDown.length) return;

            boolean down = (action == GLFW.GLFW_PRESS);
            mouseDown[button] = down;
        });

        GLFW.glfwSwapInterval(1);
        glState.isContextBound = true;
    }

    @Override
    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    @Override
    public InputData pollInputData() {
        GLInputData input = new GLInputData();

        for(var pair : keyMapping.entrySet()) {
            if(GLFW.glfwGetKey(window.ID, pair.getKey()) == GLFW.GLFW_PRESS)
                input.keys.add(pair.getValue());
        }

        for(int i=0; i<mouseDown.length; i++) {
            if(mouseDown[i])
                input.keys.add(mouseMapping.get(i));
        }

        input.mdt = mdt;
        input.mPos = mPos;
        return input.isAnyDown() ? input : null;
    }

    @Override
    public void terminate() {
        GLFW.glfwTerminate();
    }

    @Override
    public void captureCursor(boolean flag) {
        GLFW.glfwSetInputMode(window.ID, GLFW.GLFW_CURSOR, flag ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR);
    }

    @Override
    public void changeViewPort(int width, int height, float aspectRatio) {
        int[] data = ViewportUtil.calcViewport(width, height, aspectRatio);
        GL11.glViewport(data[0], data[1], data[2], data[3]);
    }

    @Override
    public void setWindowSize(int width, int height) {
        GLFW.glfwSetWindowSize(window.ID, width, height);
    }

    @Override
    public void setFullscreen(boolean value) {
        if(!glState.isWindowCreated)
            throw new CoreException("GLWindow is not created");
        if(!glState.isContextBound)
            throw new CoreException("GLContext is not bound");

        if(value) {
            long monitor = GLFW.glfwGetPrimaryMonitor(); //TODO
            GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);

            GLFW.glfwSetWindowMonitor(
                    window.ID,
                    monitor,
                    0, 0,
                    mode.width(),
                    mode.height(),
                    mode.refreshRate()
            );
        } else {
            GLFW.glfwSetWindowMonitor(
                    window.ID,
                    0L,
                    0,0, //TODO
                    800, 500,
                    0
            );
        }
        setWindowSizeFromFramebuffer();
        window.fullscreen = value;
        windowSizeChangedController.invoke(Pair.of(window.width, window.height));
    }

    @Override
    public boolean isFullscreen(){
     return window.fullscreen;
    }

    @Override
    public void setOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener) {
        windowSizeChangedController.register(onWindowSizeChangedListener);
    }

    @Override
    public void remOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener) {
        windowSizeChangedController.unregister(onWindowSizeChangedListener);
    }
}
