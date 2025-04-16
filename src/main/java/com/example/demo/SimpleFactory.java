package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class SimpleFactory implements EntityFactory {

    @Spawns("platform")
    public Entity newPlatForm(SpawnData data){
        var physics = new PhysicsComponent();
        int width =  data.get("width");
        int height =  data.get("height");

        return FXGL.entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(width, height)))
                .with(physics)
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
//
//        var width = data.<Integer>get("width");
//        var height = data.<Integer>get("height");

        return FXGL.entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(32,42)))
                //.viewWithBBox(new Rectangle(30,30,Color.BLUE))
                //.with(new CollidableComponent(true))
                .with(physics)
                .with(new PlayerControl())
                .build();
    }

    @Spawns("player2")
    public Entity newPlayer2(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
//
//        var width = data.<Integer>get("width");
//        var height = data.<Integer>get("height");

        return FXGL.entityBuilder(data)
                .bbox(new HitBox(BoundingShape.box(32,42)))
                //.viewWithBBox(new Rectangle(30,30,Color.BLUE))
                .with(physics)
                .with(new PlayerControl2())
                .build();
    }



}
