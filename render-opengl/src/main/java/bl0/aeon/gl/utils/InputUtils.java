package bl0.aeon.gl.utils;

import bl0.aeon.render.api.data.input.Key;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputUtils {

    public static HashMap<Integer, List<Key>> getDefaultKeyMapping() {
        HashMap<Integer, List<Key>> m = new HashMap<>();

        for (char c = 'A'; c <= 'Z'; c++) put(m, GLFW.GLFW_KEY_A + (c - 'A'), Key.valueOf(String.valueOf(c)));
        for (int i = 0; i <= 9; i++) put(m, GLFW.GLFW_KEY_0 + i, Key.valueOf("N" + i));
        for (int i = 1; i <= 25; i++) put(m, GLFW.GLFW_KEY_F1 + i - 1, Key.valueOf("F" + i));
        for (int i = 0; i <= 9; i++) put(m, GLFW.GLFW_KEY_KP_0 + i, Key.valueOf("KP_" + i));

        put(m, GLFW.GLFW_KEY_ESCAPE, Key.ESCAPE);
        put(m, GLFW.GLFW_KEY_SPACE, Key.SPACE);
        put(m, GLFW.GLFW_KEY_ENTER, Key.ENTER);
        put(m, GLFW.GLFW_KEY_BACKSPACE, Key.BACKSPACE);
        put(m, GLFW.GLFW_KEY_TAB, Key.TAB);

        put(m, GLFW.GLFW_KEY_LEFT_SHIFT, Key.SHIFT, Key.LEFT_SHIFT);
        put(m, GLFW.GLFW_KEY_RIGHT_SHIFT, Key.SHIFT, Key.RIGHT_SHIFT);
        put(m, GLFW.GLFW_KEY_LEFT_CONTROL, Key.CTRL, Key.LEFT_CTRL);
        put(m, GLFW.GLFW_KEY_RIGHT_CONTROL, Key.CTRL, Key.RIGHT_CTRL);
        put(m, GLFW.GLFW_KEY_LEFT_ALT, Key.ALT, Key.LEFT_ALT);
        put(m, GLFW.GLFW_KEY_RIGHT_ALT, Key.ALT, Key.RIGHT_ALT);
        put(m, GLFW.GLFW_KEY_LEFT_SUPER, Key.SUPER, Key.LEFT_SUPER);
        put(m, GLFW.GLFW_KEY_RIGHT_SUPER, Key.SUPER, Key.RIGHT_SUPER);
        put(m, GLFW.GLFW_KEY_MENU, Key.MENU);

        put(m, GLFW.GLFW_KEY_UP, Key.UP);
        put(m, GLFW.GLFW_KEY_DOWN, Key.DOWN);
        put(m, GLFW.GLFW_KEY_LEFT, Key.LEFT);
        put(m, GLFW.GLFW_KEY_RIGHT, Key.RIGHT);
        put(m, GLFW.GLFW_KEY_INSERT, Key.INSERT);
        put(m, GLFW.GLFW_KEY_DELETE, Key.DELETE);
        put(m, GLFW.GLFW_KEY_HOME, Key.HOME);
        put(m, GLFW.GLFW_KEY_END, Key.END);
        put(m, GLFW.GLFW_KEY_PAGE_UP, Key.PAGE_UP);
        put(m, GLFW.GLFW_KEY_PAGE_DOWN, Key.PAGE_DOWN);

        put(m, GLFW.GLFW_KEY_CAPS_LOCK, Key.CAPS_LOCK);
        put(m, GLFW.GLFW_KEY_SCROLL_LOCK, Key.SCROLL_LOCK);
        put(m, GLFW.GLFW_KEY_NUM_LOCK, Key.NUM_LOCK);
        put(m, GLFW.GLFW_KEY_PRINT_SCREEN, Key.PRINT_SCREEN);
        put(m, GLFW.GLFW_KEY_PAUSE, Key.PAUSE);

        put(m, GLFW.GLFW_KEY_APOSTROPHE, Key.APOSTROPHE);
        put(m, GLFW.GLFW_KEY_COMMA, Key.COMMA);
        put(m, GLFW.GLFW_KEY_MINUS, Key.MINUS);
        put(m, GLFW.GLFW_KEY_PERIOD, Key.PERIOD);
        put(m, GLFW.GLFW_KEY_SLASH, Key.SLASH);
        put(m, GLFW.GLFW_KEY_SEMICOLON, Key.SEMICOLON);
        put(m, GLFW.GLFW_KEY_EQUAL, Key.EQUAL);
        put(m, GLFW.GLFW_KEY_LEFT_BRACKET, Key.LEFT_BRACKET);
        put(m, GLFW.GLFW_KEY_BACKSLASH, Key.BACKSLASH);
        put(m, GLFW.GLFW_KEY_RIGHT_BRACKET, Key.RIGHT_BRACKET);
        put(m, GLFW.GLFW_KEY_GRAVE_ACCENT, Key.GRAVE_ACCENT);
        put(m, GLFW.GLFW_KEY_WORLD_1, Key.WORLD_1);
        put(m, GLFW.GLFW_KEY_WORLD_2, Key.WORLD_2);

        put(m, GLFW.GLFW_KEY_KP_DECIMAL, Key.KP_DECIMAL);
        put(m, GLFW.GLFW_KEY_KP_DIVIDE, Key.KP_DIVIDE);
        put(m, GLFW.GLFW_KEY_KP_MULTIPLY, Key.KP_MULTIPLY);
        put(m, GLFW.GLFW_KEY_KP_SUBTRACT, Key.KP_SUBTRACT);
        put(m, GLFW.GLFW_KEY_KP_ADD, Key.KP_ADD);
        put(m, GLFW.GLFW_KEY_KP_ENTER, Key.KP_ENTER);
        put(m, GLFW.GLFW_KEY_KP_EQUAL, Key.KP_EQUAL);

        return m;
    }

    public static HashMap<Integer, Key> getDefaultMouseMapping() {
        HashMap<Integer, Key> m = new HashMap<>();
        m.put(GLFW.GLFW_MOUSE_BUTTON_LEFT, Key.MOUSE_LEFT);
        m.put(GLFW.GLFW_MOUSE_BUTTON_RIGHT, Key.MOUSE_RIGHT);
        m.put(GLFW.GLFW_MOUSE_BUTTON_MIDDLE, Key.MOUSE_MIDDLE);
        m.put(GLFW.GLFW_MOUSE_BUTTON_4, Key.MOUSE_4);
        m.put(GLFW.GLFW_MOUSE_BUTTON_5, Key.MOUSE_5);
        m.put(GLFW.GLFW_MOUSE_BUTTON_6, Key.MOUSE_6);
        m.put(GLFW.GLFW_MOUSE_BUTTON_7, Key.MOUSE_7);
        m.put(GLFW.GLFW_MOUSE_BUTTON_8, Key.MOUSE_8);
        return m;
    }

    private static void put(HashMap<Integer, List<Key>> m, int code, Key... keys) {
        List<Key> list = m.computeIfAbsent(code, c -> new ArrayList<>(2));
        for (Key k : keys) list.add(k);
    }
}
