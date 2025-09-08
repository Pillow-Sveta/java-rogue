package domain.entities;

import domain.entities.utils.PositionOnMap;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    protected PositionOnMap position;
    protected char symbol;
    protected int health = 0;
    protected int strength = 0;
    protected int agility = 0;
    protected int gold = 0;
    private int typeOfEntity;

    public Entity(int type, char symbol, PositionOnMap position) {
        this.typeOfEntity = type;
        this.symbol = symbol;
        this.position = position;
    }

    public int getTypeOfEntity() {
        return typeOfEntity;
    }

    public void setTypeOfEntity(int typeOfEntity) {
        this.typeOfEntity = typeOfEntity;
    }

    public PositionOnMap getPosition() {
        return position;
    }

    public void setPosition(PositionOnMap position) {
        this.position = position;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getGold() {
        return gold;
    }
}
