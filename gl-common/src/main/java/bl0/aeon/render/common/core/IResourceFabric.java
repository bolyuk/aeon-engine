package bl0.aeon.render.common.core;

import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;

public interface IResourceFabric {
    ShaderProgram createShaderProgram(String vertexShader, String fragmentShader, String name);

    Mesh createCube(String name);

    Mesh createPlane(String name);

    Mesh createSphere(int sectorCount, int stackCount, float radius, String name);
}
