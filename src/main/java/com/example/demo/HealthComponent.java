package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.example.demo.listeners.DeathListener;
import com.example.demo.listeners.GameEntityType;
import com.example.demo.listeners.HealthChangeListener;

public class HealthComponent extends Component {
    private int health;
    private HealthChangeListener listener;
    private DeathListener deathListener;

    public HealthComponent(int initialHealth) {
        this.health = initialHealth;
    }


    public void setHealth(int health){
        this.health = health;
    }

    public void takeDamage(int damage) {
        System.out.println("Taking damage");
        health -= damage;
        if (listener != null) {
            listener.onHealthChanged(health);
        }
        if (entity.hasComponent(PlayerControl.class)) {
            entity.getComponent(PlayerControl.class).playHitAnimation();
        }
        if (entity.hasComponent(PlayerControl2.class)) {
            entity.getComponent(PlayerControl2.class).playHitAnimation();
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

        if (deathListener != null) {
            deathListener.onPlayerDied((GameEntityType) entity.getType());
        }

    }

    private void restartGame() {
        FXGL.getGameController().resumeEngine();
        FXGL.getGameController().startNewGame();
    }
    public void setDeathListener(DeathListener deathListener) {
        this.deathListener = deathListener;
    }
}
