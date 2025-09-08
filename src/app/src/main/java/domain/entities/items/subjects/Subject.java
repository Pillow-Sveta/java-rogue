package domain.entities.items.subjects;

import domain.entities.Entity;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;

public abstract class Subject extends Entity implements Serializable {
    private final int subtype;
    private final String name;
    private boolean inInventory;

    public Subject(int type, int subtype, char symbol, PositionOnMap position, boolean inInventory, String name) {
        super(type, symbol, position);
        this.subtype = subtype;
        this.name = name;
        this.inInventory = inInventory;
    }

    public void putToInventory() {
        inInventory = true;
        position = new PositionOnMap();
    }

    public String getName() {
        return name;
    }

    public boolean isInInventory() {
        return inInventory;
    }

    public int getSubtype() {
        return subtype;
    }
}
