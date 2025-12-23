package bl0.aeon.gl;

import bl0.aeon.gl.data.GLInputData;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.gl.graphic.GLTexture;
import bl0.aeon.gl.graphic.mesh.GLMesh;
import bl0.aeon.gl.graphic.uniforms.*;
import bl0.aeon.render.common.core.IResourceFabric;
import bl0.aeon.render.common.core.RenderEngine;
import bl0.aeon.render.common.core.RenderFrame;
import bl0.aeon.render.common.data.input.InputData;
import bl0.aeon.render.common.data.input.Key;
import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.resource.IDisposable;
import bl0.aeon.gl.base.CoreException;
import bl0.aeon.gl.graphic.Window;
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

public class GLEngine extends BJSBaseClass implements IDisposable, RenderEngine {

    private final ActionController<Pair<Integer, Integer>> windowSizeChangedController = new ActionController<>();

    private Window window;

    private boolean contextBound = false;

    private GLInputData lastInput = new GLInputData();

    public GLEngine(IContext ctx) {
        super(ctx);
    }

    public void setOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener) {
        windowSizeChangedController.register(onWindowSizeChangedListener);
    }

    public void remOnWindowSizeChangedListener(Action<Pair<Integer, Integer>> onWindowSizeChangedListener) {
        windowSizeChangedController.unregister(onWindowSizeChangedListener);
    }


    @Override
    public double getTime() {
        return GLFW.glfwGetTime();
    }

    @Override
    public IResourceFabric getFabric() {
        return new GLResourceFabric();
    }

    @Override
    public void bindContext() {
        if (contextBound) return;

        GLFW.glfwMakeContextCurrent(window.ID);
        GL.createCapabilities();

        GL30.glViewport(0, 0, window.width, window.height);
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
            GL11.glViewport(0, 0, w, h);
            windowSizeChangedController.invoke(Pair.of(w, h));
        });

        GLFW.glfwSwapInterval(1);

        contextBound = true;
    }

    @Override
    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    @Override
    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window.ID);
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

        lastInput = input;
        return input.isAnyDown() ? input : null;
    }

    @Override
    public void captureCursor(boolean flag) {
        GLFW.glfwSetInputMode(window.ID, GLFW.GLFW_CURSOR, flag ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR);
    }

    private boolean isPressed(int key){
        return GLFW.glfwGetKey(window.ID, key) == GLFW.GLFW_PRESS;
    }

    @Override
    public void render(RenderFrame renderContext) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        innerRender(renderContext);
    }

    private void innerRender(RenderFrame rFrame) {
        for(IRenderable obj : rFrame.getRenderables()){
            if (!(obj.getShaderProgram() instanceof GLShaderProgram program)) continue;
            if (!(obj.getMesh() instanceof GLMesh mesh)) continue;

            GLTexture texture = null;

            if(obj.getTexture() instanceof GLTexture t)
                texture = t;

            program.bind();
            mesh.bind();

            if(texture != null) {
                GL30.glActiveTexture(GL30.GL_TEXTURE0);
                texture.bind();
            }

            CameraUniforms.setUniforms(rFrame.getCamera(), program);
            MaterialUniforms.setUniforms(obj, program);
            TransformUniforms.setUniforms(obj, program);
            LightUniforms.setUniforms(obj, program);
            TextureUniforms.setUniforms(texture, program);

            mesh.draw();

            if(texture != null)
                texture.unbind();

            mesh.unbind();
            program.unbind();
        }
    }


    @Override
    public void dispose() {
        GLFW.glfwTerminate();
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
    }
}

