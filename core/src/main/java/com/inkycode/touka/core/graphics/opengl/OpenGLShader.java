package com.inkycode.touka.core.graphics.opengl;

import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_INSTANCE_NAME;

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
import com.inkycode.touka.core.bootstrap.annotations.Named;
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
    @Named(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INSTANCE_NAME)
    private String name;

    @Override
    public void load() {
        log.info("'{}': Loading shader, with type '{}'", this.name, this.shaderType);

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
                log.info("'{}': Shader source loaded", this.name);

                log.info("'{}': Compiling shader", this.name);
                this.handle = glCreateShader(getOpenGLShaderType(this.shaderType));
                glShaderSource(this.handle, stringBuffer.toString());
                glCompileShader(this.handle);

                try (MemoryStack stack = stackPush()) {
                    IntBuffer compileStatus = stack.mallocInt(1);

                    glGetShaderiv(this.handle, GL_COMPILE_STATUS, compileStatus);
                    if (compileStatus.get(0) <= 0) {
                        // TODO: Handle compile error
                        log.error("'{}': Failed to compile shader", this.name);

                        this.unload();

                        return;
                    }
                }

                log.error("'{}': Successfully compiled shader", this.name);
            } else {
                log.error("'{}': Unable to find '{}'", this.name, this.sourceFilePath);
            }
        } catch (IOException e) {
            // TODO: Handle error
        }

        log.error("'{}': Successfully loaded shader", this.name);
    }

    @Override
    public void unload() {
        log.info("'{}': Unloading shader", this.name);

        if (this.isValid()) {
            glDeleteShader(this.handle);

            this.handle = -1;
        }

        log.info("'{}': Successfully unloaded shader", this.name);
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