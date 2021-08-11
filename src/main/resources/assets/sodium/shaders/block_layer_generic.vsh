#version 150 core
#import <sodium:shaders/include/fog.glsl>

struct DrawParameters {
// Older AMD drivers can't handle vec3 in std140 layouts correctly
// The alignment requirement is 16 bytes (4 float components) anyways, so we're not wasting extra memory with this,
// only fixing broken drivers.
    vec4 Offset;
};

in vec4 a_Pos; // The position of the vertex around the model origin
in vec4 a_Color; // The color of the vertex
in vec2 a_TexCoord; // The block texture coordinate of the vertex
in vec2 a_LightCoord; // The light texture coordinate of the vertex

uniform mat4 u_ProjectionMatrix;
uniform mat4 u_ModelViewMatrix;

uniform float u_ModelScale;
uniform float u_TextureScale;

layout(std140) uniform ubo_DrawParameters {
    DrawParameters Chunks[256];
};

out vec4 v_Color;
out vec2 v_TexCoord;
out vec2 v_LightCoord;

out float v_FragDistance;

void main() {
    vec3 vertexPosition = (a_Pos.xyz * u_ModelScale) + Chunks[int(a_Pos.w)].Offset.xyz; // AMD drivers also need this manually inlined
    vec4 pos = u_ModelViewMatrix * vec4(vertexPosition, 1.0);

    v_FragDistance = length(pos);

    // Transform the vertex position into model-view-projection space
    gl_Position = u_ProjectionMatrix * pos;

    // Pass the color and texture coordinates to the fragment shader
    v_Color = a_Color;
    v_TexCoord = a_TexCoord * u_TextureScale;
    v_LightCoord = a_LightCoord;
}