package com.example.demo;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.geometry.Point2D;

public class PlayerControl2 extends Component {
    private TransformComponent position;
    private PhysicsComponent physics;
    private double speed = 0;
    private AnimatedTexture texture;
    private AnimationChannel animIdle,animWalk,animJump,animPunch, animEnhancedAttack,animUlt;

    private boolean punching = false;
    private double punchTimer = 0;
    private boolean enhancedPunching = false;
    private double enhancedPunchTimer = 0;
    private Duration enhancedPunchCooldown = Duration.seconds(3);

    private int enhancedPunchDamage = 15;


    private Duration punchDuration = Duration.seconds(1); // Keep this consistent
    private boolean damageApplied = false;

    private boolean ultActive = false;
    private double ultDuration = 2;
    private double ultTimer = 0;

    public PlayerControl2(){
        animIdle = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_idle_P2.png"),
                8,1320/8,192, Duration.seconds(1),
                0,7);

        animWalk = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_walk_P2.png"),
                8,1280/8,192, Duration.seconds(1),
                0,7);
        animEnhancedAttack = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_attack_enhanced_p2.png"),
                8,1320/8,192, Duration.seconds(1),
                0,7);

        animJump = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_jump_P2.png"),
                5,825/5,192, Duration.seconds(1),
                0,2);
        // How to know framesPerRow? count how many sprites are there in the png
        // in walking.png there are 10 and divide that value to the width


        animPunch = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_attack_P2.png"),
                8,1320/8,192, Duration.seconds(1),
                0,3);
        animUlt = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_ult_P2.png"),
                8,2376/8,192, Duration.seconds(1),
                0,3);

        // How to know framesPerRow? count how many sprites are there in the png
        // in walking.png there are 10 and divide that value to the width
        texture = new AnimatedTexture(animIdle);
    }
    @Override
    public void onUpdate(double tpf) {
        if (punching) {
            punchTimer -= tpf;

            if (!damageApplied) {
                Entity player1 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER).get(0);

                if (getEntity().getBoundingBoxComponent().isCollidingWith(player1.getBoundingBoxComponent())) {
                    player1.getComponent(HealthComponent.class).takeDamage(10);
                    System.out.println("Player 1 took damage! " + player1.getComponent(HealthComponent.class).getHealth());
                    damageApplied = true;
                }
            }

            if (punchTimer <= 0) {
                punching = false;
            } else {
                return; // Don't change animation while punching
            }
        }

        if (enhancedPunching) {
            enhancedPunchTimer -= tpf;

            if (!damageApplied) {
                Entity player1 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER).get(0);

                if (getEntity().getBoundingBoxComponent().isCollidingWith(player1.getBoundingBoxComponent())) {
                    player1.getComponent(HealthComponent.class).takeDamage(enhancedPunchDamage);
                    System.out.println("Player 1 took " + enhancedPunchDamage + " damage! Remaining health: "
                            + player1.getComponent(HealthComponent.class).getHealth());
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
                Entity player1 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER).get(0);

                if (getEntity().getBoundingBoxComponent().isCollidingWith(player1.getBoundingBoxComponent())) {
                    player1.getComponent(HealthComponent.class).takeDamage(20);
                    System.out.println("Player 1 took 20 damage! Remaining health: "
                            + player1.getComponent(HealthComponent.class).getHealth());
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
        entity.getTransformComponent().setScaleX(1);// temp rani duha since gamay kaayo orig sprite
        entity.getTransformComponent().setScaleY(1);// temp rani duha since gamay kaayo orig sprite
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
        texture.loopAnimationChannel(animIdle);
        entity.getTransformComponent().setScaleX(3);// temp rani duha since gamay kaayo orig sprite
        entity.getTransformComponent().setScaleY(3);// temp rani duha since gamay kaayo orig sprite
    }


    public void P2up() {
        if (isOnGround()) {
            physics.setVelocityY(-300); // Adjust the jump strength as needed
            texture.loopAnimationChannel(animJump);
        }
    }

    public void down() {
        position.translateY(5 * speed);
    }

    public void P2left() {
        texture.setScaleX(1);// flips the view of the entity
        physics.setVelocityX(-100);
        texture.loopAnimationChannel(animWalk);

    }

    public void P2right() {
        texture.setScaleX(-1);// flips the view of the entity
        physics.setVelocityX(100);
        texture.loopAnimationChannel(animWalk);
    }

    public void P2punch() {
        if (!punching) {
            punching = true;
            punchTimer = punchDuration.toSeconds();
            damageApplied = false;
            texture.playAnimationChannel(animPunch);
        }
    }
    public void P2enhancedPunch() {
        if (enhancedPunchTimer <= 0 && !enhancedPunching) {
            enhancedPunching = true;
            enhancedPunchTimer = enhancedPunchCooldown.toSeconds();
            damageApplied = false;
            System.out.println("Enhanced Punch Triggered!");
            texture.playAnimationChannel(animEnhancedAttack);
        }
    }

    public void P2ultAttack() {
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


}
