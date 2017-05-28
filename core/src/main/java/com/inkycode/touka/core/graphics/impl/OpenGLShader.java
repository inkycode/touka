package com.inkycode.touka.core.graphics.impl;

import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;

import static org.lwjgl.system.MemoryStack.stackPush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;

import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.Shader;

public class OpenGLShader implements Shader {

    private int handle;

    @Inject
    @Source("logger")
    private Logger log;

    @Inject
    @Source("property")
    private String sourceFilePath;

    @Inject
    @Source("property")
    private int shaderType;

    @Inject
    @Source("property")
    private String instanceName;

    @Override
    public void load() {
        log.info("Loading shader {}, with type {}", this.sourceFilePath, this.shaderType);

        StringBuffer stringBuffer = new StringBuffer();

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(sourceFilePath)) {
            if (inputStream != null) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8")) {
                    try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        String line = "";

                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuffer.append(line + "\n");
                        }
                    }
                } catch (IOException e) {
                    // TODO: Handle error
                }
                log.info("Shader source loaded");

                log.info("Compiling shader");
                this.handle = glCreateShader(getOpenGLShaderType(this.shaderType));
                glShaderSource(this.handle, stringBuffer.toString());
                glCompileShader(this.handle);

                try (MemoryStack stack = stackPush()) {
                    IntBuffer compileStatus = stack.mallocInt(1);

                    glGetShaderiv(this.handle, GL_COMPILE_STATUS, compileStatus);
                    if (compileStatus.get(0) <= 0) {
                        // TODO: Handle compile error
                        log.error("Failed to compile shader");

                        this.unload();

                        return;
                    }
                }

                log.error("Successfully compiled shader");
            } else {
                log.error("Unable to find {}", this.sourceFilePath);
            }
        } catch (IOException e) {
            // TODO: Handle error
        }

        log.error("Successfully loaded shader {}", this.instanceName);
    }

    @Override
    public void unload() {
        log.info("Unloading shader {}", this.instanceName);

        if (this.isValid()) {
            glDeleteShader(this.handle);

            this.handle = -1;
        }

        log.error("Successfully unloaded shader {}", this.instanceName);
    }

    @Override
    public boolean isValid() {
        return this.handle > 0;
    }

    public int getHandle() {
        return this.handle;
    }

    private int getOpenGLShaderType(int type) {
        if (type == SHADER_TYPE_VERTEX) {
            return GL_VERTEX_SHADER;
        } else if (type == SHADER_TYPE_FRAGMENT) {
            return GL_FRAGMENT_SHADER;
        } else {
            // Assume fragment shader
            return GL_FRAGMENT_SHADER;
        }
    }

}