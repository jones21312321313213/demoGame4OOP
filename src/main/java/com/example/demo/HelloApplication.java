package com.example.demo;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleSystem;
import com.almasb.fxgl.ui.FXGLCheckBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

import static com.almasb.fxgl.dsl.FXGL.*;

public class HelloApplication extends GameApplication {

//    @Override
//    protected void initUI() {
//        Label label = new Label("Hello, FXGL!");
//        label.setFont(Font.font(20.0));
//        FXGL.addUINode(label, 350.0, 290.0);
//    }
private ProgressBar healthBar2;
    private ProgressBar healthBar;


    @Override
    protected void onPreInit(){
        FXGL.getAssetLoader().loadTexture("/assets/textures/background-intro.mp4");
    }


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Stictactics");
//        settings.setVersion("0.1");
        settings.setIntroEnabled(true);// i true later
        settings.setGameMenuEnabled(false);
        settings.setMainMenuEnabled(true);
        settings.setCloseConfirmation(true);
        //settings.setFullScreenAllowed(true);
        settings.setSceneFactory(new MySceneFactory());
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
        getPhysicsWorld().setGravity(0, 120);
    }

    @Override
    protected void initGame(){
        getGameWorld().addEntityFactory(new SimpleFactory());
      Level level = getAssetLoader().loadLevel("tmx/idkbackground.tmx", new TMXLevelLoader());
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

        healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(200);
        healthBar.setLayoutX(50);
        healthBar.setLayoutY(50);
        getGameScene().addUINode(healthBar);

        healthBar2 = new ProgressBar(1.0);
        healthBar2.setPrefWidth(200);
        healthBar2.setLayoutX(1600);
        healthBar2.setLayoutY(50);
        getGameScene().addUINode(healthBar2);

        player.getComponent(HealthComponent.class).setHealthChangeListener(this::updateHealthBar);
        updateHealthBar(player.getComponent(HealthComponent.class).getHealth());

        player2.getComponent(HealthComponent.class).setHealthChangeListener(this::updateHealthBarPlayer2);
        updateHealthBarPlayer2(player2.getComponent(HealthComponent.class).getHealth());
    }
    private void updateHealthBar(int newHealth) {
        healthBar.setProgress(newHealth / 100.0);
    }

    private void updateHealthBarPlayer2(int newHealth) {
        healthBar2.setProgress(newHealth / 100.0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}