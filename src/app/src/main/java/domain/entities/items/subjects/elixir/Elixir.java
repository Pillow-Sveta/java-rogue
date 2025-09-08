package domain.entities.items.subjects.elixir;

import domain.entities.items.subjects.Subject;
import domain.entities.settings.subjects.ConstElixir;
import domain.entities.settings.subjects.ConstTypesSubjects;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;

public class Elixir extends Subject implements Serializable {

    public Elixir(PositionOnMap pos) {
        this(ConstElixir.getRandomElixir(), pos);
    }

    private Elixir(ConstElixir typeElixir, PositionOnMap pos) {
        super(ConstTypesSubjects.ELIXIR.getType(), typeElixir.getSubtype(), ConstTypesSubjects.ELIXIR.getSymbol(), pos, false, typeElixir.getName());
        super.health = typeElixir.getHealth();
        super.strength = typeElixir.getStrength();
        super.agility = typeElixir.getAgility();
    }
}
