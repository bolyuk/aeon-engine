package bl0.aeon.gl.backend;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.gl.graphic.GLTexture;
import bl0.aeon.gl.graphic.mesh.GLMesh;
import bl0.aeon.gl.graphic.mesh.VertexAttribute;
import bl0.aeon.render.common.backend.IResourceFabric;
import bl0.aeon.render.common.resource.Mesh;
import bl0.aeon.render.common.resource.ShaderProgram;
import bl0.aeon.render.common.resource.Texture;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class GLResourceFabric implements IResourceFabric {

    @Override
    public ShaderProgram createShaderProgram(String vertexShader, String fragmentShader, String name) {
        return new GLShaderProgram(vertexShader, fragmentShader, name);
    }

    @Override
    public ShaderProgram loadShaderProgramFromResourcePath(String shaderDir, String name) {
        String vertex = readTextResource(shaderDir+"/vertex.shader");
        String fragment = readTextResource(shaderDir+"/fragment.shader");
        return new GLShaderProgram(vertex, fragment, name);
    }

    @Override
    public Mesh createCube(String name) {
        float[] vertices = {
                // pos               // normal         // uv
                // front
                -0.5f,-0.5f, 0.5f,   0,0,1,            0,0,
                0.5f,-0.5f, 0.5f,   0,0,1,            1,0,
                0.5f, 0.5f, 0.5f,   0,0,1,            1,1,
                0.5f, 0.5f, 0.5f,   0,0,1,            1,1,
                -0.5f, 0.5f, 0.5f,   0,0,1,            0,1,
                -0.5f,-0.5f, 0.5f,   0,0,1,            0,0,

                // back
                -0.5f,-0.5f,-0.5f,   0,0,-1,           1,0,
                -0.5f, 0.5f,-0.5f,   0,0,-1,           1,1,
                0.5f, 0.5f,-0.5f,   0,0,-1,           0,1,
                0.5f, 0.5f,-0.5f,   0,0,-1,           0,1,
                0.5f,-0.5f,-0.5f,   0,0,-1,           0,0,
                -0.5f,-0.5f,-0.5f,   0,0,-1,           1,0,

                // left
                -0.5f, 0.5f, 0.5f,  -1,0,0,            1,1,
                -0.5f, 0.5f,-0.5f,  -1,0,0,            0,1,
                -0.5f,-0.5f,-0.5f,  -1,0,0,            0,0,
                -0.5f,-0.5f,-0.5f,  -1,0,0,            0,0,
                -0.5f,-0.5f, 0.5f,  -1,0,0,            1,0,
                -0.5f, 0.5f, 0.5f,  -1,0,0,            1,1,

                // right
                0.5f, 0.5f, 0.5f,   1,0,0,            0,1,
                0.5f,-0.5f,-0.5f,   1,0,0,            1,0,
                0.5f, 0.5f,-0.5f,   1,0,0,            1,1,
                0.5f,-0.5f,-0.5f,   1,0,0,            1,0,
                0.5f, 0.5f, 0.5f,   1,0,0,            0,1,
                0.5f,-0.5f, 0.5f,   1,0,0,            0,0,

                // bottom
                -0.5f,-0.5f,-0.5f,   0,-1,0,           0,1,
                0.5f,-0.5f,-0.5f,   0,-1,0,           1,1,
                0.5f,-0.5f, 0.5f,   0,-1,0,           1,0,
                0.5f,-0.5f, 0.5f,   0,-1,0,           1,0,
                -0.5f,-0.5f, 0.5f,   0,-1,0,           0,0,
                -0.5f,-0.5f,-0.5f,   0,-1,0,           0,1,

                // top
                -0.5f, 0.5f,-0.5f,   0,1,0,            0,1,
                -0.5f, 0.5f, 0.5f,   0,1,0,            0,0,
                0.5f, 0.5f, 0.5f,   0,1,0,            1,0,
                0.5f, 0.5f, 0.5f,   0,1,0,            1,0,
                0.5f, 0.5f,-0.5f,   0,1,0,            1,1,
                -0.5f, 0.5f,-0.5f,   0,1,0,            0,1
        };

        List<VertexAttribute> attr = List.of(new VertexAttribute(3), new VertexAttribute(3), new VertexAttribute(2));
        return new  GLMesh(vertices, attr, name);
    }

    @Override
    public Mesh createPlane(String name) {
        float[] v = {
                // pos            // normal      // uv
                -0.5f, 0.0f,-0.5f,  0,1,0,        0,0,
                0.5f, 0.0f,-0.5f,  0,1,0,        1,0,
                0.5f, 0.0f, 0.5f,  0,1,0,        1,1,

                0.5f, 0.0f, 0.5f,  0,1,0,        1,1,
                -0.5f, 0.0f, 0.5f,  0,1,0,        0,1,
                -0.5f, 0.0f,-0.5f,  0,1,0,        0,0
        };

        List<VertexAttribute> attr = List.of(new VertexAttribute(3), new VertexAttribute(3), new VertexAttribute(2));
        return new GLMesh(v, attr, name);
    }

    @Override
    public Texture createTextureFromRGBABuffer(ByteBuffer buffer, int width, int height, String name) {
        return new GLTexture(name, width, height, buffer);
    }

    @Override
    public Texture loadTextureFromPath(String path, String name) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);

            ByteBuffer image = STBImage.stbi_load(
                    path,
                    w, h, channels,
                    4
            );

            if (image == null) {
                throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
            }

            var result = new GLTexture(name, w.get(0), h.get(0), image);
            STBImage.stbi_image_free(image);
            return result;
        }
    }

    @Override
    public Texture loadTextureFromResourcePath(String path, String name) {
        ByteBuffer fileData = resourceToByteBuffer(path);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer pixels = STBImage.stbi_load_from_memory(fileData, w, h, c, 4);
            if (pixels == null) {
                throw new RuntimeException("STB failed: " + STBImage.stbi_failure_reason());
            }

            GLTexture tex = new GLTexture(name, w.get(0), h.get(0), pixels);

            STBImage.stbi_image_free(pixels);
            return tex;
        }
    }

    private static void addVertex(List<Float> list, float x, float y, float z) {
        list.add(Float.valueOf(x));
        list.add(Float.valueOf(y));
        list.add(Float.valueOf(z));
    }

    private static void addNormal(List<Float> list, float x, float y, float z) {
        Vector3f n = new Vector3f(x, y, z).normalize();
        list.add(Float.valueOf(n.x));
        list.add(Float.valueOf(n.y));
        list.add(Float.valueOf(n.z));
    }

    private static ByteBuffer resourceToByteBuffer(String path) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new IllegalArgumentException("Resource not found: " + path);
            byte[] bytes = in.readAllBytes();
            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes).flip();
            return buffer;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + path, e);
        }
    }

    private static String readTextResource(String path) {
        try (var in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new IllegalArgumentException("Resource not found: " + path);
            return new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + path, e);
        }
    }

    public Mesh createSphereSmooth(int sectorCount, int stackCount, float radius, String name) {
        ArrayList<Float> vertexData = new ArrayList<>();

        for (int i = 0; i < stackCount; ++i) {
            float stackAngle1 = 1.5707964f - (float) i * (float) Math.PI / (float) stackCount;
            float stackAngle2 = 1.5707964f - (float) (i + 1) * (float) Math.PI / (float) stackCount;

            float z1 = radius * (float) Math.sin(stackAngle1);
            float z2 = radius * (float) Math.sin(stackAngle2);
            float r1 = radius * (float) Math.cos(stackAngle1);
            float r2 = radius * (float) Math.cos(stackAngle2);

            for (int j = 0; j <= sectorCount; ++j) {
                float sectorAngle = (float) (Math.PI * 2.0 * (double) j / (double) sectorCount);
                float sectorAngleNext = (float) (Math.PI * 2.0 * (double) (j + 1) / (double) sectorCount);

                float x1 = r1 * (float) Math.cos(sectorAngle);
                float y1 = r1 * (float) Math.sin(sectorAngle);
                float x2 = r2 * (float) Math.cos(sectorAngle);
                float y2 = r2 * (float) Math.sin(sectorAngle);

                float x1n = r1 * (float) Math.cos(sectorAngleNext);
                float y1n = r1 * (float) Math.sin(sectorAngleNext);
                float x2n = r2 * (float) Math.cos(sectorAngleNext);
                float y2n = r2 * (float) Math.sin(sectorAngleNext);

                // upper triangle (skip top cap)
                if (i != 0) {
                    addVertexNormalSmooth(vertexData, x1,  y1,  z1, radius);
                    addVertexNormalSmooth(vertexData, x2,  y2,  z2, radius);
                    addVertexNormalSmooth(vertexData, x1n, y1n, z1, radius);
                }

                // lower triangle (skip bottom cap)
                if (i != stackCount - 1) {
                    addVertexNormalSmooth(vertexData, x1n, y1n, z1, radius);
                    addVertexNormalSmooth(vertexData, x2,  y2,  z2, radius);
                    addVertexNormalSmooth(vertexData, x2n, y2n, z2, radius);
                }
            }
        }

        float[] vertices = new float[vertexData.size()];
        for (int i = 0; i < vertexData.size(); ++i) vertices[i] = vertexData.get(i);

        List<VertexAttribute> attr = List.of(new VertexAttribute(3), new VertexAttribute(3));
        return new GLMesh(vertices, attr, name);
    }

    public Mesh createSphereLowPoly(int sectorCount, int stackCount, float radius, String name) {
        ArrayList<Float> vertexData = new ArrayList<>();

        for (int i = 0; i < stackCount; ++i) {
            float stackAngle1 = 1.5707964f - (float) i * (float) Math.PI / (float) stackCount;
            float stackAngle2 = 1.5707964f - (float) (i + 1) * (float) Math.PI / (float) stackCount;

            float z1 = radius * (float) Math.sin(stackAngle1);
            float z2 = radius * (float) Math.sin(stackAngle2);
            float r1 = radius * (float) Math.cos(stackAngle1);
            float r2 = radius * (float) Math.cos(stackAngle2);

            float v1 = (float) i / (float) stackCount;
            float v2 = (float) (i + 1) / (float) stackCount;

            for (int j = 0; j <= sectorCount; ++j) {
                float sectorAngle = (float) (Math.PI * 2.0 * (double) j / (double) sectorCount);
                float sectorAngleNext = (float) (Math.PI * 2.0 * (double) (j + 1) / (double) sectorCount);

                float x1 = r1 * (float) Math.cos(sectorAngle);
                float y1 = r1 * (float) Math.sin(sectorAngle);
                float x2 = r2 * (float) Math.cos(sectorAngle);
                float y2 = r2 * (float) Math.sin(sectorAngle);

                float x1n = r1 * (float) Math.cos(sectorAngleNext);
                float y1n = r1 * (float) Math.sin(sectorAngleNext);
                float x2n = r2 * (float) Math.cos(sectorAngleNext);
                float y2n = r2 * (float) Math.sin(sectorAngleNext);

                float u  = (float) j / (float) sectorCount;
                float uN = (float) (j + 1) / (float) sectorCount;

                // upper triangle (skip top cap)
                if (i != 0) {
                    addTriFlatPNUV(vertexData,
                            x1,  y1,  z1,  u,  v1,
                            x2,  y2,  z2,  u,  v2,
                            x1n, y1n, z1,  uN, v1
                    );
                }

                // lower triangle (skip bottom cap)
                if (i != stackCount - 1) {
                    addTriFlatPNUV(vertexData,
                            x1n, y1n, z1,  uN, v1,
                            x2,  y2,  z2,  u,  v2,
                            x2n, y2n, z2,  uN, v2
                    );
                }
            }
        }

        float[] vertices = new float[vertexData.size()];
        for (int i = 0; i < vertexData.size(); ++i) vertices[i] = vertexData.get(i);

        List<VertexAttribute> attr = List.of(
                new VertexAttribute(3), // position
                new VertexAttribute(3), // normal
                new VertexAttribute(2)  // uv
        );
        return new GLMesh(vertices, attr, name);
    }

    private static void addTriFlatPNUV(
            ArrayList<Float> out,
            float ax, float ay, float az, float au, float av,
            float bx, float by, float bz, float bu, float bv,
            float cx, float cy, float cz, float cu, float cv
    ) {
        // face normal = normalize((b-a) x (c-a))
        float abx = bx - ax, aby = by - ay, abz = bz - az;
        float acx = cx - ax, acy = cy - ay, acz = cz - az;

        float nx = aby * acz - abz * acy;
        float ny = abz * acx - abx * acz;
        float nz = abx * acy - aby * acx;

        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len != 0f) { nx /= len; ny /= len; nz /= len; }

        addVertexPNUV(out, ax, ay, az, nx, ny, nz, au, av);
        addVertexPNUV(out, bx, by, bz, nx, ny, nz, bu, bv);
        addVertexPNUV(out, cx, cy, cz, nx, ny, nz, cu, cv);
    }

    private static void addVertexPNUV(
            ArrayList<Float> out,
            float x, float y, float z,
            float nx, float ny, float nz,
            float u, float v
    ) {
        out.add(x);  out.add(y);  out.add(z);
        out.add(nx); out.add(ny); out.add(nz);
        out.add(u);  out.add(v);
    }

    private static void addVertexNormalSmooth(ArrayList<Float> out,
                                              float x, float y, float z, float radius) {
        GLResourceFabric.addVertex(out, x, y, z);
        float inv = 1.0f / radius;
        GLResourceFabric.addNormal(out, x * inv, y * inv, z * inv);
    }
}

