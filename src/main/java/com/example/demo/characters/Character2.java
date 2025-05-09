package com.example.demo.characters;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class Character2 extends CharacterComponent {
    private AnimationChannel animIdle, animWalk, animJump, animPunch, animEnhancedAttack, animUlt, animHit;
    public Character2(){
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
                0,7);
        animUlt = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_ult_P2.png"),
                8,2376/8,192, Duration.seconds(1),
                0,3);

        animUlt = new AnimationChannel(FXGL.getAssetLoader().loadImage("fighter_hit.png"),
                8,2376/8,192, Duration.seconds(1),
                0,3);

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
