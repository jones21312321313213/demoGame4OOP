package com.example.demo;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PlayerControl extends Component {
    private TransformComponent position;
    private PhysicsComponent physics;
    private double speed = 0;
    private AnimatedTexture texture;
    private AnimationChannel animIdle,animWalk,animJump,animPunch,animHit;

    private boolean punching = false;
    private double punchTimer = 0;
    private Duration punchDuration = Duration.seconds(1); // Keep this consistent
    private boolean damageApplied = false;

    private AnimationChannel animEnhancedAttack; // For enhanced punch animation
    private AnimationChannel animUlt;
    private boolean enhancedPunching = false;
    private double enhancedPunchTimer = 0;
    private Duration enhancedPunchCooldown = Duration.seconds(3);

    private int enhancedPunchDamage = 15;


    private boolean ultActive = false;

    private double ultDuration = 2;
    private double ultTimer = 0;

    private AnimationChannel attackAnim(String path){
        AnimationChannel test = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_jump.png"),
                5,825/5,192, Duration.seconds(1),
                0,3);
        return test;
    }

    public PlayerControl(){
        animIdle = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_idle.png"),
                8,1320/8,192, Duration.seconds(1),
                0,7);

        animWalk = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_walk.png"),
                8,1280/8,192, Duration.seconds(1),
                0,7);

        animJump = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_jump.png"),
                5,825/5,192, Duration.seconds(1),
                0,2);
                // How to know framesPerRow? count how many sprites are there in the png
                // in walking.png there are 10 and divide that value to the width
        animEnhancedAttack = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_attack_enhanced.png"),
                8,1320/8,192, Duration.seconds(1),
                0,7);

        animPunch = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_attack.png"),
                8,1320/8,192, Duration.seconds(1),
                0,7);

        animHit = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_ult.png"),
                8,2376/8,193, Duration.seconds(1),
                0,3);

        animUlt = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_ult.png"),
                8,2376/8,192, Duration.seconds(1),
                0,3);



        texture = new AnimatedTexture(animIdle);
    }
    @Override
    public void onUpdate(double tpf) { // tpf is the time passed since the last frame update

        if (punching) {
            punchTimer -= tpf;

            if (!damageApplied) {
                Entity player2 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER2).get(0);

                if (getEntity().getBoundingBoxComponent().isCollidingWith(player2.getBoundingBoxComponent())) {
                    player2.getComponent(HealthComponent.class).takeDamage(10);
                    System.out.println("Player 2 took damage! " + player2.getComponent(HealthComponent.class).getHealth());
                    damageApplied = true;
                }
            }

            if (punchTimer <= 0) {
                punching = false;
                texture.loopAnimationChannel(animIdle);
            } else {
                return;
            }
        }
        if (enhancedPunching) {
            enhancedPunchTimer -= tpf;

            if (!damageApplied) {
                Entity player2 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER2).get(0);

                if (getEntity().getBoundingBoxComponent().isCollidingWith(player2.getBoundingBoxComponent())) {
                    player2.getComponent(HealthComponent.class).takeDamage(enhancedPunchDamage);
                    System.out.println("Player 2 took " + enhancedPunchDamage + " damage! Remaining health: "
                            + player2.getComponent(HealthComponent.class).getHealth());
                    damageApplied = true;
                }
            }

            if (enhancedPunchTimer <= 0) {
                enhancedPunching = false;
                texture.loopAnimationChannel(animIdle);
            } else {
                return;
            }
        }


        if (ultActive) {
            ultTimer -= tpf;

            if (!damageApplied) {
                Entity player2 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER2).get(0);

                if (getEntity().getBoundingBoxComponent().isCollidingWith(player2.getBoundingBoxComponent())) {
                    player2.getComponent(HealthComponent.class).takeDamage(20); // Deal 20 damage
                    System.out.println("Player 2 took 20 damage! Remaining health: "
                            + player2.getComponent(HealthComponent.class).getHealth());
                    damageApplied = true;
                }
            }

            if (ultTimer <= 0) {
                ultActive = false;
                texture.loopAnimationChannel(animIdle);
            } else {
                return;
            }
        }


        if (!isOnGround()) {
            if (!texture.getAnimationChannel().equals(animJump)) {
                texture.loopAnimationChannel(animJump);
            }
        } else if (isMoving()) {
            if (!texture.getAnimationChannel().equals(animWalk)) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (!texture.getAnimationChannel().equals(animIdle)) {
                texture.loopAnimationChannel(animIdle);
            }
        }

        entity.getTransformComponent().setScaleX(1);
        entity.getTransformComponent().setScaleY(1);
    }


    private boolean isMoving(){
        return FXGLMath.abs(physics.getVelocityX()) > 0;
    }
    private boolean isOnGround() {
        return FXGLMath.abs(physics.getVelocityY()) < 1;
    }


    @Override
    public void onAdded() {
        if (!entity.getViewComponent().getChildren().contains(texture)) {
            entity.getViewComponent().addChild(texture);
        }
        texture.loopAnimationChannel(animIdle); // Ensure idle animation is set
        entity.getTransformComponent().setScaleX(3);
        entity.getTransformComponent().setScaleY(3);
    }


    public void up() {

        if (isOnGround()) {
            physics.setVelocityY(-300);
            texture.loopAnimationChannel(animJump);
        }
    }

    public void down() {
        position.translateY(5 * speed);
    }

    public void left() {
        texture.setScaleX(-1);
        physics.setVelocityX(-100);
        texture.loopAnimationChannel(animWalk);

    }

    public void right() {
        texture.setScaleX(1);
        physics.setVelocityX(100);
        texture.loopAnimationChannel(animWalk);

    }

    public void punch() {
        if (!punching) {
            punching = true;
            punchTimer = punchDuration.toSeconds();
            damageApplied = false;
            texture.playAnimationChannel(animPunch);
        }
    }
    public void enhancedPunch() {
        if (enhancedPunchTimer <= 0 && !enhancedPunching) {
            enhancedPunching = true;
            enhancedPunchTimer = enhancedPunchCooldown.toSeconds();
            damageApplied = false;
            System.out.println("Enhanced Punch Triggered!");
            texture.playAnimationChannel(animEnhancedAttack);
        }
    }


    public void ultAttack() {
        if (ultActive) {
            return;
        }

        System.out.println("Ult Attack Triggered!");

        punching = false;
        enhancedPunching = false;
        texture.stop();
        texture.playAnimationChannel(animUlt);

        ultActive = true;
        ultTimer = ultDuration;
        damageApplied = false;
    }


    public void playHitAnimation() {
        texture.playAnimationChannel(animHit);
    }
}
