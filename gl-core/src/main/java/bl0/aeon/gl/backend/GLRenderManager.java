package bl0.aeon.gl.backend;

import bl0.aeon.gl.GLState;
import bl0.aeon.gl.base.GLBaseClass;
import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.gl.graphic.GLTexture;
import bl0.aeon.gl.graphic.mesh.GLMesh;
import bl0.aeon.gl.graphic.uniforms.*;
import bl0.aeon.render.common.backend.IRenderManager;
import bl0.aeon.render.common.core.RenderFrame;
import bl0.aeon.render.common.data.render.IInstancedRenderable;
import bl0.aeon.render.common.data.render.IRenderable;
import bl0.aeon.render.common.data.render.ISingleRenderable;
import bl0.aeon.render.common.base.IDisposable;
import bl0.bjs.common.base.IContext;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

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
            LightUniforms.setUniforms(obj, program);
            TextureUniforms.setUniforms(texture, program);
            MaterialUniforms.setUniforms(obj, program);

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
}

