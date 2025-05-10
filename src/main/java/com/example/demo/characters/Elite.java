package com.example.demo.characters;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class Elite extends CharacterComponent {
    private AnimationChannel animIdle, animWalk, animJump, animPunch, animEnhancedAttack, animUlt, animHit;
    public Elite(){
        animIdle = new AnimationChannel(FXGL.getAssetLoader().loadImage("elite/elite_idle.png"),
                7,1155/7,192, Duration.seconds(1),
                0,6);

        animWalk = new AnimationChannel(FXGL.getAssetLoader().loadImage("elite/elite_walk.png"),
                8,1320/8,192, Duration.seconds(1),
                0,7);
        animEnhancedAttack = new AnimationChannel(FXGL.getAssetLoader().loadImage("elite/elite_attack_enhanced.png"),
                8,1320/8,195, Duration.seconds(1),
                0,7);

        animJump = new AnimationChannel(FXGL.getAssetLoader().loadImage("elite/elite_jump.png"),
                5,825/5,192, Duration.seconds(1),
                0,4);
        // How to know framesPerRow? count how many sprites are there in the png
        // in walking.png there are 10 and divide that value to the width

        animPunch = new AnimationChannel(FXGL.getAssetLoader().loadImage("elite/elite_attack.png"),
                8,1320/8,192, Duration.seconds(1),
                0,7);
        animUlt = new AnimationChannel(FXGL.getAssetLoader().loadImage("elite/elite_ult.png"),
                8,2376/8,195, Duration.seconds(1),
                0,7);

        animHit = new AnimationChannel(FXGL.getAssetLoader().loadImage("elite/elite_hit.png"),
                4, 660 / 4, 192, Duration.seconds(1),
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
