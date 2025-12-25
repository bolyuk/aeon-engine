#version 330 core

out vec4 FragColor;

struct PointLight {
    vec3 position;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct DirLight {
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

#define NR_POINT_LIGHTS 4

in vec3 FragPos;
in vec3 Normal;
in vec2 TexCoord;

uniform vec3 viewPos;

uniform vec4 color;

uniform sampler2D uTexture0;
uniform int useTexture; // 0 = no texture, 1 = use uTexture0

uniform PointLight pointLight[NR_POINT_LIGHTS];
uniform DirLight dirLight;

vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir, vec3 albedo) {
    vec3 lightDir = normalize(light.position - fragPos);

    float diff = max(dot(normal, lightDir), 0.0);

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    spec = clamp(spec, 0.0, 1.0);

    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    vec3 ambient  = light.ambient  * albedo;
    vec3 diffuse  = light.diffuse  * diff * albedo;
    vec3 specular = light.specular * spec * albedo;

    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;

    return (ambient + diffuse + specular);
}

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir, vec3 albedo)
{
    vec3 lightDir = normalize(-light.direction);

    float diff = max(dot(normal, lightDir), 0.0);

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    spec = clamp(spec, 0.0, 1.0);

    vec3 ambient  = light.ambient  * albedo;
    vec3 diffuse  = light.diffuse  * diff * albedo;
    vec3 specular = light.specular * spec * albedo;

    return (ambient + diffuse + specular);
}

void main() {
    vec3 norm = normalize(Normal);
    vec3 viewDir = normalize(viewPos - FragPos);

    vec4 tex = texture(uTexture0, TexCoord);

    vec4 base = (useTexture != 0) ? (tex * color) : color;

    vec3 albedo = base.rgb;

    vec3 result = albedo * 0.05;

    result += CalcDirLight(dirLight, norm, viewDir, albedo);

    for (int i = 0; i < NR_POINT_LIGHTS; i++) {
        if (length(pointLight[i].diffuse) > 0.001) {
            result += CalcPointLight(pointLight[i], norm, FragPos, viewDir, albedo);
        }
    }

    FragColor = vec4(clamp(result, 0.0, 1.0), base.a);
}