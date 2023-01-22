#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float BlockSize;

out vec4 fragColor;

vec4 getAverageColor(vec2 blockSize);

void main() {
    ivec2 realCoords = ivec2(texCoord.x * InSize.x,texCoord.y * InSize.y);

    if (mod(realCoords.x,BlockSize) == 0 && mod(realCoords.y,BlockSize) == 0) {
        vec2 blockSize = vec2(BlockSize/InSize.x, BlockSize/InSize.y);
        fragColor = getAverageColor(blockSize);
        return;
    }

    fragColor = vec4(0,0,0,0);
}

vec4 getAverageColor(vec2 blockSize) {
    vec2 add = vec2(1/InSize.x,1/InSize.y);
    vec3 avColor = vec3(0,0,0);
    float samples = 0;
    for (float i = texCoord.x; i < blockSize.x + texCoord.x; i += add.x) {
        for (float j = texCoord.y; j < blockSize.y + texCoord.y; j+= add.y) {
            samples++;

            vec4 rgb = texture(DiffuseSampler,vec2(i,j));
            avColor.r += rgb.r;
            avColor.g += rgb.g;
            avColor.b += rgb.b;
        }
    }

    return vec4(avColor.r/samples,avColor.g/samples,avColor.b/samples, 1.0);
}
