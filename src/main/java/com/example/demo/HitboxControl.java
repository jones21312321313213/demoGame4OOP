package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

public class HitboxControl extends Component {
    private int damage;
    private LocalTimer timer;
    private Duration duration;

    public HitboxControl(int damage, Duration duration) {
        this.damage = damage;
        this.duration = duration;
    }

    @Override
    public void onAdded() {
        timer = FXGL.newLocalTimer();
        timer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        if (timer.elapsed(duration)) {
            entity.removeFromWorld();
            return;
        }

        // Check for collisions with PLAYER2
        FXGL.getGameWorld()
                .getEntitiesByType(GameEntityType.PLAYER2)
                .stream()
                .filter(player2 -> entity.isColliding(player2))
                .forEach(player2 -> {
                    HealthComponent health = player2.getComponent(HealthComponent.class);
                    if (health != null) {
                        health.takeDamage(damage);
                        System.out.println("Player 2 took " + damage + " damage!");
                    }
                    entity.removeFromWorld();
                });
    }
}