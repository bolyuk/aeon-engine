package bl0.aeon.gl.graphic;

import bl0.aeon.gl.base.GLResource;
import bl0.aeon.gl.graphic.mesh.GLTextMesh;
import bl0.aeon.render.common.base.IResource;
import bl0.aeon.render.common.resource.Font;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL33;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class GLBitmapFont extends GLResource implements Font {

    public HashMap<Character, GLCharacter> characters = new HashMap<>();
    public final int fontSize;

    public int ascent;

    private final FloatBuffer vb = BufferUtils.createFloatBuffer(6 * 4);

    public GLBitmapFont(String name, int fontSize) {
        super(-1, name);
        this.fontSize = fontSize;
    }

    public GLCharacter get(Character character) {
        return characters.get(character);
    }

    @Override
    public void dispose() {
        characters.forEach((character, texture) -> texture.dispose());
        characters.clear();
    }

    @Override
    public IResource makeCopy(String name) {
        GLBitmapFont font = new GLBitmapFont(name, fontSize);
        for (Map.Entry<Character, GLCharacter> entry : characters.entrySet()) {
            font.characters.put(entry.getKey(), (GLCharacter) entry.getValue().makeCopy(name+"_c_"+entry.getKey()));
        }
        return font;
    }

    @Override
    public int getFontSize() {
        return fontSize;
    }

    public void draw(String text, float x, float y, GLTextMesh mesh) {
        float baseline = y + ascent;

        for (int i = 0; i < text.length(); i++) {
            GLCharacter ch = characters.get(text.charAt(i));
            if (ch == null) continue;

            x += (ch.advance >> 6);
            if (ch.size.x == 0 || ch.size.y == 0) continue;

            float xpos = x - (ch.advance >> 6) + ch.bearing.x;
            float ypos = baseline - ch.bearing.y;

            float w = ch.size.x;
            float h = ch.size.y;

            vb.clear();
            vb.put(xpos).put(ypos + h).put(0f).put(0f);
            vb.put(xpos).put(ypos).put(0f).put(1f);
            vb.put(xpos + w).put(ypos).put(1f).put(1f);
            vb.put(xpos).put(ypos + h).put(0f).put(0f);
            vb.put(xpos + w).put(ypos).put(1f).put(1f);
            vb.put(xpos + w).put(ypos + h).put(1f).put(0f);
            vb.flip();

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            ch.bind();
            mesh.updateVertices(vb);
            mesh.draw();
        }
    }

    @Override
    public Vector2f calculateSize(String text) {
        if (text == null || text.isEmpty()) return new Vector2f(0, 0);

        float width = 0f;
        float maxTop = 0f;
        float maxBottom = 0f;

        for (int i = 0; i < text.length(); i++) {
            GLCharacter ch = characters.get(text.charAt(i));
            if (ch == null) continue;

            width += (ch.advance >> 6);

            if (ch.size.x == 0 || ch.size.y == 0) continue;

            maxTop = Math.max(maxTop, ch.bearing.y);
            maxBottom = Math.max(maxBottom, ch.size.y - ch.bearing.y);
        }

        float height = maxTop + maxBottom;

        return new Vector2f(width, height);
    }

}
