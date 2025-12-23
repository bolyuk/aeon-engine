package bl0.aeon.gl;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import bl0.aeon.gl.graphic.GLShaderProgram;
import bl0.aeon.gl.graphic.GLTexture;
import bl0.aeon.gl.graphic.mesh.GLMesh;
import bl0.aeon.gl.graphic.mesh.VertexAttribute;
import bl0.aeon.render.common.core.IResourceFabric;
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
    public Mesh createSphere(int sectorCount, int stackCount, float radius, String name) {
        ArrayList<Float> vertexData = new ArrayList<Float>();
        for (int i = 0; i < stackCount; ++i) {
            float stackAngle1 = 1.5707964f - (float)i * (float)Math.PI / (float)stackCount;
            float stackAngle2 = 1.5707964f - (float)(i + 1) * (float)Math.PI / (float)stackCount;
            float z1 = radius * (float)Math.sin(stackAngle1);
            float z2 = radius * (float)Math.sin(stackAngle2);
            float r1 = radius * (float)Math.cos(stackAngle1);
            float r2 = radius * (float)Math.cos(stackAngle2);
            for (int j = 0; j <= sectorCount; ++j) {
                float sectorAngleNext;
                float sectorAngle = (float)(Math.PI * 2 * (double)j / (double)sectorCount);
                float x1 = r1 * (float)Math.cos(sectorAngle);
                float y1 = r1 * (float)Math.sin(sectorAngle);
                float x2 = r2 * (float)Math.cos(sectorAngle);
                float y2 = r2 * (float)Math.sin(sectorAngle);
                if (i != 0) {
                    GLResourceFabric.addVertex(vertexData, x1, y1, z1);
                    GLResourceFabric.addNormal(vertexData, x1, y1, z1);
                    GLResourceFabric.addVertex(vertexData, x2, y2, z2);
                    GLResourceFabric.addNormal(vertexData, x2, y2, z2);
                    sectorAngleNext = (float)(Math.PI * 2 * (double)(j + 1) / (double)sectorCount);
                    float x1n = r1 * (float)Math.cos(sectorAngleNext);
                    float y1n = r1 * (float)Math.sin(sectorAngleNext);
                    GLResourceFabric.addVertex(vertexData, x1n, y1n, z1);
                    GLResourceFabric.addNormal(vertexData, x1n, y1n, z1);
                }
                if (i == stackCount - 1) continue;
                sectorAngleNext = (float)(Math.PI * 2 * (double)(j + 1) / (double)sectorCount);
                float x2n = r2 * (float)Math.cos(sectorAngleNext);
                float y2n = r2 * (float)Math.sin(sectorAngleNext);
                float x1n = r1 * (float)Math.cos(sectorAngleNext);
                float y1n = r1 * (float)Math.sin(sectorAngleNext);
                GLResourceFabric.addVertex(vertexData, x1n, y1n, z1);
                GLResourceFabric.addNormal(vertexData, x1n, y1n, z1);
                GLResourceFabric.addVertex(vertexData, x2, y2, z2);
                GLResourceFabric.addNormal(vertexData, x2, y2, z2);
                GLResourceFabric.addVertex(vertexData, x2n, y2n, z2);
                GLResourceFabric.addNormal(vertexData, x2n, y2n, z2);
            }
        }
        float[] vertices = new float[vertexData.size()];
        for (int i = 0; i < vertexData.size(); ++i) {
            vertices[i] = ((Float)vertexData.get(i)).floatValue();
        }
        List<VertexAttribute> attr = List.of(new VertexAttribute(3), new VertexAttribute(3), new VertexAttribute(2));
        return new GLMesh(vertices, attr, name);
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
}

