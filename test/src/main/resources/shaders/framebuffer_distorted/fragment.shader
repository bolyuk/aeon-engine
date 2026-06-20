#version 330 core

in vec2 fragUV;
out vec4 fragColor;

uniform sampler2D screenTexture;
uniform float time;

float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec2 uv = fragUV;

    uv.x += sin(uv.y * 40.0 + time * 5.0) * 0.003;

    float lineNoise = rand(vec2(floor(uv.y * 60.0), floor(time * 8.0)));
    if (lineNoise > 0.985) {
        uv.x += (rand(vec2(time, uv.y)) - 0.5) * 0.08;
    }

    float aberration = 0.004;
    float r = texture(screenTexture, uv + vec2(aberration, 0.0)).r;
    float g = texture(screenTexture, uv).g;
    float b = texture(screenTexture, uv - vec2(aberration, 0.0)).b;
    vec3 color = vec3(r, g, b);

    color -= sin(uv.y * 800.0) * 0.03;

    color += (rand(uv * time) - 0.5) * 0.05;

    fragColor = vec4(color, 1.0);
}