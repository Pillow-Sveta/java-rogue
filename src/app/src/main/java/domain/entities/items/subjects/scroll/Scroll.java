package domain.entities.items.subjects.scroll;

import domain.entities.items.subjects.Subject;
import domain.entities.settings.subjects.ConstScroll;
import domain.entities.settings.subjects.ConstTypesSubjects;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;

public class Scroll extends Subject implements Serializable {

    public Scroll(PositionOnMap pos) {
        this(ConstScroll.getRandomScroll(), pos);
    }

    private Scroll(ConstScroll typeScroll, PositionOnMap pos) {
        super(ConstTypesSubjects.SCROLL.getType(), typeScroll.getSubtype(), ConstTypesSubjects.SCROLL.getSymbol(), pos, false, typeScroll.getName());
        super.health = typeScroll.getHealth();
        super.strength = typeScroll.getStrength();
        super.agility = typeScroll.getAgility();
    }
}
