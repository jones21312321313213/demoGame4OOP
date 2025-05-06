package com.example.demo;

import javafx.event.Event;
import javafx.event.EventType;

public class UltDamageEvent extends Event {

    public static final EventType<UltDamageEvent> ANY =
            new EventType<>(Event.ANY, "ULT_DAMAGE");

    private final double damage;

    public UltDamageEvent(double damage) {
        super(ANY);
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }
}
