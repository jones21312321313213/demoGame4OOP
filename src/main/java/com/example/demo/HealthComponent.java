package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.scene.paint.Color;

public class HealthComponent extends Component {
    private int health;
    private HealthChangeListener listener;

    public HealthComponent(int initialHealth) {
        this.health = initialHealth;
    }

    public void takeDamage(int damage) {
        System.out.println("Taking damage");
        health -= damage;
        if (listener != null) {
            listener.onHealthChanged(health);
        }

        if (health <= 0) {
            health = 0;
            showRetryScreen();

        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealthChangeListener(HealthChangeListener listener) {
        this.listener = listener;
    }
    private void showRetryScreen() {
        FXGL.getGameController().pauseEngine();

        String whoDied = entity.getType() == GameEntityType.PLAYER ? "Player 1" : "Player 2";

        var message = FXGL.getUIFactoryService().newText(whoDied + " Died", 36);
        message.setFill(Color.RED);

        FXGLButton retryButton = new FXGLButton("Retry");
        retryButton.setOnAction(e -> restartGame());

        var dialogBox = new javafx.scene.layout.VBox(20, message, retryButton);
        dialogBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.90);"
                + "-fx-padding: 30px;");
        dialogBox.setPrefWidth(300);

        double centerX = 1920 / 2.0;
        double centerY = 1080 / 2.0;

        dialogBox.setLayoutX(centerX - 150); // 300 / 2
        dialogBox.setLayoutY(centerY - 100);

        FXGL.getGameScene().addUINode(dialogBox);
    }

    private void restartGame() {
        FXGL.getGameController().resumeEngine();
        FXGL.getGameController().startNewGame();
    }
}
