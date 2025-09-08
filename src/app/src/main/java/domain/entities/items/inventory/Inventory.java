package domain.entities.items.inventory;

import domain.entities.items.subjects.Subject;
import domain.entities.items.subjects.weapon.Weapon;
import domain.entities.settings.subjects.ConstTypesSubjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

import static domain.entities.GameConstants.MAX_COUNT_TYPE_SUBJECTS;
import static domain.entities.GameConstants.MAX_COUNT_TYPE_SUBJECTS_WEAPONS;

public class Inventory implements Serializable {
    private final ArrayList<Subject> inventory;

    public Inventory() {
        this.inventory = new ArrayList<>();
    }

    public ArrayList<Subject> getElixirs() {
        return inventory.stream()
                .filter(subject -> subject.getTypeOfEntity() == ConstTypesSubjects.ELIXIR.getType())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Subject> getScrolls() {
        return inventory.stream()
                .filter(subject -> subject.getTypeOfEntity() == ConstTypesSubjects.SCROLL.getType())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Weapon> getWeapons() {
        return inventory.stream()
                .filter(subject -> subject.getTypeOfEntity() == ConstTypesSubjects.WEAPON.getType())
                .map(subject -> (Weapon) subject)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Subject> getFood() {
        return inventory.stream()
                .filter(subject -> subject.getTypeOfEntity() == ConstTypesSubjects.FOOD.getType())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Subject> getAll() {
        return inventory;
    }

    public Subject take(int subtype) {
        Iterator<Subject> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            Subject subject = iterator.next();
            if (subject.getSubtype() == subtype) {
                iterator.remove();
                return subject;
            }
        }
        return null;
    }


    public String put(Subject sub) {
        String status = "The backpack is too full, so we couldn't take the item.";
        long countTypeSubjects = inventory.stream()
                .filter(type -> type.getTypeOfEntity() == sub.getTypeOfEntity())
                .count();

        if (countTypeSubjects <= MAX_COUNT_TYPE_SUBJECTS) {
            if (sub.getTypeOfEntity() != ConstTypesSubjects.WEAPON.getType() || countTypeSubjects <= MAX_COUNT_TYPE_SUBJECTS_WEAPONS) {
                inventory.add(sub);
                status = "You picked " + sub.getName();
            }
        }
        return status;
    }
}
