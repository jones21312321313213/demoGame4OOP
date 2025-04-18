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
    public PlayerControl(){
        animIdle = new AnimationChannel(FXGL.getAssetLoader().loadImage("idk.png"),
                4,384/4,63, Duration.seconds(1),
                0,3);

        animWalk = new AnimationChannel(FXGL.getAssetLoader().loadImage("walking.png"),
                10, 960/10, 63,
                Duration.seconds(2),
                0, 9);

        animJump = new AnimationChannel(FXGL.getAssetLoader().loadImage("jump.png"),
                4,384/4,63, Duration.seconds(1),
                0,3);
                // How to know framesPerRow? count how many sprites are there in the png
                // in walking.png there are 10 and divide that value to the width
        animPunch = new AnimationChannel(FXGL.getAssetLoader().loadImage("punch.png"),
                3,288/3,63, Duration.seconds(1),
                0,2);

        texture = new AnimatedTexture(animIdle);
    }
    @Override
    public void onUpdate(double tpf) {//tpf is the time passed since the last frame update
        if (punching) {
            punchTimer -= tpf;
            if (punchTimer <= 0) {
                punching = false;
            } else {
                return; // Don't change animation while punching
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

        entity.getTransformComponent().setScaleX(3);
        entity.getTransformComponent().setScaleY(3);
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
        punching = true;
        punchTimer = punchDuration.toSeconds(); // Punch duration in seconds
        texture.playAnimationChannel(animPunch);
    }
}
