#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PaletteSampler;

in vec2 texCoord;

uniform vec2 InSize;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    if (color.a == 0) {
        fragColor = vec4(0,0,0,0);
        return;
    }

    int c = (int(color.r*127) << 14) | (int(color.g*127) << 7) | int(color.b*127);

    int sizeX = int(textureSize(PaletteSampler,0).x);
    int sizeY = int(textureSize(PaletteSampler,0).y);

    float yPos = c/sizeY;
    float xPos =  mod(c, sizeX);

    fragColor = texture(PaletteSampler, vec2((xPos+0.5) / float(sizeX), ((yPos +0.5) / float(sizeY))), 0);
}