#version 330 core

in vec2 fragUV;
out vec4 fragColor;

uniform sampler2D screenTexture;

void main() {
    vec3 color = texture(screenTexture, fragUV).rgb;
    fragColor = vec4(1.0 - color, 1.0);
}