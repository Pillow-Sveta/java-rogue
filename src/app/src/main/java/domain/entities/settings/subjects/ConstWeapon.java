package domain.entities.settings.subjects;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum ConstWeapon {
    MACE(10, "Mace", 11),
    LONG_SWORD(11, "Long sword", 12),
    SHORT_BOW(12, "Short bow", 13),
    DAGGER(13, "Dagger", 14),
    TWO_HANDED_SWORD(14, "Two handed sword", 15);

    private static final Random RANDOM = new Random();
    private final int subtype;
    private final String name;
    private final int strength;

    ConstWeapon(int subtype, String name, int strength) {
        this.subtype = subtype;
        this.name = name;
        this.strength = strength;
    }

    public static ConstWeapon getRandomWeapon() {
        ConstWeapon[] weapons = values();
        return weapons[RANDOM.nextInt(weapons.length)];
    }

    public static List<Integer> getAllSubtypes() {
        return Arrays.stream(values())
                .map(ConstWeapon::getSubtype)
                .collect(Collectors.toList());
    }

    public int getSubtype() {
        return subtype;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }
}
