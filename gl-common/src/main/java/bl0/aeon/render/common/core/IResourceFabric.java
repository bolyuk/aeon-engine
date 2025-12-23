package bl0.aeon.render.common.core;

import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;

import java.nio.ByteBuffer;

public interface IResourceFabric {
    ShaderProgram createShaderProgram(String vertexShader, String fragmentShader, String name);

    ShaderProgram loadShaderProgramFromResourcePath(String shaderDir, String name);

    Mesh createCube(String name);

    Mesh createPlane(String name);

    Mesh createSphere(int sectorCount, int stackCount, float radius, String name);

    Texture loadTextureFromPath(String path, String name);

    Texture loadTextureFromResourcePath(String path, String name);

}
