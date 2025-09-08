package domain.entities.settings.creatures;

public enum ConstCharacter {
    CHARACTER('Y', 30, 30, 30);

    private final char symbol;
    private final int baseHealth;
    private final int dexterity;
    private final int strength;

    ConstCharacter(char symbol, int baseHealth, int dexterity, int strength) {
        this.symbol = symbol;
        this.baseHealth = baseHealth;
        this.dexterity = dexterity;
        this.strength = strength;
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
}
