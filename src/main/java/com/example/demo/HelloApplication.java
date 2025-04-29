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
import com.almasb.fxgl.ui.FXGLButton;
import com.almasb.fxgl.ui.FXGLCheckBox;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
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
    private int p1Score = 0;
    private int p2Score = 0;
    private HBox p1Star;
    private HBox p2Star;

    private Arc cooldownArc;
    private Text cooldownText;
    private double enhancedPunchCooldownTime = 5.0; // 5 seconds cooldown
    private double remainingCooldownTime = 0.0; // Time left for cooldown
    private boolean isCooldownActive = false;




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
        settings.setIntroEnabled(false);// i true later
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

        getInput().addAction(new UserAction("P1EnhancedPunch") {
            @Override
            protected void onAction() {
                if (!isCooldownActive) { // Only trigger if cooldown is not active
                    player.getComponent(PlayerControl.class).enhancedPunch(); // Trigger the enhanced punch
                    startEnhancedPunchCooldown(); // Start the cooldown for the enhanced punch
                }
            }
        }, KeyCode.E);

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



        cooldownArc = new Arc(100, 100, 50, 50, 90, 0); // X, Y, radiusX, radiusY, startAngle, length
        cooldownArc.setType(ArcType.ROUND);
        cooldownArc.setFill(Color.GREEN);
        cooldownArc.setStroke(Color.GREEN);
        cooldownArc.setStrokeWidth(10);

        cooldownText = new Text(String.format("%.0f", remainingCooldownTime));
        cooldownText.setFont(Font.font(24));
        cooldownText.setFill(Color.BLACK);

        cooldownText.setX(100 - cooldownText.getLayoutBounds().getWidth() / 2);
        cooldownText.setY(100 + cooldownText.getLayoutBounds().getHeight() / 2);

        getGameScene().addUINode(cooldownArc);
        getGameScene().addUINode(cooldownText);

        cooldownArc.setLayoutX(50);
        cooldownArc.setLayoutY(150);
        cooldownText.setLayoutX(50);
        cooldownText.setLayoutY(150);



        p1Star = new HBox(10);
        p1Star.setLayoutX(300);
        p1Star.setLayoutY(300);
        p1Star.setStyle("-fx-background-color: rgba(0, 255, 0, 0.5); -fx-pref-width: 200px; -fx-pref-height: 50px;");
        p1Star.setMinWidth(200);
        p1Star.setMinHeight(100);
        getGameScene().addUINode(p1Star);

        p2Star = new HBox(10);
        p2Star.setLayoutX(300);
        p2Star.setLayoutY(400);
        p2Star.setMinWidth(200);
        p2Star.setMinHeight(100);
        p2Star.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-pref-width: 200px; -fx-pref-height: 50px;");
        getGameScene().addUINode(p2Star);

        player.getComponent(HealthComponent.class).setDeathListener(this::onPlayerDied);
        player2.getComponent(HealthComponent.class).setDeathListener(this::onPlayerDied);

        player.getComponent(HealthComponent.class).setHealthChangeListener(this::updateHealthBar);
        updateHealthBar(player.getComponent(HealthComponent.class).getHealth());

        player2.getComponent(HealthComponent.class).setHealthChangeListener(this::updateHealthBarPlayer2);
        updateHealthBarPlayer2(player2.getComponent(HealthComponent.class).getHealth());

        for (int i = 0; i < p1Score; i++) {
            addStar(p1Star);
        }

        for (int i = 0; i < p2Score; i++) {
            addStar(p2Star);
        }

        p1Star.layout();
        p2Star.layout();

        p1Star.requestLayout();
        p2Star.requestLayout();
    }
    private void updateHealthBar(int newHealth) {
        healthBar.setProgress(newHealth / 100.0);
    }

    private void updateHealthBarPlayer2(int newHealth) {
        healthBar2.setProgress(newHealth / 100.0);
    }

    private void onPlayerDied(GameEntityType deadPlayerType) {
        if (deadPlayerType == GameEntityType.PLAYER) {
            p2Score++; // Player 2 wins a round
        } else if (deadPlayerType == GameEntityType.PLAYER2) {
            p1Score++; // Player 1 wins a round
        }

        if (p1Score >= 3) {
            showGameOverScreen("Player 1 Wins the Match!");
        } else if (p2Score >= 3) {
            showGameOverScreen("Player 2 Wins the Match!");
        } else {
            showRoundWinScreen(deadPlayerType);
        }
    }

    private void showRoundWinScreen(GameEntityType deadPlayerType) {
        FXGL.getGameScene().clearUINodes();


        String winner = (deadPlayerType == GameEntityType.PLAYER) ? "Player 2" : "Player 1";

        var message = FXGL.getUIFactoryService().newText(winner + " wins the round!", 36);
        message.setFill(Color.GREEN);

        FXGLButton nextRoundButton = new FXGLButton("Next Round");
        nextRoundButton.setOnAction(e -> startNextRound());

        var dialogBox = new javafx.scene.layout.VBox(20, message, nextRoundButton);
        dialogBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.90);"
                + "-fx-padding: 30px;");
        dialogBox.setPrefWidth(300);

        double centerX = 1920 / 2.0;
        double centerY = 1080 / 2.0;

        dialogBox.setLayoutX(centerX - 150); // 300/2
        dialogBox.setLayoutY(centerY - 100);

        FXGL.getGameScene().addUINode(dialogBox);

        FXGL.getGameController().pauseEngine();
    }

    private void showGameOverScreen(String winnerText) {
        FXGL.getGameScene().clearUINodes();

        var message = FXGL.getUIFactoryService().newText(winnerText, 48);
        message.setFill(Color.YELLOW);

        FXGLButton restartButton = new FXGLButton("Restart Match");
        restartButton.setOnAction(e -> restartMatch());

        FXGLButton backToMenuButton = new FXGLButton("Back to Menu");
        backToMenuButton.setOnAction(e -> backToMainMenu());

        var dialogBox = new javafx.scene.layout.VBox(20, message, restartButton, backToMenuButton);
        dialogBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.90);"
                + "-fx-padding: 30px;");
        dialogBox.setPrefWidth(400);

        double centerX = 1920 / 2.0;
        double centerY = 1080 / 2.0;

        dialogBox.setLayoutX(centerX - 200); // 400/2
        dialogBox.setLayoutY(centerY - 100);

        FXGL.getGameScene().addUINode(dialogBox);

        FXGL.getGameController().pauseEngine();
    }

    private void backToMainMenu() {
        FXGL.getGameController().resumeEngine();
        FXGL.getGameController().gotoMainMenu();
    }

    private void startNextRound() {
        p1Star.getChildren().clear();
        p2Star.getChildren().clear();

        if (p1Score == 0) {
            addStar(p1Star);
        }
        if (p2Score == 0) {
            addStar(p2Star);
        }

        p1Star.layout();
        p2Star.layout();

        p1Star.requestLayout();
        p2Star.requestLayout();

        FXGL.getGameController().resumeEngine();
        FXGL.getGameController().startNewGame();
    }

    private void restartMatch() {
        p1Score = 0;
        p2Score = 0;
        FXGL.getGameController().resumeEngine();
        FXGL.getGameController().startNewGame();
    }

    private void addStar(HBox starBox) {
        System.out.println("Adding star to " + starBox);  // Debugging line

        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(12);
        circle.setFill(Color.YELLOW);

        Platform.runLater(() -> {
            starBox.getChildren().add(circle);
            System.out.println("Children count: " + starBox.getChildren().size());

            starBox.requestLayout();

            System.out.println("Star added: " + circle);
        });
    }

    private void startEnhancedPunchCooldown() {
        isCooldownActive = true;
        remainingCooldownTime = enhancedPunchCooldownTime;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(enhancedPunchCooldownTime),
                        new KeyValue(cooldownArc.lengthProperty(), 360)
                )
        );
        timeline.setCycleCount(1);
        timeline.play();

        getGameTimer().runAtInterval(() -> {
            if (remainingCooldownTime > 0) {
                remainingCooldownTime -= 1.0; 

                // Update the arc length based on the remaining cooldown time
                double progress = remainingCooldownTime / enhancedPunchCooldownTime;
                cooldownArc.setLength(360 * progress); // Set arc length to show progress

                // Update the text to show the remaining cooldown time
                cooldownText.setText(String.format("%.0f", remainingCooldownTime));

                // Recenter the text in case the text size changes
                cooldownText.setX(100 - cooldownText.getLayoutBounds().getWidth() / 2);
                cooldownText.setY(100 + cooldownText.getLayoutBounds().getHeight() / 2);
            }

            // Once cooldown is finished, reset the cooldown
            if (remainingCooldownTime <= 0) {
                isCooldownActive = false; // Reset cooldown state
            }
        }, Duration.seconds(1)); // Update every second
    }









    public static void main(String[] args) {
        launch(args);
    }

}