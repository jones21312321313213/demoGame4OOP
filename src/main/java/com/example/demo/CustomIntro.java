package com.example.demo;

import com.almasb.fxgl.app.scene.IntroScene;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.io.File;

public class CustomIntro extends IntroScene {

    private MediaPlayer mediaPlayer;

    public CustomIntro() {
        System.out.println("CustomIntro constructor called");
        getContentRoot().setStyle("-fx-background-color: black;");

        try {
            // Load the video file (replace with your actual video path)
            // For development, you might want to put the video in src/main/resources
            String videoPath = getClass().getResource("/assets/textures/intro.mp4").toExternalForm();
            // Or if packaged in a JAR:
            // String videoPath = getClass().getResource("/intro.mp4").toExternalForm();

            Media media = new Media(videoPath);
            mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Set the size to match the game window
            mediaView.setFitWidth(FXGL.getAppWidth());
            mediaView.setFitHeight(FXGL.getAppHeight());

            getContentRoot().getChildren().add(mediaView);

        } catch (Exception e) {
            System.err.println("Error loading video: " + e.getMessage());
            e.printStackTrace();

            // Fallback to text if video fails
            Label label = new Label("Welcome to Stictactics");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            label.setTextFill(Color.WHITE);
            label.setLayoutX(FXGL.getAppWidth() / 2 - 180);
            label.setLayoutY(FXGL.getAppHeight() / 2 - 20);
            getContentRoot().getChildren().add(label);
        }
    }

    @Override
    public void startIntro() {
        System.out.println("[DEBUG] Intro started!");

        if (mediaPlayer != null) {
            mediaPlayer.play();
        }

        // Switch scenes after video duration or fixed time
        PauseTransition delay = new PauseTransition(Duration.seconds(7));
        delay.setOnFinished(event -> {
            System.out.println("[DEBUG] PauseTransition completed - switching scenes");
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                FXGL.getGameController().gotoMainMenu();
            } catch (Exception e) {
                System.err.println("Scene transition failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
        System.out.println("[DEBUG] Starting PauseTransition");
        delay.play();
    }

    @Override
    public void onDestroy() {
        // Clean up media player resources
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }
}