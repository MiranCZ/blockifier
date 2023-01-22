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

    ivec3 colorInt = ivec3(color.r*127,color.g*127,color.b*127);

    int c = (colorInt.r << 14) | (colorInt.g << 7) | colorInt.b;

    bool first = c%2==0;
    int texturePos = c/2;

    vec2 size = textureSize(PaletteSampler,0);

    float xPos = mod(texturePos, int(size.x));
    float yPos = floor(texturePos/size.y);

    vec4 paletteColor = texture(PaletteSampler, vec2(xPos/size.x,yPos/size.y));

    float colorPos = 0;
    if (first) {
        colorPos = paletteColor.r*255 + paletteColor.g*255;
    } else {
        colorPos = paletteColor.b*255 + paletteColor.a*255;
    }
    //FIXME 453 is static value for the length of the blocks
    fragColor =  vec4(colorPos/453,0,0,1);

}