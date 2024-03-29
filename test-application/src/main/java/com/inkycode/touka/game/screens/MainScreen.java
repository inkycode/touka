package com.inkycode.touka.game.screens;

import java.util.concurrent.Callable;

import org.slf4j.Logger;

import com.inkycode.touka.core.application.Screen;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.Mesh;
import com.inkycode.touka.core.graphics.MeshFactory;
import com.inkycode.touka.core.graphics.primitive.PrimitiveFactory;
import com.inkycode.touka.core.graphics.Renderer;
import com.inkycode.touka.core.graphics.ShaderProgram;
import com.inkycode.touka.core.graphics.vertex.VertexFactory;
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
        PrimitiveFactory primitiveFactory = meshFactory.getPrimitiveFactory();

        int primitiveOffset = 0;

        for (int y = 0; y < maxYSections; y ++) {
            for (int x = 0; x < maxXSections; x ++) {
                float xPos1 = ((x + 0) / (float) maxXSections), yPos1 = ((y + 0) / (float) maxYSections), zPos1 = 0.0f;
                float xPos2 = ((x + 1) / (float) maxXSections), yPos2 = ((y + 0) / (float) maxYSections), zPos2 = 0.0f;
                float xPos3 = ((x + 0) / (float) maxXSections), yPos3 = ((y + 1) / (float) maxYSections), zPos3 = 0.0f;
                
                float xPos4 = ((x + 1) / (float) maxXSections), yPos4 = ((y + 0) / (float) maxYSections), zPos4 = 0.0f;
                float xPos5 = ((x + 1) / (float) maxXSections), yPos5 = ((y + 1) / (float) maxYSections), zPos5 = 0.0f;
                float xPos6 = ((x + 0) / (float) maxXSections), yPos6 = ((y + 1) / (float) maxYSections), zPos6 = 0.0f;

                vertexFactory.setAttribute(0, new float[] {xPos1, yPos1, zPos1}); meshFactory.addVertex(vertexFactory.build());
                vertexFactory.setAttribute(0, new float[] {xPos2, yPos2, zPos2}); meshFactory.addVertex(vertexFactory.build());
                vertexFactory.setAttribute(0, new float[] {xPos3, yPos3, zPos3}); meshFactory.addVertex(vertexFactory.build());

                primitiveFactory.setIndex(0, primitiveOffset + 0);
                primitiveFactory.setIndex(1, primitiveOffset + 1);
                primitiveFactory.setIndex(2, primitiveOffset + 2);
                meshFactory.addPrimitive(primitiveFactory.build());

                primitiveOffset += 3;

                vertexFactory.setAttribute(0, new float[] {xPos4, yPos4, zPos4}); meshFactory.addVertex(vertexFactory.build());
                vertexFactory.setAttribute(0, new float[] {xPos5, yPos5, zPos5}); meshFactory.addVertex(vertexFactory.build());
                vertexFactory.setAttribute(0, new float[] {xPos6, yPos6, zPos6}); meshFactory.addVertex(vertexFactory.build());

                primitiveFactory.setIndex(0, primitiveOffset + 0);
                primitiveFactory.setIndex(1, primitiveOffset + 1);
                primitiveFactory.setIndex(2, primitiveOffset + 2);
                meshFactory.addPrimitive(primitiveFactory.build());
                primitiveOffset += 3;
            }
        }

        this.mesh = meshFactory.build();

        log.info("Mesh stats {vertexCount = {}, indexCount = {}, primitiveCount = {}}", this.mesh.getVertexCount(), this.mesh.getIndexCount(), this.mesh.getPrimitiveCount());

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