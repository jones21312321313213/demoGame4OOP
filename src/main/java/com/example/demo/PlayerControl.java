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
    private AnimationChannel animIdle,animWalk,animJump,animPunch;

    private boolean punching = false;
    private double punchTimer = 0;
    private Duration punchDuration = Duration.seconds(1); // Keep this consistent
    private boolean damageApplied = false;

    private AnimationChannel animEnhancedAttack; // For enhanced punch animation
    private boolean enhancedPunching = false;
    private double enhancedPunchTimer = 0;
    private Duration enhancedPunchCooldown = Duration.seconds(3);
    private int enhancedPunchDamage = 15;

    private Text enhancedPunchCooldownText = new Text();

    public PlayerControl(){
        animIdle = new AnimationChannel(FXGL.getAssetLoader().loadImage("p1_idle.png"),
                8,16384/8,512, Duration.seconds(1),
                0,3);

        animWalk = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_walk.png"),
                8,1280/8,192, Duration.seconds(1),
                0,3);

        animJump = new AnimationChannel(FXGL.getAssetLoader().loadImage("jump.png"),
                4,384/4,63, Duration.seconds(1),
                0,3);
                // How to know framesPerRow? count how many sprites are there in the png
                // in walking.png there are 10 and divide that value to the width
        animEnhancedAttack = new AnimationChannel(FXGL.getAssetLoader().loadImage("punch.png"),
                3,288/3,63, Duration.seconds(1),
                0,2);

        animPunch = new AnimationChannel(FXGL.getAssetLoader().loadImage("punch.png"),
                3,288/3,63, Duration.seconds(1),
                0,2);



        texture = new AnimatedTexture(animIdle);
    }
    @Override
    public void onUpdate(double tpf) { // tpf is the time passed since the last frame update

        enhancedPunchCooldownText.setText("Enhanced Punch Cooldown: " + Math.max(0, Math.ceil(enhancedPunchTimer)) + "s");

        if (punching) {
            punchTimer -= tpf;

            if (!damageApplied) {
                Entity player2 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER2).get(0);

                if (getEntity().getBoundingBoxComponent().isCollidingWith(player2.getBoundingBoxComponent())) {
                    player2.getComponent(HealthComponent.class).takeDamage(100);
                    System.out.println("Player 2 took damage! " + player2.getComponent(HealthComponent.class).getHealth());
                    damageApplied = true;
                }
            }

            if (punchTimer <= 0) {
                punching = false;
                texture.loopAnimationChannel(animIdle);
            } else {
                return; // Don't change animation while punching
            }
        }

        // Enhanced Punching Logic
        if (enhancedPunching) {
            enhancedPunchTimer -= tpf;

            // Handle damage for enhanced punch while it is active
            if (!damageApplied) {
                Entity player2 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER2).get(0);

                // Check for collision to apply enhanced punch damage
                if (getEntity().getBoundingBoxComponent().isCollidingWith(player2.getBoundingBoxComponent())) {
                    player2.getComponent(HealthComponent.class).takeDamage(enhancedPunchDamage);
                    System.out.println("Player 2 took " + enhancedPunchDamage + " damage! Remaining health: "
                            + player2.getComponent(HealthComponent.class).getHealth());
                    damageApplied = true;
                }
            }

            // Once the enhanced punch cooldown is over, stop enhanced punch and revert to idle
            if (enhancedPunchTimer <= 0) {
                enhancedPunching = false; // Reset after cooldown
                texture.loopAnimationChannel(animIdle); // Return to idle animation after cooldown
            } else {
                return; // Don't change animation while enhanced punch is active
            }
        }

        // Default Movement and Idle Animations
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
        texture.setScaleX(-1);// flips the view of the entity
        physics.setVelocityX(-100);
        texture.loopAnimationChannel(animWalk);

    }

    public void right() {
        texture.setScaleX(1);// flips the view of the entity
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
            enhancedPunchTimer = enhancedPunchCooldown.toSeconds(); // Reset the cooldown for the enhanced punch
            damageApplied = false;

            // Debugging: Ensure this line is called
            System.out.println("Enhanced Punch Triggered!");

            // Play the enhanced punch animation
            texture.playAnimationChannel(animPunch);

            // Apply damage for the enhanced punch
            //dealDamage(enhancedPunchDamage);

            // Optional: Add any special effects or visual enhancements here
        }
    }



    private void dealDamage(int damage) {
        Entity player2 = FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER2).get(0);
        if (getEntity().getBoundingBoxComponent().isCollidingWith(player2.getBoundingBoxComponent())) {
            player2.getComponent(HealthComponent.class).takeDamage(damage);
            System.out.println("Player 2 took " + damage + " damage! Remaining health: "
                    + player2.getComponent(HealthComponent.class).getHealth());
        }
    }
}
