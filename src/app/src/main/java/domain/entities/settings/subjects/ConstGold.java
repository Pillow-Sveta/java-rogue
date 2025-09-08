package domain.entities.settings.subjects;

public enum ConstGold {
    GOLD(15, "GOLD", 30);

    private final int subtype;
    private final String name;
    private final int gold;

    ConstGold(int subtype, String name, int gold) {
        this.subtype = subtype;
        this.name = name;
        this.gold = gold;
    }

    public int getSubtype() {
        return subtype;
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }
}
