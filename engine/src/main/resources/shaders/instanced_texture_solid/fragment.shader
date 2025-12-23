#version 330 core

in vec2 TexCoord;
out vec4 FragColor;

uniform sampler2D uTexture0;
uniform vec4 color;

void main()
{
    vec4 texColor = texture(uTexture0, TexCoord);
    FragColor = texColor * color;
}
