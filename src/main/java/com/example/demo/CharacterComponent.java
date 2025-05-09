package com.example.demo;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimationChannel;

public abstract class CharacterComponent extends Component {
    public abstract AnimationChannel getAnimIdle();
    public abstract AnimationChannel getAnimWalk();
    public abstract AnimationChannel getAnimJump();
    public abstract AnimationChannel getAnimPunch();
    public abstract AnimationChannel getAnimEnhancedAttack();
    public abstract AnimationChannel getAnimUlt();
    public abstract AnimationChannel getAnimHit();
}
