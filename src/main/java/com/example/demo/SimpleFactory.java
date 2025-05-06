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

        Rectangle outline = new Rectangle(60, 195);
        outline.setFill(Color.TRANSPARENT);
        outline.setStroke(Color.RED);
        outline.setStrokeWidth(1);
        outline.setTranslateX(15);
        outline.setTranslateY(5);

        HitBox offsetHitBox = new HitBox("PLAYER_HITBOX", new Point2D(15, 5), BoundingShape.box(60, 195));

        return FXGL.entityBuilder(data)
                .type(GameEntityType.PLAYER)
                .bbox(offsetHitBox)
                .viewWithBBox(outline)
                .with(physics)
                .with(new HealthComponent(100))
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

        Rectangle outline = new Rectangle(60, 195);
        outline.setFill(Color.TRANSPARENT);
        outline.setStroke(Color.RED);
        outline.setStrokeWidth(1);
        outline.setTranslateX(15);
        outline.setTranslateY(5);

        HitBox offsetHitBox = new HitBox("PLAYER_HITBOX", new Point2D(15, 5), BoundingShape.box(60, 195));

        return FXGL.entityBuilder(data)
                .type(GameEntityType.PLAYER2)
                .with(new HealthComponent(100))
                .bbox(offsetHitBox)
                .viewWithBBox(outline)
                .with(physics)
                .with(new PlayerControl2())
                .build();
    }



}
