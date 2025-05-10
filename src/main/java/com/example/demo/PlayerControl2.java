package com.example.demo;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.example.demo.Factory.CharacterFactory;
import com.example.demo.listeners.GameEntityType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PlayerControl2 extends Component {
    private TransformComponent position;
    private PhysicsComponent physics;
    private double speed = 0;
    private AnimatedTexture texture;
    private AnimationChannel animIdle,animWalk,animJump,animPunch, animEnhancedAttack,animUlt,animHit;

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


    private boolean isHit = false;
    private double hitTimer = 0;
    private final double hitDuration = 0.6; // duration in seconds (adjust to match hit animation)


    public PlayerControl2(String charcter){
        CharacterFactory c = new CharacterFactory(charcter);
        animIdle = c.getAnimIdle();
        animWalk = c.getAnimWalk();
        animJump = c.getAnimJump();
        animEnhancedAttack = c.getAnimEnhancedAttack();
        animPunch = c.getAnimPunch();
        animHit = c.getAnimHit();
        animUlt = c.getAnimUlt();
        texture = new AnimatedTexture(animIdle);
    }
    @Override
    public void onUpdate(double tpf) {
        if (isHit) {
            hitTimer -= tpf;
            if (hitTimer <= 0) {
                isHit = false;
                texture.loopAnimationChannel(animIdle);
            }
            return; // skip the rest of update while hit anim is playing
        }

        if (punching) {
            punchTimer -= tpf;
            if (punchTimer <= 0) {
                punching = false;
                texture.loopAnimationChannel(animIdle);
            } else {
                return;
            }
        }

        if (enhancedPunching) {
            enhancedPunchTimer -= tpf;
            if (enhancedPunchTimer <= 0) {
                enhancedPunching = false;
                texture.loopAnimationChannel(animIdle);
            } else {
                return;
            }
        }

        if (ultActive) {
            ultTimer -= tpf;
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
        entity.getTransformComponent().setScaleX(-1);// temp rani duha since gamay kaayo orig sprite
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
        entity.getTransformComponent().setScaleX(-3);// temp rani duha since gamay kaayo orig sprite
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
        // For Player 2, left movement should face left (scaleX positive)
        texture.setScaleX(1); // Face left
        physics.setVelocityX(-100);
        texture.loopAnimationChannel(animWalk);
    }

    public void P2right() {
        // For Player 2, right movement should face right (scaleX negative)
        texture.setScaleX(-1); // Face right
        physics.setVelocityX(100);
        texture.loopAnimationChannel(animWalk);
    }

    public void P2punch() {
        if (!punching) {
            punching = true;
            punchTimer = punchDuration.toSeconds();
            damageApplied = false;
            texture.playAnimationChannel(animPunch);
            spawnHitbox(10, Duration.seconds(0.3),AttackType.NORMAL);
            showHitboxEffect(AttackType.NORMAL);
        }
    }
    public void P2enhancedPunch() {
        if (enhancedPunchTimer <= 0 && !enhancedPunching) {
            enhancedPunching = true;
            enhancedPunchTimer = enhancedPunchCooldown.toSeconds();
            damageApplied = false;
            System.out.println("Enhanced Punch Triggered!");
            texture.playAnimationChannel(animEnhancedAttack);
            spawnHitbox(10, Duration.seconds(0.3),AttackType.ENHANCED);
            showHitboxEffect(AttackType.ENHANCED);
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
        spawnHitbox(10, Duration.seconds(0.3),AttackType.ULTIMATE);
        showHitboxEffect(AttackType.ULTIMATE);
    }

    private void spawnHitbox(int damage, Duration duration, AttackType type) {
        // Attack dimensions based on type
        double reach, boxHeight;
        switch (type) {
            case ULTIMATE:
                reach = 250;
                boxHeight = 60;
                break;
            case ENHANCED:
                reach = 180;
                boxHeight = 50;
                break;
            default: // NORMAL
                reach = 120;
                boxHeight = 40;
        }

        // For Player 2, we need to invert the facing direction check
        boolean facingRight = texture.getScaleX() < 0; // Negative scale means facing right

        // Position calculation
        double hitboxX;
        if (facingRight) {
            // Right-facing: hitbox extends to the right
            hitboxX = entity.getX() + 115 + 15;
        } else {
            // Left-facing: hitbox extends to the left
            hitboxX = entity.getX() + 15 - reach;
        }

        Entity hitbox = FXGL.entityBuilder()
                .type(GameEntityType.HITBOX)
                .at(hitboxX, entity.getY() + 10)
                .bbox(new HitBox(BoundingShape.box(reach, boxHeight)))
                .collidable()
                .with(new HitboxControl(damage, GameEntityType.PLAYER2,type))
                .buildAndAttach();
    }

    private void showHitboxEffect(AttackType type) {
        double reach, boxHeight;
        switch (type) {
            case ULTIMATE:
                reach = 250;
                boxHeight = 60;
                break;
            case ENHANCED:
                reach = 180;
                boxHeight = 50;
                break;
            default:
                reach = 120;
                boxHeight = 40;
        }

        // For Player 2, we need to invert the facing direction check
        boolean facingRight = texture.getScaleX() < 0; // Negative scale means facing right

        Rectangle box = new Rectangle(reach, boxHeight);
        box.setFill(Color.RED.deriveColor(1, 1, 1, type == AttackType.ULTIMATE ? 0.5 : 0.4));

        if (type == AttackType.ULTIMATE) {
            box.setStroke(Color.GOLD);
            box.setStrokeWidth(2);
            box.setArcWidth(15);
            box.setArcHeight(15);
        }

        double hitboxX = facingRight
                ? entity.getX() + 115 + 15
                : entity.getX() + 15 - reach;

        box.setLayoutX(hitboxX);
        box.setLayoutY(entity.getY() + 10);

        FXGL.getGameScene().addUINode(box);
        FXGL.getGameTimer().runOnceAfter(() -> FXGL.getGameScene().removeUINode(box),
                Duration.seconds(type == AttackType.ULTIMATE ? 0.4 :
                        type == AttackType.ENHANCED ? 0.35 : 0.3));
    }

    public void playHitAnimation() {
        if (!isHit) {
            isHit = true;
            hitTimer = hitDuration;
            texture.playAnimationChannel(animHit);
            physics.setVelocityX(0); // optional: stop movement during hit
        }
    }

}
