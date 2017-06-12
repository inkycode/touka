package com.inkycode.touka.game.screens;

import java.util.Map;
import java.util.concurrent.Callable;

import org.joml.Vector3f;
import org.slf4j.Logger;

import com.inkycode.touka.core.application.Screen;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.Mesh;
import com.inkycode.touka.core.graphics.MeshFactory;
import com.inkycode.touka.core.graphics.Renderer;
import com.inkycode.touka.core.graphics.ShaderProgram;
import com.inkycode.touka.core.graphics.VertexFactory;
import com.inkycode.touka.core.graphics.impl.vertex.Position3Vertex;
import com.inkycode.touka.core.platform.Canvas;

public class MainScreen implements Screen {

    @Inject
    @Source("logger")
    private Logger log;

    @Inject
    @Source("component")
    private Renderer renderer;

    @Inject
    @Source("component")
    private Canvas canvas;

    @Inject
    @Source("component")
    @Named("basic")
    private ShaderProgram basicShader;

    @Inject
    @Source("component")
    @Named("position3")
    private MeshFactory meshFactory;

    private Mesh mesh;

    @Override
    public void load() {

        int maxXSections = 10;
        int maxYSections = 8;

        VertexFactory vertexFactory = meshFactory.getVertexFactory();

        for (int y = 0; y < maxYSections; y ++) {
            for (int x = 0; x < maxXSections; x ++) {
                float xPos1 = ((x + 0) / (float) maxXSections), yPos1 = ((y + 0) / (float) maxYSections), zPos1 = 0.0f;
                float xPos2 = ((x + 1) / (float) maxXSections), yPos2 = ((y + 0) / (float) maxYSections), zPos2 = 0.0f;
                float xPos3 = ((x + 0) / (float) maxXSections), yPos3 = ((y + 1) / (float) maxYSections), zPos3 = 0.0f;
                
                float xPos4 = ((x + 1) / (float) maxXSections), yPos4 = ((y + 0) / (float) maxYSections), zPos4 = 0.0f;
                float xPos5 = ((x + 1) / (float) maxXSections), yPos5 = ((y + 1) / (float) maxYSections), zPos5 = 0.0f;
                float xPos6 = ((x + 0) / (float) maxXSections), yPos6 = ((y + 1) / (float) maxYSections), zPos6 = 0.0f;

                vertexFactory.setAttribute(0, new Vector3f(xPos1, yPos1, zPos1)); meshFactory.addVertex(vertexFactory.build());
                vertexFactory.setAttribute(0, new Vector3f(xPos2, yPos2, zPos2)); meshFactory.addVertex(vertexFactory.build());
                vertexFactory.setAttribute(0, new Vector3f(xPos3, yPos3, zPos3)); meshFactory.addVertex(vertexFactory.build());

                vertexFactory.setAttribute(0, new Vector3f(xPos4, yPos4, zPos4)); meshFactory.addVertex(vertexFactory.build());
                vertexFactory.setAttribute(0, new Vector3f(xPos5, yPos5, zPos5)); meshFactory.addVertex(vertexFactory.build());
                vertexFactory.setAttribute(0, new Vector3f(xPos6, yPos6, zPos6)); meshFactory.addVertex(vertexFactory.build());
            }
        }

        this.mesh = meshFactory.build();

        this.basicShader.load();

        this.renderer.setViewport(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    public void unload() {
        this.basicShader.unload();

        this.mesh.destroy();
    }

    @Override
    public void render() {
        this.basicShader.use(new Callable<Void>() {

            @Override
            public Void call() {
                MainScreen.this.mesh.render();

                return null;
            }

        });
    }

    @Override
    public void update() {
        
    }
}