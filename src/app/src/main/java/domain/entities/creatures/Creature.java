package domain.entities.creatures;

import domain.entities.Entity;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;

public abstract class Creature extends Entity implements Serializable {
    protected int maxHealth = 0;
    protected char[][] map;

    public Creature(int type, char symbol, PositionOnMap position, char[][] map, int gold) {
        super(type, symbol, position);
        this.map = map;
        this.gold = gold;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    protected boolean isValidPosition(int y, int x) {
        return x >= 0 && x < map[0].length && y >= 0 && y < map.length;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public char[][] getMap() {
        return map;
    }
}
