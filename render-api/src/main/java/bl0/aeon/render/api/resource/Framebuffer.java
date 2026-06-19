package bl0.aeon.render.api.resource;

import bl0.aeon.render.api.base.IResource;

public interface Framebuffer extends IResource {
    void setTexture(Texture texture);
    Texture getTexture();

    void setMesh(Mesh mesh);
    Mesh getMesh();

    void setShader(ShaderProgram shader);
    ShaderProgram getShader();
}
