#version 330 core

in vec2 fragUV;
out vec4 fragColor;

uniform sampler2D screenTexture;

uniform vec2 screenSize;
uniform float pixelSize;

void main() {
    vec2 grid = screenSize / pixelSize;
    vec2 pixelatedUV = (floor(fragUV * grid) + 0.5) / grid;
    fragColor = texture(screenTexture, pixelatedUV);
}