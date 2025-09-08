package domain.entities.settings.subjects;

import java.util.Random;

public enum ConstElixir {
    ELIXIR_HEALTH(1, "Health elixir", 10, 0, 0),
    ELIXIR_STRENGHT(2, "Strength elixir", 0, 10, 0),
    ELIXIR_AGILITY(3, "Agility elixir", 0, 0, 10);

    private static final Random RANDOM = new Random();
    private final int subtype;
    private final String name;
    private final int health;
    private final int strength;
    private final int agility;

    ConstElixir(int subtype, String name, int health, int strength, int agility) {
        this.subtype = subtype;
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.agility = agility;
    }

    public static ConstElixir getRandomElixir() {
        ConstElixir[] elixir = values();
        return elixir[RANDOM.nextInt(elixir.length)];
    }

    public int getSubtype() {
        return subtype;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
    }
}
