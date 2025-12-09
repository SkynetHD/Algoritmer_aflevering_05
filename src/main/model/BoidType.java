package main.model;

import main.behavior.BehaviorStrategy;
import main.behavior.FlockBehavior;
import main.behavior.RandomBehavior;

import java.awt.Color;

public enum BoidType {
    STANDARD(Color.WHITE, new FlockBehavior()),
    RANDOM(Color.YELLOW, new RandomBehavior());

    private final Color color;
    private final BehaviorStrategy defaultBehavior;

    BoidType(Color color, BehaviorStrategy defaultBehavior) {
        this.color = color;
        this.defaultBehavior = defaultBehavior;
    }

    public Color getColor() {
        return color;
    }

    public BehaviorStrategy getDefaultBehavior() {
        return defaultBehavior;
    }
}