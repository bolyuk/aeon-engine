package bl0.aeon.render.api.backend;

import bl0.aeon.render.api.resource.*;

import java.nio.ByteBuffer;

public interface IResourceFabric {
    ShaderProgram createShaderProgram(String vertexShader, String fragmentShader, String name);
    ShaderProgram loadShaderProgramFromResourcePath(String shaderDir, String name);

    Framebuffer createFramebuffer(String name);

    Font loadFontFromResourcePath(String path, String name, int size);

    Mesh createQuad(String name);
    Mesh createCube(String name);
    Mesh createPlane(String name);
    Mesh createSphereSmooth(int sectorCount, int stackCount, float radius, String name);
    Mesh createSphereLowPoly(int sectorCount, int stackCount, float radius, String name);

    Texture createTextureFromRGBABuffer(ByteBuffer buffer, int width, int height, String name);
    Texture createTexture(int width, int height, String name);
    Texture loadTextureFromPath(String path, String name);
    Texture loadTextureFromResourcePath(String path, String name);

    Mesh createUITextMesh(String name);
    Mesh createUIQuadMesh(String name);
}
