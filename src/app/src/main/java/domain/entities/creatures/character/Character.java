package domain.entities.creatures.character;

import datalayer.data.Session;
import domain.entities.creatures.Creature;
import domain.entities.creatures.enemies.Enemy;
import domain.entities.items.inventory.Inventory;
import domain.entities.items.subjects.Subject;
import domain.entities.items.subjects.weapon.Weapon;
import domain.entities.settings.creatures.ConstCharacter;
import domain.entities.settings.creatures.ConstEnemy;
import domain.entities.settings.creatures.ConstTypesCreates;
import domain.entities.settings.subjects.ConstGold;
import domain.entities.settings.subjects.ConstTypesSubjects;
import domain.entities.settings.subjects.ConstWeapon;
import domain.entities.utils.ConstMove;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import static domain.entities.GameConstants.EMPTY;
import static domain.entities.GameConstants.EXIT;
import static java.lang.Math.max;

public class Character extends Creature implements Serializable {
    public Inventory inventory;
    private ArrayList<Enemy> listEnemy;
    private ArrayList<Subject> listItems;
    private int score = 0;
    private boolean lvlFl = false;

    public Character(PositionOnMap pos, char[][] map, ArrayList<Enemy> listEnemy, ArrayList<Subject> listItems) {
        super(ConstTypesCreates.CHARACTER.getType(), ConstCharacter.CHARACTER.getSymbol(), pos, map, 0);
        super.maxHealth = ConstCharacter.CHARACTER.getBaseHealth();
        super.health = ConstCharacter.CHARACTER.getBaseHealth();
        ;
        super.agility = ConstCharacter.CHARACTER.getDexterity();
        super.strength = ConstCharacter.CHARACTER.getStrength();
        this.listEnemy = listEnemy;
        this.listItems = listItems;
        inventory = new Inventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String takeSubject(int y, int x) {
        String status = "";
        Iterator<Subject> iterator = listItems.iterator();
        while (iterator.hasNext()) {
            Subject subject = iterator.next();
            PositionOnMap position = subject.getPosition();
            if (position.getX() == x && position.getY() == y) {
                iterator.remove();
                if (subject.getSubtype() == ConstGold.GOLD.getSubtype()) {
                    this.gold += subject.getGold();
                } else {
                    status = inventory.put(subject);
                    Session.curStats.treasureAmount++;
                }
            }
        }
        return status;  // Если объект не найден
    }

    public String useSubject(int subType) {
        if (!ConstWeapon.getAllSubtypes().contains(subType)) {
            Subject sub = inventory.take(subType);
            if (sub.getTypeOfEntity() == ConstTypesSubjects.ELIXIR.getType()) {
                health = health + sub.getHealth();
                maxHealth = max(health, ConstCharacter.CHARACTER.getBaseHealth());
                Session.curStats.drunk++;
            } else {
                health = Math.min(health + sub.getHealth(), maxHealth);
            }
            if (sub.getTypeOfEntity() == ConstTypesSubjects.FOOD.getType()) Session.curStats.eaten++;
            if (sub.getTypeOfEntity() == ConstTypesSubjects.SCROLL.getType()) Session.curStats.read++;
            strength += sub.getStrength();
            agility += sub.getAgility();
            return "You used " + sub.getName();
        } else {
            return changeWeapon(subType);
        }
    }

    private String changeWeapon(int subType) {
        StringBuilder status = new StringBuilder("You changed ");
        for (Weapon oldWeapon : inventory.getWeapons()) {
            if (oldWeapon.isActiveFl()) {
                strength -= oldWeapon.getStrength();
                oldWeapon.changeActiveFl();
                status.append(oldWeapon.getName());
                break;
            }
        }

        for (Weapon newWeapon : inventory.getWeapons()) {
            if (newWeapon.getSubtype() == subType) {
                strength += newWeapon.getStrength();
                newWeapon.changeActiveFl();
                status.append(" to ").append(newWeapon.getName());
                break;
            }
        }

        return status.toString();
    }

    public String takeOffWeapon() {
        for (Weapon weapon : inventory.getWeapons()) {
            if (weapon.isActiveFl()) {
                strength -= weapon.getStrength();
                weapon.changeActiveFl();
                return weapon.getName();
            }
        }
        return "";
    }

//    public String takeOnWeapon(int subType) {
//        for (Weapon weapon : inventory.getWeapons()) {
//            if (weapon.getSubtype() == subType) {
//                strength += weapon.getStrength();
//                weapon.changeActiveFl();
//                return weapon.getName();
//            }
//        }
//        return "";
//    }

    public String throwSubject(int subType) {
        Subject sub = inventory.take(subType);
        return "You throw away " + sub.getName();
    }

    public String attackEnemy(int y, int x) {
        String status = "";
        // Ищем врага на указанных координатах
        Iterator<Enemy> iterator = listEnemy.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            PositionOnMap enemyPos = enemy.getPosition();
            if (enemyPos.getX() == x && enemyPos.getY() == y) {
                // Рассчитываем шанс попадания (базовый 80% + разница ловкости)
                double hitChance = 0.8 + (this.agility - enemy.getAgility()) * 0.01;
                hitChance = max(0.6, Math.min(0.95, hitChance)); // Ограничиваем диапазон 60%-95%

                if (Math.random() < hitChance) {
                    // Успешная атака - наносим урон
                    int damage = this.strength;
                    enemy.takeDamage(damage);
                    status = "You have successfully attacked the enemy.";
                    Session.curStats.hitMade++;

                    if (enemy.isDead()) {
                        iterator.remove();
                        score += enemy.getCost();
                        gold += enemy.getGold();
                        status = "You killed the enemy";
                        Session.curStats.killed++;
                    }
                } else {
                    status = "You missed";
                }
            }
        }
        return status;
    }

    public String tryMove(ConstMove move) {
        String status = "";
        int newX = position.getX() + move.getDelX();
        int newY = position.getY() + move.getDelY();
        if (isValidPosition(newY, newX)) {
            if (EMPTY.contains(map[newY][newX])) {
                position.setPos(newY, newX);
                status = "";
                if (Session.curStats != null) {
                    Session.curStats.moved++;
                }
            } else if (ConstTypesSubjects.getAllSymbols().contains(map[newY][newX])) {
                status = takeSubject(newY, newX);
                position.setPos(newY, newX);
            } else if (ConstEnemy.getAllSymbols().contains(map[newY][newX])) {
                status = attackEnemy(newY, newX);
            } else if (map[newY][newX] == EXIT) {
                status = "The player found exit";
                changeLevelFl();
            }
        }
        return status;
    }

    public void changeLevelFl() {
        lvlFl = !lvlFl;
    }

    public void changeLevel(ArrayList<Enemy> listEnemy, ArrayList<Subject> listItems, char[][] map) {
        setListEnemy(listEnemy);
        setListItems(listItems);
        setMap(map);
    }

    public boolean isLvlFl() {
        return lvlFl;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setListEnemy(ArrayList<Enemy> listEnemy) {
        this.listEnemy = listEnemy;
    }

    public void setListItems(ArrayList<Subject> listItems) {
        this.listItems = listItems;
    }

    public void setMap(char[][] map) {
        this.map = map;
    }
}
