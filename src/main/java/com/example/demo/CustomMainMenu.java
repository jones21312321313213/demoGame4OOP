package com.example.demo;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public class CustomMainMenu extends FXGLMenu {

    public CustomMainMenu(@NotNull MenuType type) {
        super(type);

        // Create and add background firss.d,s.d,
        var bg = FXGL.texture("mainmenu.png");
        bg.setFitWidth(FXGL.getAppWidth());
        bg.setFitHeight(FXGL.getAppHeight());
        getContentRoot().getChildren().add(bg);

        // Then overlay UI elements
        VBox content = new VBox(15);
        content.setTranslateX(900);
        content.setTranslateY(600);

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px;");
        playButton.setPrefSize(200, 50);
        playButton.setOnMouseEntered(e -> playButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: white;"));
        playButton.setOnMouseExited(e -> playButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: black;"));
        playButton.setOnAction(e -> fireNewGame());

        Button controlsButton = new Button("Controls");
        controlsButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px;");
        controlsButton.setOnMouseEntered(e -> controlsButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: white;"));
        controlsButton.setOnMouseExited(e -> controlsButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: black;"));
        controlsButton.setPrefSize(200, 50);
        controlsButton.setOnAction(e -> {
            String controlsInfo = "Player 1:\n" +
                    "  Move Left: A\n" +
                    "  Move Right: D\n" +
                    "  Jump: W\n" +
                    "  Punch: F\n\n" +
                    "Player 2:\n" +
                    "  Move Left: J\n" +
                    "  Move Right: L\n" +
                    "  Jump: I\n" +
                    "  Punch: H";
            FXGL.getDialogService().showMessageBox(controlsInfo);
        });

        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(200, 50);
        exitButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px;");
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: white;"));
        exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50; -fx-font-size: 24px; -fx-text-fill: black;"));
        exitButton.setOnAction(e -> fireExit());

        content.getChildren().addAll(playButton, controlsButton, exitButton);

        getContentRoot().getChildren().add(content);
    }
}
