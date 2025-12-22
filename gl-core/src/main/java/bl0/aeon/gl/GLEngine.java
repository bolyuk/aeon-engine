package bl0.aeon.gl;

import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.gl.graphic.mesh.GLMesh;
import bl0.aeon.gl.graphic.uniforms.CameraUniforms;
import bl0.aeon.gl.graphic.uniforms.LightUniforms;
import bl0.aeon.gl.graphic.uniforms.MaterialUniforms;
import bl0.aeon.gl.graphic.uniforms.TransformUniforms;
import bl0.aeon.render.common.core.IResourceFabric;
import bl0.aeon.render.common.core.RenderEngine;
import bl0.aeon.render.common.core.RenderFrame;
import bl0.aeon.render.common.data.render.IRenderable;
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
        GLFW.glfwMakeContextCurrent(window.ID);
        GL.createCapabilities();

        GL11.glViewport(0, 0, window.width, 600);
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        GL11.glEnable(2929);
        GL11.glEnable(2884);
        GL11.glCullFace(1029);
        GL11.glFrontFace(2305);
        GL11.glDepthFunc(513);

        GLFW.glfwSetFramebufferSizeCallback(window.ID, (win, w, h) -> {
            GL11.glViewport(0, 0, w, h);
            windowSizeChangedController.invoke(Pair.of(w, h));
        });
    }

    @Override
    public void render(RenderFrame renderContext) {
        if(!GLFW.glfwWindowShouldClose(window.ID)) {
            GL11.glClear(16640);

            innerRender(renderContext);

            GLFW.glfwSwapBuffers(window.ID);
            GLFW.glfwPollEvents();
        }
    }

    private void innerRender(RenderFrame rFrame) {
        for(IRenderable obj : rFrame.getRenderables()){
            if (!(obj.getShaderProgram() instanceof GLShaderProgram program)) continue;
            if (!(obj.getMesh() instanceof GLMesh mesh)) continue;

            program.bind();
            mesh.bind();

            CameraUniforms.setUniforms(rFrame.getCamera(), program);
            MaterialUniforms.setUniforms(obj, program);
            TransformUniforms.setUniforms(obj, program);
            LightUniforms.setUniforms(obj, program);
            mesh.draw();

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
        GLFW.glfwInit();
        GLFW.glfwWindowHint(139266, 3);
        GLFW.glfwWindowHint(139267, 3);
        GLFW.glfwWindowHint(139272, 204801);
        this.window = new Window(width, height, title);

        try {
            this.window.ID = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);

            if (window.ID == 0L) {
                throw new CoreException("Failed to create the GLFW window");
            }
        } catch (Exception e) {
            l.err(e);
            GLFW.glfwTerminate();
        }
    }
}

