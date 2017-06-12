package com.inkycode.touka.core.graphics.impl.opengl;

import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_INSTANCE_NAME;

import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glGetProgramiv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;

import static org.lwjgl.system.MemoryStack.stackPush;

import static java.util.stream.Collectors.toMap;

import java.nio.IntBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;

import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.Shader;
import com.inkycode.touka.core.graphics.ShaderProgram;

public class OpenGLShaderProgram implements ShaderProgram {

    @Inject
    @Source("logger")
    private Logger log;

    @Inject 
    @Source("component")
    private Map<String, Shader> shaderPool;

    @Inject
    @Source("property")
    private List<String> shaderNames;

    @Inject
    @Source("property")
    @Named(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INSTANCE_NAME)
    private String name;

    private int handle;

    private Collection<Shader> shaders;

    @Override
    public void load() {
        log.info("'{}': Loading shader program", this.name);

        log.info("'{}': Filtering shader pool for required shaders", this.name);
        this.shaders =
            shaderPool.entrySet()
                .stream()
                .filter(entry -> shaderNames.contains(entry.getKey()))
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()))
                .values();
        log.info("'{}': Obtained all required shaders", this.name);
        
        log.info("'{}': Loading shaders, if required, and attaching shaders to shader program", this.name);
        this.handle = glCreateProgram();
        for (final Shader shader : this.shaders) {
            if (!shader.isValid()) {
                shader.load();
            }

            glAttachShader(this.handle, ((OpenGLShader) shader).getHandle());
        }

        log.info("'{}': Linking program", this.name);
        glLinkProgram(this.handle);
        try (final MemoryStack stack = stackPush()) {
            final IntBuffer linkStatus = stack.mallocInt(1);

            glGetProgramiv(this.handle, GL_LINK_STATUS, linkStatus);
            if (linkStatus.get(0) <= 0) {
                // TODO: Handle linking error
                log.info("'{}': Failed to link shader program", this.name);

                this.unload();

                return;
            }
        }

        log.info("'{}': Successfully linked shader program", this.name);
    }

    @Override
    public void unload() {
        log.info("'{}': Unloading shader program", this.name);

        if (this.isValid()) {
            this.handle = -1;

            glDeleteProgram(this.handle);
        }

        for (final Shader shader : this.shaders) {
            shader.unload();
        }

        log.info("'{}': Successfully unloaded shader program", this.name);
    }

    @Override
    public void use(final Callable<Void> task) {
        glUseProgram(this.handle);

        try {
            task.call();
        } catch (Exception e) {
            // TODO: Handle exception
        }

        glUseProgram(0);
    }

    @Override
    public boolean isValid() {
        return this.handle > 0;
    }

}