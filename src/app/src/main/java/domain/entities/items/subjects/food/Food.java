package domain.entities.items.subjects.food;

import domain.entities.items.subjects.Subject;
import domain.entities.settings.subjects.ConstFood;
import domain.entities.settings.subjects.ConstTypesSubjects;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;

public class Food extends Subject implements Serializable {

    public Food(PositionOnMap pos) {
        super(ConstTypesSubjects.FOOD.getType(), ConstFood.FOOD.getSubtype(), ConstTypesSubjects.FOOD.getSymbol(), pos, false, ConstFood.FOOD.getName());
        super.health = ConstFood.FOOD.getHealth();
    }
}
