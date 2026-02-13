package bl0.aeon.render.api.data.input;

public interface InputData {
    boolean isKeyDown(Key key);

    long keyDownTime(Key key);
    boolean isAnyDown();

    double getMouseX();
    double getMouseY();

    double getMouseDX();
    double getMouseDY();
}
