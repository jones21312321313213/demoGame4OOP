package com.example.demo.Scenes;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.example.demo.*;
import com.example.demo.listeners.GameEntityType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class CustomPauseMenu extends FXGLMenu {
    private final MatchTimer matchTimer; //
    private ArrayList<GameState> gameStateList = new ArrayList<>();
    private boolean saved = false;
    public CustomPauseMenu(MenuType type, MatchTimer matchTimer, ArrayList<GameState> gameStateList) {
        super(type);
        this.matchTimer = matchTimer;
        this.gameStateList = gameStateList;
        AnchorPane root = new AnchorPane();

        // Resume Button
        Button resumeButton = new Button("Resume");
        resumeButton.setFont(Font.font(24));
        resumeButton.setPrefSize(200, 50);
        resumeButton.setLayoutX(880);
        resumeButton.setLayoutY(650);
        resumeButton.setStyle("-fx-background-color: yellow; -fx-background-radius: 50;");
        resumeButton.setOnAction(e -> {
            if (matchTimer != null) {
                matchTimer.resume();
            }
            FXGL.getSceneService().popSubScene();
        });

        // Exit Button
        Button exitButton = new Button("Exit to Main Menu");
        exitButton.setFont(Font.font(24));
        exitButton.setPrefSize(200, 50);
        exitButton.setLayoutX(880);
        exitButton.setLayoutY(750);
        exitButton.setStyle("-fx-background-color: orange; -fx-background-radius: 50;");
        exitButton.setOnAction(e -> {
            if(!saved){
                RoundStateManager.getInstance().setStartOfRound(true);
            }
            fireExitToMainMenu();

        });

        // Save Button (Added)
        Button saveButton = new Button("Save Game");
        saveButton.setFont(Font.font(24));
        saveButton.setPrefSize(200, 50);
        saveButton.setLayoutX(880);
        saveButton.setLayoutY(850);  // Adjust position below the Exit button
        saveButton.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 50;");
        saveButton.setOnAction(e -> {
            fireSaveGame(matchTimer);
            RoundStateManager.getInstance().setStartOfRound(false);
            saved = true;
        });

        // Adding buttons to the root pane
        root.getChildren().addAll(resumeButton, exitButton, saveButton);
        getContentRoot().getChildren().add(root);
    }

    // Add your save game functionality here (stub for now)
    private void fireSaveGame(MatchTimer matchTimer) {
        try {
            // Fetch game data from FXGL variables
            int timeLeft = matchTimer.getTimeLeft();

//            String player1Name = FXGL.gets("player1Name");
//            String player2Name = FXGL.gets("player2Name");

            String player1Name = "Heloo ";
            String player2Name = " World";
//            int player1Health = FXGL.geti("player1Health");
//            int player2Health = FXGL.geti("player2Health");



//            int player1Score = FXGL.geti("player1Score");
//            int player2Score = FXGL.geti("player2Score");
            int player1Score = 123445;
            int player2Score = 1122;


//            String map = FXGL.gets("currentMap");
            String map =  "idk";

            Entity player1 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER).get(0);
            Entity player2 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER2).get(0);

            double x1 = 0.0;
            double y1 = 0.0;
            double x2 = 0.0;
            double y2 = 0.0;

            if (player1 != null) {
                x1 = player1.getX();
                y1 = player1.getY();
            }
            if (player2 != null) {
                x2 = player2.getX();
                y2 = player2.getY();
            }

            //          String player1Name = "player1";
//            String player2Name = "player2";
            int player1Health =   player1.getComponent(HealthComponent.class).getHealth();
            int player2Health = player2.getComponent(HealthComponent.class).getHealth();


            GameState gameState = new GameState(
                    timeLeft,
                    player1Health,
                    player2Health,
                    player1Name,
                    player2Name,
                    player1Score,
                    player2Score,
                    x1,
                    y1,
                    x2,
                    y2,
                    map
            );

            // Add to the front of the list (most recent first)
            gameStateList.add(gameState);

            while (gameStateList.size() > 5) {
                gameStateList.remove(0);  // Remove oldest
            }

            // Serialize and save the list to a file
            try (FileOutputStream fileOut = new FileOutputStream("savedstates.txt");
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(gameStateList);
                System.out.println("Game state saved. Total saved states: " + gameStateList.size());
                System.out.println(x1 + " "+ y1 + " " + x2 +" " + y2);
                System.out.println(player1Health + " " + player2Health);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
