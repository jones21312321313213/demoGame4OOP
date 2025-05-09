package com.example.demo.Scenes;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.example.demo.MatchTimer;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CustomPauseMenu extends FXGLMenu {
    private final MatchTimer matchTimer; //
    public CustomPauseMenu(MenuType type, MatchTimer matchTimer) {
        super(type);
        this.matchTimer = matchTimer;
        AnchorPane root = new AnchorPane();

        // Resume Button
        Button resumeButton = new Button("Resume");
        resumeButton.setFont(Font.font(24));
        resumeButton.setPrefSize(200, 50);
        resumeButton.setLayoutX(880);
        resumeButton.setLayoutY(650);
        resumeButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50;");
        resumeButton.setOnAction(e -> {
            matchTimer.resume(); // Now accessible
            FXGL.getSceneService().popSubScene(); // Remove pause menu
        });

        // Exit Button
        Button exitButton = new Button("Exit to Main Menu");
        exitButton.setFont(Font.font(24));
        exitButton.setPrefSize(200, 50);
        exitButton.setLayoutX(880);
        exitButton.setLayoutY(750);
        exitButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50;");
        exitButton.setOnAction(e -> fireExitToMainMenu());

        // Save Button (Added)
        Button saveButton = new Button("Save Game");
        saveButton.setFont(Font.font(24));
        saveButton.setPrefSize(200, 50);
        saveButton.setLayoutX(880);
        saveButton.setLayoutY(850);  // Adjust position below the Exit button
        saveButton.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 50;");
        saveButton.setOnAction(e -> fireSaveGame());

        // Adding buttons to the root pane
        root.getChildren().addAll(resumeButton, exitButton, saveButton);
        getContentRoot().getChildren().add(root);
    }

    // Add your save game functionality here (stub for now)
    private void fireSaveGame() {
        // Implement your save game logic here
        System.out.println("Game Saved!"); // Placeholder message
    }
}
