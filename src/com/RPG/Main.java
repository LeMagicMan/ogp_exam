package com.RPG;

import com.RPG.Core.*;
import com.RPG.Exception.*;
import com.RPG.Mechanics.DefaultBattleSystem;

import javax.naming.InvalidNameException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws InvalidDamageTypesException, InvalidNameException, InvalidSkinTypeException, InvalidItemsException, InvalidValueException, InvalidHolderException {
        Hero player1 = new Hero("Dave", 60L, BigDecimal.valueOf(25.00), null);
        Weapon weapon1 = new Weapon(5, player1, AnchorPoint.LEFTHAND, ShineLevel.LEGENDARY, 30);
        Backpack backpack1 = new Backpack(player1, AnchorPoint.BACK);
        Weapon weapon2 = new Weapon(2, null, null, ShineLevel.NONE, 2);
        Weapon weapon3 = new Weapon(10, player1, AnchorPoint.RIGHTHAND, ShineLevel.LOW, 10);

        ArrayList<AnchorPoint> anchorPoints = new ArrayList<>();
        anchorPoints.add(AnchorPoint.BODY);
        anchorPoints.add(AnchorPoint.RIGHTHAND);
        anchorPoints.add(AnchorPoint.BODY);
        anchorPoints.add(AnchorPoint.BACK);
        HashSet<DamageType> damageTypes = new HashSet<>();
        damageTypes.add(DamageType.CLAWS);
        Monster player2 = new Monster("Amaro", 40L, anchorPoints, damageTypes , SkinType.TOUGH);
        Backpack backpack2 = new Backpack(7, 20, 40, player2, AnchorPoint.BACK, ShineLevel.HIGH);

        ArrayList<Item> desiredItems = new ArrayList<>();
        desiredItems.add(backpack2);

        DefaultBattleSystem VATS = new DefaultBattleSystem();
        VATS.combat(player1, player2, desiredItems, player1, true);
    }
}
