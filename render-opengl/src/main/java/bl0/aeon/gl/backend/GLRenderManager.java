package bl0.aeon.gl.backend;

import bl0.aeon.gl.GLState;
import bl0.aeon.gl.base.GLBaseClass;
import bl0.aeon.gl.c.Uniforms;
import bl0.aeon.gl.graphic.GLBitmapFont;
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
import bl0.bjs.common.base.IContext;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class GLRenderManager extends GLBaseClass implements IRenderManager {

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
    public void render(RenderFrame renderContext) {
        try {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL30.glClearColor(0,0,0,1f);
            innerRender(renderContext);
        } catch(Throwable t) {
            ctx.generateLogger(this.getClass()).err(t);
        }
    }

    private void innerRender(RenderFrame rFrame) {
        List<IUIRenderable> ui = new ArrayList<>();

        for(IRenderable raw : rFrame.renderables()){
            if(raw instanceof ISceneRenderable obj)
                drawSceneRenderable(obj, rFrame.camera());
            else if(raw instanceof IUIRenderable obj)
                ui.add(obj);
        }

        GL11.glDisable(GL11.GL_CULL_FACE);

        for(IUIRenderable uiElement : ui)
            drawUIElement(uiElement, rFrame.framebufferWidth(), rFrame.framebufferHeight());

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    private void drawUIElement(IUIRenderable uiElement, int w, int h) {
        if (uiElement.getShaderProgram() instanceof GLShaderProgram program &&
        uiElement.getMesh() instanceof UIQuadMesh mesh) {

            program.bind();
            mesh.bind();

            program.setUniform("projection", new Matrix4f().ortho2D(0, w, h, 0));
            ColorUniforms.setUniforms(uiElement, program);

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

        program.bind();
        mesh.bind();

        if(texture != null) {
            GL30.glActiveTexture(GL30.GL_TEXTURE0);
            texture.bind();
        }

        CameraUniforms.setUniforms(camera, program);
        LightUniforms.setUniforms(obj, program);
        TextureUniforms.setUniforms(texture, program);
        ColorUniforms.setUniforms(obj, program);

        if(!obj.isDepthTestEnabled())
            GL30.glDisable(GL30.GL_DEPTH_TEST);

        if(obj instanceof ISingleRenderable isr) {
            TransformUniforms.setUniforms(isr, program);
            mesh.draw();
        } else if(obj instanceof IInstancedRenderable iir) {
            mesh.enableMatrixInstancing(3, iir.getInstanceCount());
            mesh.uploadInstanceMatrices(iir.getInstanceMatrices(), iir.getInstanceCount());
            mesh.drawInstanced(iir.getInstanceCount());
        }

        if(!obj.isDepthTestEnabled())
            GL30.glEnable(GL30.GL_DEPTH_TEST);

        if(texture != null)
            texture.unbind();

        mesh.unbind();
        program.unbind();
    }
}

