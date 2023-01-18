#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PaletteSampler;

in vec2 texCoord;

uniform vec2 InSize;

out vec4 fragColor;

float getClosestColor(vec3 color);

void main() {
    if (texture(DiffuseSampler,texCoord).a == 0) {
        fragColor = vec4(0,0,0,0);
        return;
    }

    vec3 color = texture(DiffuseSampler,texCoord).rgb;
    float pos = getClosestColor(color);
    fragColor = vec4(pos,0,0,1);
}

float getClosestColor(vec3 color) {
    vec2 size = textureSize(PaletteSampler,0);
    float multiply = 1/size.x;

    float pos = 0;
    float bestScore = 4;

    for (float i = 0; i < size.x; i++) {
            vec4 optionColor = texture(PaletteSampler, vec2(i/size.x, 0));

            float r = abs(optionColor.r - color.r);
            float g = abs(optionColor.g - color.g);
            float b = abs(optionColor.b - color.b);

            float score = r+g+b;
            if (score < bestScore) {
                bestScore = score;
                pos = i;
            }

    }

    return pos/size.x;
}