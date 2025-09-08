package domain.entities.items.subjects.weapon;

import domain.entities.items.subjects.Subject;
import domain.entities.settings.subjects.ConstTypesSubjects;
import domain.entities.settings.subjects.ConstWeapon;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;

public class Weapon extends Subject implements Serializable {
    private boolean activeFl;

    public Weapon(PositionOnMap pos) {
        this(ConstWeapon.getRandomWeapon(), pos);
    }

    private Weapon(ConstWeapon typeWeapon, PositionOnMap pos) {
        super(ConstTypesSubjects.WEAPON.getType(), typeWeapon.getSubtype(), ConstTypesSubjects.WEAPON.getSymbol(), pos, false, typeWeapon.getName());
        super.strength = typeWeapon.getStrength();
        activeFl = false;
    }

    public boolean isActiveFl() {
        return activeFl;
    }

    public void changeActiveFl() {
        activeFl = !activeFl;
    }
}