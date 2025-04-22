package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;



public class CustomIntro extends SubScene {

    public CustomIntro() {
        // Create the text for the intro
        Text introText = new Text("Welcome to Stictactics! 🚀");

        // Set font properties for the text
        introText.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        introText.setFill(Color.ORANGE);

        // Calculate the center of the screen (1920x1080)
        double centerX = 1920 / 2 - introText.getLayoutBounds().getWidth() / 2;
        double centerY = 1080 / 2 - introText.getLayoutBounds().getHeight() / 2;

        // Set the text position to the center of the screen
        introText.setTranslateX(centerX);
        introText.setTranslateY(centerY);

        // Add the text to the scene's root
        getContentRoot().getChildren().add(introText);

        // Create a pause transition to wait for a few seconds before transitioning away
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            // Transition away from the intro screen after the pause
            FXGL.getSceneService().popSubScene();
        });
        pause.play();
    }
}
