package com.shc.silenceengine.collision;

import com.shc.silenceengine.entity.Entity2D;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.SceneNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class GridSceneCollider implements SceneCollider2D
{
    private Scene scene;
    private Grid  grid;

    private List<Entity2D> entities;

    private HashMap<Class<? extends Entity2D>, Class<? extends Entity2D>> collisionMap;

    public GridSceneCollider(int mapWidth, int mapHeight, int cellWidth, int cellHeight)
    {
        grid = new Grid(mapWidth, mapHeight, cellWidth, cellHeight);
        entities = new ArrayList<>();
        collisionMap = new HashMap<>();
    }

    @Override
    public void setScene(Scene scene)
    {
        this.scene = scene;
    }

    @Override
    public Scene getScene()
    {
        return scene;
    }

    @Override
    public void register(Class<? extends Entity2D> type1, Class<? extends Entity2D> type2)
    {
        collisionMap.put(type1, type2);
    }

    @Override
    public void checkCollisions()
    {
        grid.clear();
        entities.clear();

        for (SceneNode child : scene.getChildren())
            if (child instanceof Entity2D)
            {
                Entity2D entity = (Entity2D) child;

                grid.insert(entity);
                entities.add(entity);
            }

        for (Class<? extends Entity2D> class1 : collisionMap.keySet())
            for (Entity2D entity : entities)
                if (class1.isInstance(entity))
                {
                    List<Entity2D> collidables = grid.retrieve(entity);

                    for (Entity2D entity2 : collidables)
                        if (collisionMap.get(class1).isInstance(entity2))
                            // Check collision
                            if (entity.getPolygon().intersects(entity2.getPolygon()))
                                entity.collision(entity2);
                }
    }
}