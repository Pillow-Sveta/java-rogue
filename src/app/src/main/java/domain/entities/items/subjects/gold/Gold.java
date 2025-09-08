package domain.entities.items.subjects.gold;


import domain.entities.items.subjects.Subject;
import domain.entities.settings.subjects.ConstGold;
import domain.entities.settings.subjects.ConstTypesSubjects;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;

public class Gold extends Subject implements Serializable {
    public Gold(PositionOnMap pos) {
        super(ConstTypesSubjects.GOLD.getType(), ConstGold.GOLD.getSubtype(), ConstTypesSubjects.GOLD.getSymbol(), pos, false, ConstGold.GOLD.getName());
        super.health = ConstGold.GOLD.getGold();
    }
}
