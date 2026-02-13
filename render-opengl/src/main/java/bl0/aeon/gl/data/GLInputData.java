package bl0.aeon.gl.data;

import bl0.aeon.render.api.data.input.InputData;
import bl0.aeon.render.api.data.input.Key;
import org.joml.Vector2d;

import java.util.ArrayList;

public class GLInputData implements InputData {
    public ArrayList<Key> keys = new  ArrayList<>();

    public Vector2d mPos, mdt;

    @Override
    public boolean isKeyDown(Key key) {
        return keys.contains(key);
    }

    @Override
    public long keyDownTime(Key key) {
        return -1; //TODO
    }

    @Override
    public boolean isAnyDown() {
        return !keys.isEmpty();
    }

    @Override
    public double getMouseX() {
        return mPos.x;
    }

    @Override
    public double getMouseY() {
        return mPos.y;
    }

    @Override
    public double getMouseDX() {
        return mdt.x;
    }

    @Override
    public double getMouseDY() {
        return mdt.y;
    }
}
