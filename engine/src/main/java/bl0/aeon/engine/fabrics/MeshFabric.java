/*
 * Decompiled with CFR 0.152.
 */
package bl0.aeon.engine.fabrics;

import java.util.ArrayList;
import java.util.List;
import org.bl0.aeon.core.components.data.res.Mesh;
import org.bl0.aeon.core.graphic.mesh.VertexAttribute;
import org.joml.Vector3f;

public class MeshFabric {
    public static Mesh cube() {
        float[] vertices = new float[]{-0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f};
        ArrayList<VertexAttribute> attr = new ArrayList<VertexAttribute>();
        attr.add(new VertexAttribute(3));
        attr.add(new VertexAttribute(3));
        return new Mesh(vertices, attr);
    }

    public static Mesh plane() {
        float[] vertices = new float[]{0.5f, 0.5f, 0.0f, 0.5f, -0.5f, 0.0f, -0.5f, -0.5f, 0.0f, -0.5f, 0.5f, 0.0f};
        return new Mesh(vertices, null);
    }

    public static Mesh sphere(int sectorCount, int stackCount, float radius) {
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
                    MeshFabric.addVertex(vertexData, x1, y1, z1);
                    MeshFabric.addNormal(vertexData, x1, y1, z1);
                    MeshFabric.addVertex(vertexData, x2, y2, z2);
                    MeshFabric.addNormal(vertexData, x2, y2, z2);
                    sectorAngleNext = (float)(Math.PI * 2 * (double)(j + 1) / (double)sectorCount);
                    float x1n = r1 * (float)Math.cos(sectorAngleNext);
                    float y1n = r1 * (float)Math.sin(sectorAngleNext);
                    MeshFabric.addVertex(vertexData, x1n, y1n, z1);
                    MeshFabric.addNormal(vertexData, x1n, y1n, z1);
                }
                if (i == stackCount - 1) continue;
                sectorAngleNext = (float)(Math.PI * 2 * (double)(j + 1) / (double)sectorCount);
                float x2n = r2 * (float)Math.cos(sectorAngleNext);
                float y2n = r2 * (float)Math.sin(sectorAngleNext);
                float x1n = r1 * (float)Math.cos(sectorAngleNext);
                float y1n = r1 * (float)Math.sin(sectorAngleNext);
                MeshFabric.addVertex(vertexData, x1n, y1n, z1);
                MeshFabric.addNormal(vertexData, x1n, y1n, z1);
                MeshFabric.addVertex(vertexData, x2, y2, z2);
                MeshFabric.addNormal(vertexData, x2, y2, z2);
                MeshFabric.addVertex(vertexData, x2n, y2n, z2);
                MeshFabric.addNormal(vertexData, x2n, y2n, z2);
            }
        }
        float[] vertices = new float[vertexData.size()];
        for (int i = 0; i < vertexData.size(); ++i) {
            vertices[i] = ((Float)vertexData.get(i)).floatValue();
        }
        List<VertexAttribute> attr = List.of(new VertexAttribute(3), new VertexAttribute(3));
        return new Mesh(vertices, attr);
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
}

