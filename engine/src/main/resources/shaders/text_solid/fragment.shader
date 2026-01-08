#version 330 core

in vec2 TexCoords;
out vec4 FragColor;

uniform sampler2D uTexture0;
uniform vec4 color;

void main()
{
    FragColor = color * vec4(1.0, 1.0, 1.0, texture(uTexture0, TexCoords).r);
}