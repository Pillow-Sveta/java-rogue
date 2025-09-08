package domain.entities.settings.creatures;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum ConstEnemy {
    ZOMBIE('Z', 15, 13, 8, 15, 10, 1),
    VAMPIRE('V', 20, 12, 8, 20, 10, 1),
    GHOST('G', 10, 11, 8, 30, 10, 2),
    OGRE('O', 30, 18, 8, 20, 10, 2),
    SNAKE('S', 12, 19, 8, 40, 10, 3),
    MIMIK('M', 18, 9, 8, 50, 10, 3);

    private static final Random RANDOM = new Random();
    private final char symbol;
    private final int baseHealth;
    private final int dexterity;
    private final int strength;
    private final int cost;
    private final int gold;
    private final int hostility;

    ConstEnemy(char symbol, int baseHealth, int dexterity, int strength, int cost, int gold, int hostility) {
        this.symbol = symbol;
        this.baseHealth = baseHealth;
        this.dexterity = dexterity;
        this.strength = strength;
        this.cost = cost;
        this.gold = gold;
        this.hostility = hostility;
    }

    public static ConstEnemy getRandomEnemy() {
        ConstEnemy[] enemies = values();
        return enemies[RANDOM.nextInt(enemies.length)];
    }

    public static List<Character> getAllSymbols() {
        return Arrays.stream(values())
                .map(ConstEnemy::getSymbol)
                .collect(Collectors.toList());
    }

    public char getSymbol() {
        return symbol;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public int getCost() {
        return cost;
    }

    public int getGold() {
        return gold;
    }

    public int getHostility() {
        return hostility;
    }
}
