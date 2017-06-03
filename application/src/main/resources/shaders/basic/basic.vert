#version 400

layout (location = 0) in vec3 position;

uniform mat4 projectionMatrix;

void main() {
    vec3 currentPosition = vec3(position);
    vec3 spherePosition = vec3(0);

    currentPosition.x = (currentPosition.x * 180.0) - 180.0;
    currentPosition.y = (currentPosition.y * 180.0) - 180.0;

    currentPosition.x = (currentPosition.x * 3.141592) / 180.0;
    currentPosition.y = (currentPosition.y * 3.141592) / 180.0;

    spherePosition.x = 1.0 * cos(currentPosition.y) * cos(currentPosition.x);
    spherePosition.y = 1.0 * cos(currentPosition.y) * sin(currentPosition.x);
    spherePosition.z = 1.0 * sin(currentPosition.y);

    gl_Position = vec4(spherePosition, 1.0);
}