package com.example.demo;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;

public class CustomMainMenu extends FXGLMenu {

    public CustomMainMenu(@NotNull MenuType type) {
        super(type);

        // Create and add background firss.d,s.d,
        String videoPath = getClass().getResource("/assets/textures/background-intro.mp4").toExternalForm();

        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop the video
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(FXGL.getAppWidth());
        mediaView.setFitHeight(FXGL.getAppHeight());

        getContentRoot().getChildren().add(mediaView);


        Button playButton = new Button("Play");
//        playButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px;");
//        playButton.setPrefSize(200, 50);
//        playButton.setOnMouseEntered(e -> playButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: white;"));
//        playButton.setOnMouseExited(e -> playButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: black;"));
//        playButton.setOnAction(e -> fireNewGame());

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        shadow.setColor(Color.BLACK);
        playButton.setEffect(shadow);

        ///////////////////////////////

        AnchorPane root = new AnchorPane();

        Button redButton = new Button();
        redButton.setPrefSize(200, 50);
        redButton.setLayoutX(880);
        redButton.setLayoutY(660);
        redButton.setStyle("-fx-background-color: red; -fx-background-radius: 50; -fx-font-size: 24px;");
        redButton.setTextFill(Color.WHITE);
        redButton.setEffect(new ColorAdjust());

        // YELLOW "Start" BUTTON
        Button startButton = new Button("Start");
        startButton.setFont(Font.font(24));
        startButton.setPrefSize(200, 50);
        startButton.setLayoutX(880);
        startButton.setLayoutY(650);
        startButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px;");
        startButton.setTextFill(Color.BLACK);
        startButton.setEffect(new ColorAdjust());

        startButton.setOnMouseEntered(e ->
                startButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: white;"));

        startButton.setOnMouseExited(e ->
                startButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: black;"));

        startButton.setOnAction(e -> fireNewGame());

        /////////////////////////////////

        Button redButton2 = new Button();
        redButton2.setPrefSize(200, 50);
        redButton2.setLayoutX(880);
        redButton2.setLayoutY(760);
        redButton2.setStyle("-fx-background-color: red; -fx-background-radius: 50; -fx-font-size: 24px;");
        redButton2.setTextFill(Color.WHITE);
        redButton2.setEffect(new ColorAdjust());

        Button controlsButton = new Button("Controls");
        controlsButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px;");
        controlsButton.setOnMouseEntered(e -> controlsButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: white;"));
        controlsButton.setOnMouseExited(e -> controlsButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: black;"));
        controlsButton.setPrefSize(200, 50);
        controlsButton.setLayoutX(880);
        controlsButton.setLayoutY(750);
        controlsButton.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo/playerControls.fxml"));
                Parent playerControl = fxmlLoader.load();

                // Option 1: Open in a new window
                Stage controlsStage = new Stage();
                controlsStage.setTitle("Controls");
                controlsStage.setScene(new Scene(playerControl));
                controlsStage.show();

                // Option 2 (Alternative): Replace current scene
                // Stage stage = (Stage) controlsButton.getScene().getWindow();
                // stage.setScene(new Scene(root));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        /////////////////////////////

        Button redButton3 = new Button();
        redButton3.setPrefSize(200, 50);
        redButton3.setLayoutX(880);
        redButton3.setLayoutY(860);
        redButton3.setStyle("-fx-background-color: red; -fx-background-radius: 50; -fx-font-size: 24px;");
        redButton3.setTextFill(Color.WHITE);
        redButton3.setEffect(new ColorAdjust());

        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(200, 50);
        exitButton.setLayoutX(880);
        exitButton.setLayoutY(850);
        exitButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px;");
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: white;"));
        exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: black;"));
        exitButton.setOnAction(e -> fireExit());

        root.getChildren().addAll(redButton,redButton2,redButton3,startButton, controlsButton,exitButton);
        getContentRoot().getChildren().add(root);
    }
}
