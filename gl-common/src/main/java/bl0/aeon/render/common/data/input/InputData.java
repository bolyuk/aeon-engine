package bl0.aeon.render.common.data.input;

public interface InputData {
    boolean isKeyDown(Key key);
    boolean isAnyDown();

    double getMouseX();
    double getMouseY();

    double getMouseDX();
    double getMouseDY();
}
