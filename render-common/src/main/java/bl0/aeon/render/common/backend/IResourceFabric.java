package bl0.aeon.render.common.backend;

import bl0.aeon.render.common.resource.Font;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;

import java.nio.ByteBuffer;

public interface IResourceFabric {
    ShaderProgram createShaderProgram(String vertexShader, String fragmentShader, String name);
    ShaderProgram loadShaderProgramFromResourcePath(String shaderDir, String name);

    Font loadFontFromResourcePath(String path, String name, int size);
    Mesh createCube(String name);
    Mesh createPlane(String name);
    Mesh createSphereSmooth(int sectorCount, int stackCount, float radius, String name);
    public Mesh createSphereLowPoly(int sectorCount, int stackCount, float radius, String name);
    Texture createTextureFromRGBABuffer(ByteBuffer buffer, int width, int height, String name);
    Texture loadTextureFromPath(String path, String name);
    Texture loadTextureFromResourcePath(String path, String name);

    Mesh createUITextMesh(String name);

    Mesh createUIQuadMesh(String name);
}
