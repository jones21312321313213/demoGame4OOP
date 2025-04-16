package com.example.demo;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.ui.FXGLCheckBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.HashMap;

import static com.almasb.fxgl.dsl.FXGL.*;

public class HelloApplication extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1200);
        settings.setHeight(240);
        settings.setTitle("OOP GAME");
        settings.setVersion("0.1");
        settings.setIntroEnabled(true);// i true later
        settings.setGameMenuEnabled(false);
        settings.setMainMenuEnabled(true);// i true later
        settings.setCloseConfirmation(true);
    }

    private Entity player;
    private Entity player2;
    @Override
    protected void initInput(){
       getInput().addAction(new UserAction("P1Left"){
           @Override
           protected void onAction(){
                player.getComponent(PlayerControl.class).left();
           }
       },KeyCode.A);

        getInput().addAction(new UserAction("P1Right"){
            @Override
            protected void onAction(){
                player.getComponent(PlayerControl.class).right();
            }
        },KeyCode.D);

        getInput().addAction(new UserAction("P1Jump"){
            @Override
            protected void onAction(){
                player.getComponent(PlayerControl.class).up();
            }
        },KeyCode.W);

        getInput().addAction(new UserAction("P1Punch"){
            @Override
            protected void onAction(){
                player.getComponent(PlayerControl.class).punch();
            }
        },KeyCode.F);

        // temp

        getInput().addAction(new UserAction("P2Left"){
            @Override
            protected void onAction(){
                player2.getComponent(PlayerControl2.class).P2left();
            }
        },KeyCode.J);

        getInput().addAction(new UserAction("P2Right"){
            @Override
            protected void onAction(){
                player2.getComponent(PlayerControl2.class).P2right();
            }
        },KeyCode.L);

        getInput().addAction(new UserAction("P2Jump"){
            @Override
            protected void onAction(){
                player2.getComponent(PlayerControl2.class).P2up();
            }
        },KeyCode.I);

        getInput().addAction(new UserAction("P2Punch"){
            @Override
            protected void onAction(){
                player2.getComponent(PlayerControl2.class).P2punch();
            }
        },KeyCode.H);

//        onKeyDown(KeyCode.F, () -> {
//            System.out.println("hid");
//            getNotificationService().pushNotification("Hello world ");
//        });
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 120); // Adjust the Y value as needed
    }

    @Override
    protected void initGame(){
        getGameWorld().addEntityFactory(new SimpleFactory());
      Level level = getAssetLoader().loadLevel("tmx/bg.tmx", new TMXLevelLoader());
      getGameWorld().setLevel(level);





      player = getGameWorld().spawn("player",100,100);
      player2 = getGameWorld().spawn("player2",1000,100);

//        run(() -> {
//            spawn("ally", FXGLMath.randomPoint(
//               new Rectangle2D(0,0, getAppWidth(), getAppHeight())
//            ));
//            spawn("enemy", FXGLMath.randomPoint(
//                    new Rectangle2D(0,0, getAppWidth(), getAppHeight())
//            ));
//        }, Duration.seconds(1));
    }
    public static void main(String[] args) {
        launch(args);
    }

}