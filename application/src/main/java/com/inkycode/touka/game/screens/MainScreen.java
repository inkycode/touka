package com.inkycode.touka.game.screens;

import java.util.Map;
import java.util.concurrent.Callable;

import org.joml.Vector3f;
import org.slf4j.Logger;

import com.inkycode.touka.core.application.Screen;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.Mesh;
import com.inkycode.touka.core.graphics.MeshFactory;
import com.inkycode.touka.core.graphics.Renderer;
import com.inkycode.touka.core.graphics.ShaderProgram;
import com.inkycode.touka.core.graphics.vertex.Position3Vertex;
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
    private Map<String, ShaderProgram> shaderPrograms;

    @Inject
    @Source("component")
    private MeshFactory meshFactory;

    private Mesh mesh;

    @Override
    public void load() {

        int maxXSections = 10;
        int maxYSections = 8;

        for (int y = 0; y < maxYSections; y ++) {
            for (int x = 0; x < maxXSections; x ++) {
                float xPos1 = ((x + 0) / (float) maxXSections), yPos1 = ((y + 0) / (float) maxYSections), zPos1 = 0.0f;
                float xPos2 = ((x + 1) / (float) maxXSections), yPos2 = ((y + 0) / (float) maxYSections), zPos2 = 0.0f;
                float xPos3 = ((x + 0) / (float) maxXSections), yPos3 = ((y + 1) / (float) maxYSections), zPos3 = 0.0f;
                
                float xPos4 = ((x + 1) / (float) maxXSections), yPos4 = ((y + 0) / (float) maxYSections), zPos4 = 0.0f;
                float xPos5 = ((x + 1) / (float) maxXSections), yPos5 = ((y + 1) / (float) maxYSections), zPos5 = 0.0f;
                float xPos6 = ((x + 0) / (float) maxXSections), yPos6 = ((y + 1) / (float) maxYSections), zPos6 = 0.0f;

                meshFactory.addVertex(new Position3Vertex(new Vector3f(xPos1, yPos1, zPos1)));
                meshFactory.addVertex(new Position3Vertex(new Vector3f(xPos2, yPos2, zPos2)));
                meshFactory.addVertex(new Position3Vertex(new Vector3f(xPos3, yPos3, zPos3)));
                
                meshFactory.addVertex(new Position3Vertex(new Vector3f(xPos4, yPos4, zPos4)));
                meshFactory.addVertex(new Position3Vertex(new Vector3f(xPos5, yPos5, zPos5)));
                meshFactory.addVertex(new Position3Vertex(new Vector3f(xPos6, yPos6, zPos6)));
            }
        }

        this.mesh = meshFactory.build();

        this.shaderPrograms.get("basic").load();

        this.renderer.setViewport(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }

    public void unload() {
        this.shaderPrograms.get("basic").unload();

        this.mesh.destroy();
    }

    @Override
    public void render() {
        this.shaderPrograms.get("basic").use(new Callable<Void>() {

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