package bl0.aeon.render.api.c;

import org.joml.Vector3f;

public final class Vectors {
    public static Vector3f ONE() {
        return new Vector3f(1.0f, 1.0f, 1.0f);
    }

    public static Vector3f ZERO() {
        return new Vector3f(0.0f, 0.0f, 0.0f);
    }

    private Vectors() {}
}

