package bl0.aeon.gl.graphic;

import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.base.IBindable;
import bl0.aeon.render.api.base.IResource;
import bl0.aeon.render.api.resource.Framebuffer;
import bl0.aeon.render.api.resource.Mesh;
import bl0.aeon.render.api.resource.ShaderProgram;
import bl0.aeon.render.api.resource.Texture;
import org.lwjgl.opengl.GL30;

public class GLFramebuffer extends GLResource implements IBindable, Framebuffer {

    private Texture texture;
    private Mesh mesh;
    private ShaderProgram shader;

    public GLFramebuffer(String name) {
        super(GL30.glGenFramebuffers(), name);
    }

    @Override
    public void bind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, ID);
    }

    @Override
    public void unbind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void dispose() {
        GL30.glDeleteFramebuffers(ID);
    }

    @Override
    public IResource makeCopy(String name) {
        var res = new GLFramebuffer(name);

        res.setTexture(texture);
        res.setMesh(mesh);
        res.setShader(shader);

        return res;
    }

    @Override
    public void setTexture(Texture texture) {
        if(!(texture instanceof GLTexture gltexture)) {
            throw new IllegalArgumentException("Wrong texture type");
        }
        this.texture = texture;

        bind();
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D,
                gltexture.ID, 0);

        int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            throw new IllegalStateException("Framebuffer '" + name + "' incomplete: status = " + status);
        }
        unbind();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public void setShader(ShaderProgram shader) {
        this.shader = shader;
    }

    @Override
    public ShaderProgram getShader() {
        return shader;
    }
}
