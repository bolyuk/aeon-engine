package bl0.aeon.gl.backend;

import bl0.aeon.gl.GLState;
import bl0.aeon.gl.base.CoreException;
import bl0.aeon.gl.base.GLBaseClass;
import bl0.aeon.gl.data.GLInputData;
import bl0.aeon.gl.graphic.Window;
import bl0.aeon.gl.graphic.utils.ViewportUtil;
import bl0.aeon.render.common.backend.IWindowManager;
import bl0.aeon.render.common.data.input.InputData;
import bl0.aeon.render.common.data.input.Key;
import bl0.bjs.common.base.BJSBaseClass;
import bl0.bjs.common.base.IContext;
import bl0.bjs.common.core.event.action.Action;
import bl0.bjs.common.core.event.action.ActionController;
import bl0.bjs.common.core.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class GLWindowManager extends GLBaseClass implements IWindowManager {
    private final ActionController<Pair<Integer, Integer>> windowSizeChangedController = new ActionController<>();
    private Window window;

    public GLWindowManager(GLState state, IContext ctx) {
        super(state, ctx);
    }

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

        int[] fbw = new int[1];
        int[] fbh = new int[1];

        // because on KDE FramebufferSize != WindowSize
        GLFW.glfwGetFramebufferSize(window.ID, fbw, fbh);
        window.width = fbw[0];
        window.height = fbh[0];

        glState.isWindowCreated = true;
        glState.windowID = this.window.ID;
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

        GLFW.glfwSwapInterval(1);
    }

    @Override
    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    @Override
    public InputData pollInputData() {
        GLInputData input = new GLInputData();

        /// >.<
        if(isPressed(GLFW.GLFW_KEY_W))
            input.keys.add(Key.W);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_A))
            input.keys.add(Key.A);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_S))
            input.keys.add(Key.S);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_D))
            input.keys.add(Key.D);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_ESCAPE))
            input.keys.add(Key.ESCAPE);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_SPACE))
            input.keys.add(Key.SPACE);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_BACKSPACE))
            input.keys.add(Key.BACKSPACE);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_ENTER))
            input.keys.add(Key.ENTER);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_Q))
            input.keys.add(Key.Q);
        /// >.<
        if(isPressed(GLFW.GLFW_KEY_E))
            input.keys.add(Key.E);

        return input.isAnyDown() ? input : null;
    }

    private boolean isPressed(int key){
        return GLFW.glfwGetKey(window.ID, key) == GLFW.GLFW_PRESS;
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
    public void setOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener) {
        windowSizeChangedController.register(onWindowSizeChangedListener);
    }

    @Override
    public void remOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener) {
        windowSizeChangedController.unregister(onWindowSizeChangedListener);
    }
}
