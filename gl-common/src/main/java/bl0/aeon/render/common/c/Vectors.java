package bl0.aeon.render.common.c;

import org.joml.Vector3f;

public final class Vectors {
    public static Vector3f ONE() {
        return new Vector3f(1.0f, 1.0f, 1.0f);
    }

    public static Vector3f HALF() {
        return new Vector3f(0.5f, 0.5f, 0.5f);
    }

    private Vectors() {
    }
}

