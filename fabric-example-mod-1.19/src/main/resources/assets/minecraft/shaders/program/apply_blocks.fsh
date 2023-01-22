#version 150

uniform sampler2D DiffuseSampler;

uniform sampler2D BlockSamplerx16;
uniform sampler2D BlockSamplerx8;
uniform sampler2D BlockSamplerx4;
uniform sampler2D BlockSamplerx2;
uniform sampler2D BlockSamplerx1;

in vec2 texCoord;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float BlockSize;

out vec4 fragColor;

vec2 getSamplerSize();
vec4 getSamplerTexture(vec2 coords);

void main() {
    ivec2 realCoords = ivec2(texCoord.x * InSize.x, texCoord.y * InSize.y);

    ivec2 mod = ivec2(mod(realCoords.x, BlockSize), mod(realCoords.y, BlockSize));

    vec2 pixelpos = vec2(texCoord.x - mod.x/InSize.x, texCoord.y - mod.y/InSize.y);
    vec2 sizeBlock = getSamplerSize();


    vec4 paletteColor = texture(DiffuseSampler, pixelpos);

    float colorPos = paletteColor.r*255 + paletteColor.g*255;
    float blockPos = (colorPos*BlockSize+0.5)/sizeBlock.x;


    fragColor = getSamplerTexture(vec2 (blockPos+mod.x/sizeBlock.x,1- mod.y/sizeBlock.y - 1/sizeBlock.y));
}

vec2 getSamplerSize() {
    if (BlockSize == 1) {
        return textureSize(BlockSamplerx1,0);
    }
    if (BlockSize == 2) {
        return textureSize(BlockSamplerx2,0);
    }
    if (BlockSize == 4) {
        return textureSize(BlockSamplerx4,0);
    }
    if (BlockSize == 8) {
        return textureSize(BlockSamplerx8,0);
    }

    return textureSize(BlockSamplerx16,0);
}

vec4 getSamplerTexture(vec2 coords) {
    if (BlockSize == 1) {
        return texture(BlockSamplerx1,coords);
    }
    if (BlockSize == 2) {
        return texture(BlockSamplerx2,coords);
    }
    if (BlockSize == 4) {
        return texture(BlockSamplerx4,coords);
    }
    if (BlockSize == 8) {
        return texture(BlockSamplerx8,coords);
    }

    return texture(BlockSamplerx16,coords);
}