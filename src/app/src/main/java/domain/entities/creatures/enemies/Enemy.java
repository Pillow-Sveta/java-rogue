package domain.entities.creatures.enemies;

import datalayer.data.Session;
import domain.entities.creatures.Creature;
import domain.entities.creatures.character.Character;
import domain.entities.settings.creatures.ConstEnemy;
import domain.entities.settings.creatures.ConstTypesCreates;
import domain.entities.settings.subjects.ConstTypesSubjects;
import domain.entities.utils.ConstMove;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static domain.entities.GameConstants.EMPTY;

public class Enemy extends Creature implements Serializable {
    private final int cost;
    private final int hostility; // 1-3: 1 - минимальная, 3 - максимальная агрессия
    Character character;
    private char last_monster = ' ';
    private boolean ghostFl;
    private boolean mimikFl;
    private char symbolForMimik;

    public Enemy(PositionOnMap pos, char[][] map, Character character) {
        this(ConstEnemy.getRandomEnemy(), pos, map, character);
    }

    protected Enemy(ConstEnemy enemyType, PositionOnMap pos, char[][] map, Character character) {
        super(ConstTypesCreates.ENEMY.getType(), enemyType.getSymbol(), pos, map, enemyType.getGold());
        super.maxHealth = enemyType.getBaseHealth();
        super.health = enemyType.getBaseHealth();
        super.agility = enemyType.getDexterity();
        super.strength = enemyType.getStrength();
        super.symbol = enemyType.getSymbol();
        this.cost = enemyType.getCost();
        this.character = character;
        this.hostility = enemyType.getHostility();
        ghostFl = enemyType.getSymbol() == ConstEnemy.GHOST.getSymbol();
        mimikFl = enemyType.getSymbol() == ConstEnemy.MIMIK.getSymbol();
        if (this.mimikFl)
            symbolForMimik = getRandomSymbolForMimik();
        else
            symbolForMimik = enemyType.getSymbol();

    }

    public String tryMove() {
        int currentX = position.getX();
        int currentY = position.getY();
        char characterSymbol = character.getSymbol();
        String status = "";
        boolean heroFound = false;

        // 1. Сначала проверяем ближайшие клетки (атака)
        if (checkForHero(currentX, currentY, 1, false)) {
            return attackCharacter();
        }

        // 2. Проверяем зону видимости в зависимости от враждебности (преследование)
        for (int radius = 1; radius <= hostility && !heroFound; radius++) {
            heroFound = checkForHero(currentX, currentY, radius, true);
        }

        if (heroFound) {
            status = moveTowards(character.getPosition().getY(), character.getPosition().getX());
        } else if (symbol != ConstEnemy.MIMIK.getSymbol()) {
            status = randomMove();
        }

        return status;
    }


    private boolean checkForHero(int currentX, int currentY, int radius, boolean includeDiagonals) {
        // Для радиуса 1 всегда проверяем только прямые направления (атака)
        if (radius == 1) {
            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            return Arrays.stream(directions)
                    .anyMatch(dir -> checkHeroAtPosition(currentX + dir[0], currentY + dir[1]));
        }

        // Для преследования учитываем диагонали если требуется
        List<int[]> directions = includeDiagonals ?
                getAllDirections(radius) :
                getStraightDirections(radius);

        return directions.stream()
                .anyMatch(dir -> checkHeroAtPosition(currentX + dir[0], currentY + dir[1]));
    }

    private boolean checkHeroAtPosition(int x, int y) {
        return isValidPosition(y, x) && map[y][x] == character.getSymbol();
    }

    private List<int[]> getStraightDirections(int radius) {
        return Arrays.asList(
                new int[]{0, radius},  // вверх
                new int[]{0, -radius}, // вниз
                new int[]{radius, 0},  // вправо
                new int[]{-radius, 0}  // влево
        );
    }

    private List<int[]> getAllDirections(int radius) {
        return Arrays.stream(ConstMove.values())
                .map(move -> new int[]{move.getDelX() * radius, move.getDelY() * radius})
                .collect(Collectors.toList());
    }

    private String moveTowards(int targetY, int targetX) {
        int currentX = position.getX();
        int currentY = position.getY();

        // Сначала пытаемся двигаться по прямой к цели
        int preferredMoveX = Integer.compare(targetX, currentX);
        int preferredMoveY = Integer.compare(targetY, currentY);

        if (preferredMoveX != 0) {
            int newX = currentX + preferredMoveX;
            int newY = currentY;
            if (canMoveTo(newY, newX)) {
                position.setPos(newY, newX);
                return "";
            }
        }

        if (preferredMoveY != 0) {
            int newX = currentX;
            int newY = currentY + preferredMoveY;
            if (canMoveTo(newY, newX)) {
                position.setPos(newY, newX);
                return "";
            }
        }

        // Если прямой путь заблокирован, пробуем обходные пути
        for (ConstMove move : ConstMove.getShuffledMoves()) {
            int newX = currentX + move.getDelX();
            int newY = currentY + move.getDelY();
            if (canMoveTo(newY, newX)) {
                position.setPos(newY, newX);
                return "";
            }
        }
        return "";
    }

    private boolean canMoveTo(int y, int x) {
        return isValidPosition(y, x) && EMPTY.contains(map[y][x]);
    }

    private String randomMove() {
        int currentX = position.getX();
        int currentY = position.getY();

        for (ConstMove move : ConstMove.getShuffledMoves()) {
            int newX = currentX + move.getDelX();
            int newY = currentY + move.getDelY();

            if (canMoveTo(newY, newX)) {
                position.setPos(newY, newX);
                return "";
            }
        }

        return "";
    }

    public String attackCharacter() {
        if (!hasDirectLineOfSight()) {
            return "The enemy can't attack";
        }

        String status;
        boolean hit = Math.random() * 100 > (double) (character.getAgility() - this.agility) / 2;

        if (hit) {
            character.takeDamage(this.strength);
            if (character.isDead()) {
                this.last_monster = this.getSymbol();
            }
            status = "The enemy attacked you";
            Session.curStats.hitGot++;
        } else {
            status = "The enemy tried to attack you, but missed.";
        }
        return status;
    }

    private boolean hasDirectLineOfSight() {
        int dx = character.getPosition().getX() - position.getX();
        int dy = character.getPosition().getY() - position.getY();
        return dx == 0 || dy == 0; // Только по прямой
    }

    public int getCost() {
        return cost;
    }

    public char getLastMonster() {
        return last_monster;
    }

    public boolean isGhostFl() {
        return ghostFl;
    }

    public boolean isMimikFl() {
        return mimikFl;
    }

    public char getSymbolForMimik() {
        return symbolForMimik;
    }

    private java.lang.Character getRandomSymbolForMimik() {
        List<java.lang.Character> symbols = ConstTypesSubjects.getAllSymbols();
        Random random = new Random();
        return symbols.get(random.nextInt(symbols.size()));
    }

}