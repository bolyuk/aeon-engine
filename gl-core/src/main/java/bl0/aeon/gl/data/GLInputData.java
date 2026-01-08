package bl0.aeon.gl.data;

import bl0.aeon.render.common.data.input.InputData;
import bl0.aeon.render.common.data.input.Key;

import java.util.ArrayList;

public class GLInputData implements InputData {
    public ArrayList<Key> keys = new  ArrayList<>();

    double mX, mY, mdX, mdY;

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
        return mX;
    }

    @Override
    public double getMouseY() {
        return mY;
    }

    @Override
    public double getMouseDX() {
        return mdX;
    }

    @Override
    public double getMouseDY() {
        return mdY;
    }
}
