package com.example.demo;

import com.almasb.fxgl.texture.AnimationChannel;
import com.example.demo.characters.Character1;
import com.example.demo.characters.Character2;

public class CharacterFactory {
    private final CharacterComponent character;

    public CharacterFactory(String characterName) {
        switch (characterName.toLowerCase()) {
            case "character1":
                character = new Character1();
                break;
            case "character2":
                character = new Character2();
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
