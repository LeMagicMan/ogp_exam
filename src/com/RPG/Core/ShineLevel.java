package com.RPG.Core;

//TODO: doc
public enum ShineLevel {

    NONE(1F),

    LOW(1.2F),

    MEDIUM(1.5F),

    HIGH(2F),

    LEGENDARY(3F);

    private float valueMultiplier;

    private ShineLevel(float valueMultiplier) {
        this.valueMultiplier = valueMultiplier;
    }

    public float getValueMultiplier() {
        return valueMultiplier;
    }
}
