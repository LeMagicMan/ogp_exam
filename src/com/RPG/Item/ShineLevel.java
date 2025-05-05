package com.RPG.Item;

//TODO: doc
public enum ShineLevel {

    NONE(1F),

    LOW(1F),

    MEDIUM(1F),

    HIGH(1F),

    LEGENDARY(1F);

    private float valueMultiplier;

    private ShineLevel(float valueMultiplier) {
        this.valueMultiplier = valueMultiplier;
    }

    public float getValueMultiplier() {
        return valueMultiplier;
    }
}
