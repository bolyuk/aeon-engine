#version 330 core
layout (location = 0)
in vec4 vertex;
out vec2 TexCoords;

uniform mat4 projection;
uniform vec4 uRotation;
uniform vec2 uOrigin;

vec2 applyQuaternion2D(vec2 pos, vec4 q)
{
    float zz = q.z * q.z;
    float ww = q.w * q.w;
    float xz = q.x * q.z;
    float yw = q.y * q.w;
    float yz = q.y * q.z;
    float xw = q.x * q.w;

    vec3 rotated = vec3(
        pos.x * (ww + zz - 1.0 + 1.0 - 2.0*zz) + pos.y * (-2.0*(xz + yw)),
        pos.x * (2.0*(xz - yw))                 + pos.y * (ww - zz + 1.0 - 2.0*zz),
        0.0
    );

    float cosA = q.w * q.w - q.z * q.z;
    float sinA = 2.0 * q.w * q.z;

    return vec2(
        pos.x * cosA - pos.y * sinA,
        pos.x * sinA + pos.y * cosA
    );
}

void main()
{
    vec2 pos = vertex.xy - uOrigin;
    vec2 rotated = applyQuaternion2D(pos, uRotation);
    vec2 finalPos = rotated + uOrigin;

    gl_Position = projection * vec4(finalPos, 0.0, 1.0);
    TexCoords = vertex.zw;
}