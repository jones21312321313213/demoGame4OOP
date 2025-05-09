package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import com.example.demo.listeners.GameEntityType;
import javafx.util.Duration;

public class HitboxControl extends Component {
    private int damage;
    private LocalTimer timer;
    private Duration duration;
    private GameEntityType ownerType; // Who spawned this hitbox?

    public HitboxControl(int damage, Duration duration, GameEntityType ownerType) {
        this.damage = damage;
        this.duration = duration;
        this.ownerType = ownerType;
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

        GameEntityType targetType = (ownerType == GameEntityType.PLAYER)
                ? GameEntityType.PLAYER2
                : GameEntityType.PLAYER;

        FXGL.getGameWorld()
                .getEntitiesByType(targetType)
                .stream()
                .filter(player -> entity.isColliding(player))
                .forEach(player -> {
                    HealthComponent health = player.getComponent(HealthComponent.class);
                    if (health != null) {
                        health.takeDamage(damage);
                        System.out.println(targetType + " took " + damage + " damage!");
                    }
                    entity.removeFromWorld();
                });
    }
}
