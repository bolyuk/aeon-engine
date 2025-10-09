/*
 * Decompiled with CFR 0.152.
 */
package org.bl0.aeon.core.c;

public final class Uniforms {
    public static final String TEXTURE = "texture";
    public static final String COLOR = "color";
    public static final String MODEL = "model";
    public static final String POSITION = "position";
    public static final String DIRECTION = "direction";

    private static String getARR(String POINTER) {
        return POINTER + "[$index]";
    }

    private static String getField(String ARR, String FIELD) {
        return ARR + "." + FIELD;
    }

    private Uniforms() {
    }

    public static final class Light {
        public static final String AMBIENT = "ambient";
        public static final String DIFFUSE = "diffuse";
        public static final String SPECULAR = "specular";
        public static final String LINEAR = "linear";
        public static final String CONSTANT = "constant";
        public static final String QUADRATIC = "quadratic";

        private Light() {
        }

        public static final class Point {
            public static final String POINTER = "pointLight";
            private static final String ARR = Uniforms.getARR("pointLight");
            public static final String POSITION_INDEX = Uniforms.getField(ARR, "position");
            public static final String AMBIENT_INDEX = Uniforms.getField(ARR, "ambient");
            public static final String DIFFUSE_INDEX = Uniforms.getField(ARR, "diffuse");
            public static final String SPECULAR_INDEX = Uniforms.getField(ARR, "specular");
            public static final String LINEAR_INDEX = Uniforms.getField(ARR, "linear");
            public static final String CONSTANT_INDEX = Uniforms.getField(ARR, "constant");
            public static final String QUADRATIC_INDEX = Uniforms.getField(ARR, "quadratic");

            private Point() {
            }
        }

        public static final class Directional {
            public static final String POINTER = "dirLight";
            public static final String AMBIENT = Uniforms.getField("dirLight", "ambient");
            public static final String DIFFUSE = Uniforms.getField("dirLight", "diffuse");
            public static final String SPECULAR = Uniforms.getField("dirLight", "specular");
            public static final String DIRECTION = Uniforms.getField("dirLight", "direction");

            private Directional() {
            }
        }
    }

    public static final class Camera {
        public static final String VIEW = "view";
        public static final String PROJECTION = "projection";
        public static final String VIEW_POS = "viewPos";

        private Camera() {
        }
    }
}

