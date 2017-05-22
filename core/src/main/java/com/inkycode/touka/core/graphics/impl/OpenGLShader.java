package com.inkycode.touka.core.graphics.impl;

import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.Callable;

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


    @Override
    public void load() {
        //handle = glCreateShader(getOpenGLShaderType(this.shaderType));

        StringBuffer stringBuffer = new StringBuffer();

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(sourceFilePath)) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
            }
        } catch (IOException e) {
            // TODO: Handle error
        }

        String shaderSource = stringBuffer.toString();

        log.error("source: {}", shaderSource);
    }

    @Override
    public void unload() {
    }

    @Override
    public void use(Callable<Void> task) {
        try {
            task.call();
        } catch (Exception e) {
            // TODO: Handle error state
        }
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