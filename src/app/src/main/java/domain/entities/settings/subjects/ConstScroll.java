package domain.entities.settings.subjects;

import java.util.Random;

public enum ConstScroll {
    HEALTH_SCROLL(5, "Health scroll", 10, 0, 0),
    STRENGTH_SCROLL(6, "Strength scroll", 0, 10, 0),
    AGILITY_SCROLL(7, "Agility scroll", 0, 0, 10),
    CURSED_STRENGTH_SCROLL(8, "Cursed strength scroll", 0, -2, 0),
    CURSED_AGILITY_SCROLL(9, "Cursed agility scroll", 0, 0, -2);

    private static final Random RANDOM = new Random();
    private final int subtype;
    private final String name;
    private final int health;
    private final int strength;
    private final int agility;

    ConstScroll(int subtype, String name, int health, int strength, int agility) {
        this.subtype = subtype;
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.agility = agility;
    }

    public static ConstScroll getRandomScroll() {
        ConstScroll[] scrolls = values();
        return scrolls[RANDOM.nextInt(scrolls.length)];
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
