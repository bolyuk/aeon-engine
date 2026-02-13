package bl0.aeon.gl.utils;

import bl0.aeon.render.api.data.input.Key;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class InputUtils {
    public static HashMap<Integer, Key> getDefaultKeyMapping(){
     HashMap<Integer, Key> keyMapping = new HashMap<>();

     keyMapping.put(GLFW.GLFW_KEY_W, Key.W);
     keyMapping.put(GLFW.GLFW_KEY_A, Key.A);
     keyMapping.put(GLFW.GLFW_KEY_S, Key.S);
     keyMapping.put(GLFW.GLFW_KEY_D, Key.D);
     keyMapping.put(GLFW.GLFW_KEY_Q, Key.Q);
     keyMapping.put(GLFW.GLFW_KEY_E, Key.E);

     keyMapping.put(GLFW.GLFW_KEY_ESCAPE, Key.ESCAPE);
     keyMapping.put(GLFW.GLFW_KEY_SPACE, Key.SPACE);
     keyMapping.put(GLFW.GLFW_KEY_BACKSPACE, Key.BACKSPACE);
     keyMapping.put(GLFW.GLFW_KEY_ENTER, Key.ENTER);

     keyMapping.put(GLFW.GLFW_KEY_F11, Key.F11);

     return keyMapping;
    }

    public static HashMap<Integer, Key> getDefaultMouseMapping(){
     HashMap<Integer, Key> keyMapping = new HashMap<>();

     keyMapping.put(GLFW.GLFW_MOUSE_BUTTON_LEFT, Key.MOUSE_LEFT);
     keyMapping.put(GLFW.GLFW_MOUSE_BUTTON_RIGHT, Key.MOUSE_RIGHT);

     return keyMapping;
    }
}