package com.example.demo.characters;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.demo.CharacterComponent;
import javafx.util.Duration;

public class Character1 extends CharacterComponent {
    private AnimationChannel animIdle, animWalk, animJump, animPunch, animEnhancedAttack, animUlt, animHit;

    public Character1() {
        animIdle = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_idle.png"),
                8, 1320 / 8, 192, Duration.seconds(1),
                0, 7);

        animWalk = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_walk.png"),
                8, 1280 / 8, 192, Duration.seconds(1),
                0, 7);

        animJump = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_jump.png"),
                5, 825 / 5, 192, Duration.seconds(1),
                0, 2);

        animEnhancedAttack = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_attack_enhanced.png"),
                8, 1320 / 8, 192, Duration.seconds(1),
                0, 7);

        animPunch = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_attack.png"),
                8, 1320 / 8, 192, Duration.seconds(1),
                0, 7);

        animHit = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_hit.png"),
                8, 2376 / 8, 193, Duration.seconds(1),
                0, 3);

        animUlt = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_ult.png"),
                8, 2376 / 8, 192, Duration.seconds(1),
                0, 3);
    }

    @Override
    public AnimationChannel getAnimIdle() {
        return animIdle;
    }

    @Override
    public AnimationChannel getAnimWalk() {
        return animWalk;
    }

    @Override
    public AnimationChannel getAnimJump() {
        return animJump;
    }

    @Override
    public AnimationChannel getAnimPunch() {
        return animPunch;
    }

    @Override
    public AnimationChannel getAnimEnhancedAttack() {
        return animEnhancedAttack;
    }

    @Override
    public AnimationChannel getAnimUlt() {
        return animUlt;
    }

    @Override
    public AnimationChannel getAnimHit() {
        return animHit;
    }
}
