package bl0.aeon.gl.c;

public final class Uniforms {
    public static final String TEXTURE = "uTexture0";
    public static final String USE_TEXTURE = "useTexture";
    public static final String COLOR = "color";
    public static final String MODEL = "model";
    public static final String POSITION = "position";
    public static final String DIRECTION = "direction";

    private static String getARR(String POINTER) {
        return POINTER + "["+Placeholders.INDEX+"]";
    }

    private static String getField(String ARR, String FIELD) {
        return ARR + "." + FIELD;
    }

    private Uniforms() {}

    public static final class Light {
        public static final String AMBIENT = "ambient";
        public static final String DIFFUSE = "diffuse";
        public static final String SPECULAR = "specular";

        private Light() {}

        public static final class Point {
            public static final String POINTER = "pointLight";
            private static final String ARR = Uniforms.getARR(POINTER);
            public static final String POSITION_INDEX = Uniforms.getField(ARR, Uniforms.POSITION);
            public static final String AMBIENT_INDEX = Uniforms.getField(ARR, AMBIENT);
            public static final String DIFFUSE_INDEX = Uniforms.getField(ARR, DIFFUSE);
            public static final String SPECULAR_INDEX = Uniforms.getField(ARR, Light.SPECULAR);
            public static final String LINEAR_INDEX = Uniforms.getField(ARR, "linear");
            public static final String CONSTANT_INDEX = Uniforms.getField(ARR, "constant");
            public static final String QUADRATIC_INDEX = Uniforms.getField(ARR, "quadratic");

            private Point() {}
        }

        public static final class Directional {
            public static final String POINTER = "dirLight";
            public static final String AMBIENT = Uniforms.getField(POINTER, Light.AMBIENT);
            public static final String DIFFUSE = Uniforms.getField(POINTER, Light.DIFFUSE);
            public static final String SPECULAR = Uniforms.getField(POINTER, Light.SPECULAR);
            public static final String DIRECTION = Uniforms.getField(POINTER, Uniforms.DIRECTION);

            private Directional() {}
        }
    }

    public static final class Camera {
        public static final String VIEW = "view";
        public static final String PROJECTION = "projection";
        public static final String VIEW_POS = "viewPos";

        private Camera() {}
    }
}

