package domain.entities.settings.creatures;

public enum ConstTypesCreates {
    CHARACTER(1),
    ENEMY(2);

    private final int type;

    ConstTypesCreates(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
