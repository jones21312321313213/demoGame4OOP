package com.example.demo.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.example.demo.HealthComponent;
import com.example.demo.HelloController;
import com.example.demo.PlayerControl;
import com.example.demo.PlayerControl2;
import com.example.demo.listeners.GameEntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class SimpleFactory implements EntityFactory {

    private HelloController h = new HelloController();
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

        Rectangle outline = new Rectangle(115, 195);
        outline.setFill(Color.TRANSPARENT);
        outline.setStroke(Color.RED);
        outline.setStrokeWidth(1);
        outline.setTranslateX(15);
        outline.setTranslateY(5);

        HitBox offsetHitBox = new HitBox("PLAYER_HITBOX", new Point2D(15, 5), BoundingShape.box(115, 195));

        return FXGL.entityBuilder(data)
                .type(GameEntityType.PLAYER)
                .bbox(offsetHitBox)
                .with("id", "player1")
                .with(new HealthComponent(100))
                .with(physics)
                .with(new PlayerControl())
                .viewWithBBox(outline)
                .build();
    }

    @Spawns("player2")
    public Entity newPlayer2(SpawnData data){
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        Rectangle outline = new Rectangle(115, 195);
        outline.setFill(Color.TRANSPARENT);
        outline.setStroke(Color.RED);
        outline.setStrokeWidth(1);
        outline.setTranslateX(15);
        outline.setTranslateY(5);

        HitBox offsetHitBox = new HitBox("PLAYER_HITBOX", new Point2D(15, 5), BoundingShape.box(115, 195));

        return FXGL.entityBuilder(data)
                .type(GameEntityType.PLAYER2)
                .bbox(offsetHitBox)
                .with("id", "player2")
                .with(new HealthComponent(100))
                .with(physics)
                .with(new PlayerControl2("female"))
                .viewWithBBox(outline)
                .build();
    }

}
