package domain.entities.settings.subjects;

public enum ConstFood {
    FOOD(4, "FOOD", 10);

    private final int subtype;
    private final String name;
    private final int health;

    ConstFood(int subtype, String name, int health) {
        this.subtype = subtype;
        this.name = name;
        this.health = health;
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
}
