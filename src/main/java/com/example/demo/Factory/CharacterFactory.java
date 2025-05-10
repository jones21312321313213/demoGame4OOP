package com.example.demo.Factory;

import com.almasb.fxgl.texture.AnimationChannel;
import com.example.demo.characters.*;

public class CharacterFactory {
    private final CharacterComponent character;

    public CharacterFactory(String characterName) {
        switch (characterName.toLowerCase()) {
            case "male":
                character = new Male();
                break;
            case "elite":
                character = new Elite();
                break;
            case "knight":
                character = new Knight();
                break;
            case "pirate":
                character = new Pirate();
                break;
            case "ninja":
                character = new Ninja();
                break;
            case "female":
                character = new Female();
                break;
            default:
                throw new IllegalArgumentException("Unknown character: " + characterName);
        }
    }

    public AnimationChannel getAnimIdle() {
        return character.getAnimIdle();
    }

    public AnimationChannel getAnimWalk() {
        return character.getAnimWalk();
    }

    public AnimationChannel getAnimJump() {
        return character.getAnimJump();
    }

    public AnimationChannel getAnimPunch() {
        return character.getAnimPunch();
    }

    public AnimationChannel getAnimEnhancedAttack() {
        return character.getAnimEnhancedAttack();
    }

    public AnimationChannel getAnimHit() {
        return character.getAnimHit();
    }

    public AnimationChannel getAnimUlt() {
        return character.getAnimUlt();
    }
}
