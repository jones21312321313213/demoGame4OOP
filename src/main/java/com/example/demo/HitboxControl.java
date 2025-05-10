package com.example.demo;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import com.example.demo.listeners.GameEntityType;
import javafx.util.Duration;


public class HitboxControl extends Component {
    private int damage;
    private Duration duration;
    private Duration damageCooldown;
    private LocalTimer lifetimeTimer;
    private LocalTimer damageTimer;
    private GameEntityType ownerType;
    private AttackType attackType;
    private int hitsRemaining = 1; // Default 1 hit for normal attacks

    public HitboxControl(int damage, GameEntityType ownerType, AttackType attackType) {
        this.damage = damage;
        this.ownerType = ownerType;
        this.attackType = attackType;

        if (attackType == AttackType.ULTIMATE) {
            this.duration = Duration.seconds(1.5); // Shorter duration since we control hits
            this.damageCooldown = Duration.seconds(0.3); // Time between hits
            this.hitsRemaining = 2; // Ultimate hits 2 times
        } else {
            this.duration = Duration.seconds(1);
            this.damageCooldown = Duration.seconds(0.5);
        }
    }

    @Override
    public void onAdded() {
        lifetimeTimer = FXGL.newLocalTimer();
        lifetimeTimer.capture();

        damageTimer = FXGL.newLocalTimer();
        damageTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        if (lifetimeTimer.elapsed(duration)) {
            entity.removeFromWorld();
            return;
        }

        // Skip if not ready to damage again or no hits remaining
        if (!damageTimer.elapsed(damageCooldown) || hitsRemaining <= 0) {
            return;
        }

        GameEntityType targetType = (ownerType == GameEntityType.PLAYER)
                ? GameEntityType.PLAYER2
                : GameEntityType.PLAYER;

        FXGL.getGameWorld()
                .getEntitiesByType(targetType)
                .stream()
                .filter(player -> entity.isColliding(player))
                .findFirst()
                .ifPresent(player -> {
                    HealthComponent health = player.getComponent(HealthComponent.class);
                    if (health != null) {
                        health.takeDamage(damage);
                        System.out.println(targetType + " took " + damage + " damage! (" +
                                hitsRemaining + " hits remaining)");
                        damageTimer.capture();
                        hitsRemaining--;

                        // Remove after last hit or if not ultimate
                        if (hitsRemaining <= 0 || attackType != AttackType.ULTIMATE) {
                            entity.removeFromWorld();
                        }
                    }
                });
    }
}