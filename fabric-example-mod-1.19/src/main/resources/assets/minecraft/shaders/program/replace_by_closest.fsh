#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PaletteSampler;

in vec2 texCoord;

uniform vec2 InSize;

out vec4 fragColor;

void main() {
    vec4 color =  texture(DiffuseSampler, texCoord);
    if (color.a == 0) {
        fragColor = vec4(0,0,0,0);
        return;
    }

    ivec3 colorInt = ivec3(color.r*127,color.g*127,color.b*127);

    int c = (colorInt.r << 14) | (colorInt.g << 7) | colorInt.b;

    vec2 size = textureSize(PaletteSampler,0);

    float yPos = floor(c/size.y)+1;
    float xPos = mod(c, int(size.x))+1;

    fragColor = texture(PaletteSampler, vec2(xPos/size.x,yPos/size.y));
}