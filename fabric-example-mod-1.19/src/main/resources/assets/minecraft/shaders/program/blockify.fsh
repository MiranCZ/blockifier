#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D BlockSampler;
uniform sampler2D PaletteSampler;

in vec2 texCoord;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float BlockSize;

out vec4 fragColor;


void main() {
    vec2 size = textureSize(DiffuseSampler, 0);
    vec2 multiplier = vec2(1/size.x, 1/size.y);
    ivec2 realCoords = ivec2(texCoord.x * size.x, texCoord.y * size.y);

    ivec2 mod = ivec2(mod(realCoords.x, BlockSize), mod(realCoords.y, BlockSize));

    vec2 pixelpos = vec2(texCoord.x - multiplier.x*mod.x, texCoord.y - multiplier.y*mod.y);
    float blockPos =texture(DiffuseSampler, pixelpos).r;

    if (BlockSize == 1) {
        fragColor = texture(PaletteSampler, vec2(blockPos, 0));
        return;
    }

    vec2 sizeBlock = textureSize(BlockSampler, 0);
    vec2 multiplyBlock = vec2(1/sizeBlock.x, 1/sizeBlock.y);

    blockPos*= textureSize(PaletteSampler,0).x;
    blockPos *= BlockSize;
    blockPos *= multiplyBlock.x;

    float modX = mod(realCoords.x, BlockSize);
    float modY = BlockSize - mod(realCoords.y, BlockSize);


    fragColor = texture(BlockSampler, vec2 (blockPos+modX*multiplyBlock.x, modY*multiplyBlock.y));
}