package bl0.aeon.gl.backend;

import bl0.aeon.gl.GLState;
import bl0.aeon.gl.base.GLBaseClass;
import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLBitmapFont;
import bl0.aeon.gl.graphic.GLFramebuffer;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.gl.graphic.GLTexture;
import bl0.aeon.gl.graphic.mesh.GLMesh;
import bl0.aeon.gl.graphic.mesh.ui.UIQuadMesh;
import bl0.aeon.gl.graphic.mesh.ui.UITextMesh;
import bl0.aeon.gl.graphic.uniforms.*;
import bl0.aeon.render.api.backend.IRenderManager;
import bl0.aeon.render.api.data.render.Camera;
import bl0.aeon.render.api.data.render.IRenderable;
import bl0.aeon.render.api.data.render.RenderFrame;
import bl0.aeon.render.api.data.render.scene.IInstancedRenderable;
import bl0.aeon.render.api.data.render.scene.ISceneRenderable;
import bl0.aeon.render.api.data.render.scene.ISingleRenderable;
import bl0.aeon.render.api.data.render.ui.ITextRenderable;
import bl0.aeon.render.api.data.render.ui.IUIRenderable;
import bl0.aeon.render.api.resource.Framebuffer;
import bl0.bjs.common.base.IContext;
import bl0.aeon.render.api.data.render.scene.IBoundedRenderable;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class GLRenderManager extends GLBaseClass implements IRenderManager {


    private static final boolean GL_CHECK = Boolean.getBoolean("aeon.glcheck");
    private static final boolean GPU_TIME = Boolean.getBoolean("aeon.gputime");

    public GLRenderManager(GLState state, IContext ctx) {
        super(state, ctx);
    }

    @Override
    public double getTime() {
        return GLFW.glfwGetTime();
    }

    @Override
    public void swapBuffers() {
        GLFW.glfwSwapBuffers(glState.windowID);
    }

    @Override
    public void render(RenderFrame frame) {
        try {
            if(!(frame.framebuffer() instanceof GLFramebuffer glFramebuffer))
                throw new RuntimeException("framebuffer is not a GLFramebuffer");

            GL30.glEnable(GL30.GL_DEPTH_TEST);

            glFramebuffer.bind();
            GL30.glClearColor(0.1f,0.1f,0.1f,1f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            innerRender(frame);

            glFramebuffer.unbind();
            GL30.glClearColor(1f,1f,1f,1f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if(!(frame.framebuffer().getShader() instanceof GLShaderProgram glShaderProgram))
                throw new RuntimeException("Shader is not a GLShaderProgram");

            glShaderProgram.bind();

            if(!(frame.framebuffer().getMesh() instanceof GLMesh glMesh))
                throw new RuntimeException("Mesh is not a GLMesh");

            glMesh.bind();

            GL30.glDisable(GL30.GL_DEPTH_TEST);

            if(!(frame.framebuffer().getTexture() instanceof GLTexture glTexture))
                throw new RuntimeException("Framebuffer Texture is not a GLTexture");

            glTexture.bind();
            glMesh.draw();

            GL30.glEnable(GL30.GL_DEPTH_TEST);

            if (DUMP_PATH != null && ++frameCounter % DUMP_FRAME == 0) dumpScreen(frame.framebufferWidth(), frame.framebufferHeight());
            if (GL_CHECK) {
                int err = GL11.glGetError();
                if (err != 0 && glErrorReports++ < 20)
                    System.err.println("[GL] error 0x" + Integer.toHexString(err) + " after frame");
            }

        } catch(Throwable t) {
            ctx.generateLogger(this.getClass()).err(t);
        }
    }

    private void innerRender(RenderFrame rFrame) {
        List<IUIRenderable> ui = new ArrayList<>();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Frustum for this frame: renderables that declare bounds are skipped when fully outside it.
        FrustumIntersection frustum = new FrustumIntersection(
                new Matrix4f(rFrame.camera().getProjectionMatrix()).mul(rFrame.camera().getViewMatrix()));
        resetSceneState();
        if (GPU_TIME) gpuTimerBegin();

        for(IRenderable raw : rFrame.renderables()){
            if(raw instanceof ISceneRenderable obj) {
                if (obj instanceof IBoundedRenderable b) {
                    if (b.isCulled()) continue;
                    Vector3f min = b.getBoundsMin(), max = b.getBoundsMax();
                    if (!frustum.testAab(min.x, min.y, min.z, max.x, max.y, max.z)) continue;
                }
                drawSceneRenderable(obj, rFrame.camera());
            } else if(raw instanceof IUIRenderable obj)
                ui.add(obj);
        }
        releaseSceneState();
        if (GPU_TIME) gpuTimerEnd();

        if(!ui.isEmpty()) {
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            for (IUIRenderable uiElement : ui)
                drawUIElement(uiElement, rFrame.framebufferWidth(), rFrame.framebufferHeight());

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);
    }

    private void drawUIElement(IUIRenderable uiElement, int w, int h) {
        if (uiElement.getShaderProgram() instanceof GLShaderProgram program &&
        uiElement.getMesh() instanceof UIQuadMesh mesh) {

            program.bind();
            mesh.bind();

            program.setUniform("projection", new Matrix4f().ortho2D(0, w, h, 0));
            ColorUniforms.setUniforms(uiElement, program);
            UIRotationUniforms.setUniforms(uiElement, program);
            mesh.draw(uiElement.getPosition(), uiElement.getSize());

            mesh.unbind();
            program.unbind();

        } else if(uiElement instanceof ITextRenderable itr) {
            if (!(itr.getFont() instanceof GLBitmapFont font)) return;
            if (!(itr.getTextMesh() instanceof UITextMesh textMesh)) return;
            if (!(itr.getTextShader() instanceof GLShaderProgram textShader)) return;

            textShader.bind();
            textMesh.bind();

            textShader.setUniform("projection", new Matrix4f().ortho2D(0, w, h, 0));
            textShader.setUniform("uTexture0", 0);
            UIRotationUniforms.setUniforms(uiElement, textShader);
            textShader.setUniform(Uniforms.COLOR, itr.getTextColor());

            Vector2f pos = uiElement.getPosition();
            font.draw(itr.getText(), pos.x, pos.y, textMesh);

            textMesh.unbind();
            textShader.unbind();
        }
    }

    private void drawSceneRenderable(ISceneRenderable obj, Camera camera) {
        if (!(obj.getShaderProgram() instanceof GLShaderProgram program)) return;
        if (!(obj.getMesh() instanceof GLMesh mesh)) return;

        GLTexture texture = null;

        if(obj.getTexture() instanceof GLTexture t)
            texture = t;

        if (program != boundProgram) {
            program.bind();
            boundProgram = program;
            uniformsValid = false;
        }
        if (mesh.vaoID != boundVao) {                 // pooled meshes share their arena's vertex array
            mesh.bind();
            boundVao = mesh.vaoID;
        }

        if (texture != boundTexture) {
            GL30.glActiveTexture(GL30.GL_TEXTURE0);
            if (texture != null) texture.bind(); else GL30.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            boundTexture = texture;
            uniformsValid = false;
        }

        Vector4f color = obj.getColor();
        boolean sameState = uniformsValid
                && obj.getDirectionalLight() == lastDirectional
                && obj.getPointLights() == lastPointLights
                && (color == null ? lastColor == null : (lastColor != null && color.equals(lastColor)));
        if (!sameState) {
            CameraUniforms.setUniforms(camera, program);
            LightUniforms.setUniforms(obj, program);
            TextureUniforms.setUniforms(texture, program);
            ColorUniforms.setUniforms(obj, program);

            lastDirectional = obj.getDirectionalLight();
            lastPointLights = obj.getPointLights();
            if (color == null) lastColor = null;
            else if (lastColor == null) lastColor = new Vector4f(color);
            else lastColor.set(color);
            uniformsValid = true;
        }

        if(!obj.isDepthTestEnabled())
            GL30.glDisable(GL30.GL_DEPTH_TEST);

        if(obj instanceof ISingleRenderable isr) {
            TransformUniforms.setUniforms(isr, program);
            mesh.draw();
            statDraws++;
            statVertices += mesh.vertexCount;
        } else if(obj instanceof IInstancedRenderable iir) {
            mesh.enableMatrixInstancing(3, iir.getInstanceCount());
            boundVao = -1;                            // enabling instancing binds and unbinds vertex arrays
            mesh.uploadInstanceMatrices(iir.getInstanceMatrices(), iir.getInstanceCount());
            mesh.drawInstanced(iir.getInstanceCount());
        }

        if(!obj.isDepthTestEnabled())
            GL30.glEnable(GL30.GL_DEPTH_TEST);
    }

    private static final String DUMP_PATH = System.getProperty("aeon.dump");
    private static final int DUMP_FRAME = Integer.getInteger("aeon.dumpFrame", 600);

    private static void dumpScreen(int w, int h) {
        try {
            java.nio.ByteBuffer b = org.lwjgl.BufferUtils.createByteBuffer(w * h * 4);
            GL11.glReadPixels(0, 0, w, h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, b);
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < h; y++)
                for (int x = 0; x < w; x++) {
                    int i = ((h - 1 - y) * w + x) * 4;
                    img.setRGB(x, y, ((b.get(i) & 255) << 16) | ((b.get(i + 1) & 255) << 8) | (b.get(i + 2) & 255));
                }
            javax.imageio.ImageIO.write(img, "png", new java.io.File(DUMP_PATH));
            System.err.println("[GL] frame dumped to " + DUMP_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int QUERIES = 4;
    private int[] gpuQueries;
    private int gpuQueryFrame;
    private long statDraws, statVertices, statFrames, statGpuNanos, statGpuSamples, statLast = System.nanoTime();

    private void gpuTimerBegin() {
        if (gpuQueries == null) {
            gpuQueries = new int[QUERIES];
            org.lwjgl.opengl.GL15.glGenQueries(gpuQueries);
        }
        int q = gpuQueries[gpuQueryFrame % QUERIES];
        if (gpuQueryFrame >= QUERIES) {                         // the query issued QUERIES frames ago is done by now
            statGpuNanos += org.lwjgl.opengl.GL33.glGetQueryObjecti64(q, org.lwjgl.opengl.GL15.GL_QUERY_RESULT);
            statGpuSamples++;
        }
        org.lwjgl.opengl.GL15.glBeginQuery(org.lwjgl.opengl.GL33.GL_TIME_ELAPSED, q);
    }

    private void gpuTimerEnd() {
        org.lwjgl.opengl.GL15.glEndQuery(org.lwjgl.opengl.GL33.GL_TIME_ELAPSED);
        gpuQueryFrame++;
        statFrames++;
        long now = System.nanoTime();
        if (now - statLast >= 2_000_000_000L) {
            System.out.printf("[GPU] scene pass %.2f ms | %d draws, %.2fM vertices per frame%n",
                    statGpuSamples == 0 ? 0 : statGpuNanos / 1e6 / statGpuSamples, statDraws / Math.max(1, statFrames),
                    statVertices / 1e6 / Math.max(1, statFrames));
            statDraws = statVertices = statFrames = statGpuNanos = statGpuSamples = 0;
            statLast = now;
        }
    }

    private int glErrorReports, frameCounter;

    private GLShaderProgram boundProgram;
    private GLTexture boundTexture;
    private boolean uniformsValid;
    private Object lastDirectional;
    private Object lastPointLights;
    private Vector4f lastColor;

    private int boundVao = -1;

    private void resetSceneState() {
        boundVao = -1;
        boundProgram = null;
        boundTexture = null;
        uniformsValid = false;
        lastDirectional = null;
        lastPointLights = null;
        lastColor = null;
    }

    private void releaseSceneState() {
        if (boundTexture != null) GL30.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindVertexArray(0);
        if (boundProgram != null) boundProgram.unbind();
        resetSceneState();
    }
}

