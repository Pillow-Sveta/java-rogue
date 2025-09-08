package domain.map;

import datalayer.data.Session;
import domain.entities.creatures.character.Character;
import domain.entities.creatures.enemies.Enemy;
import domain.entities.items.subjects.Subject;
import domain.entities.items.subjects.elixir.Elixir;
import domain.entities.items.subjects.food.Food;
import domain.entities.items.subjects.scroll.Scroll;
import domain.entities.items.subjects.weapon.Weapon;
import domain.entities.settings.creatures.ConstCharacter;
import domain.entities.utils.PositionOnMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static domain.entities.GameConstants.EXIT;


public class Level implements Serializable {
    public final static int MAP_HEIGHT = 100;
    public final static int MAP_WIDTH = 100;
    final static int MAX_SPACE = 10;
    final static int MIN_SPACE = 6;
    private static final Random rand = new Random();
    private static final int enemyChance = 50;
    private static final int subjectChance = 50;
    public Room[][] rooms;
    public char[][] map;
    public char[][] mapBuf;
    public int[][] warFog;
    public int count;
    public Character character;
    public ArrayList<Enemy> enemies;
    public ArrayList<Subject> items;
    private char[][] corridorMap;

    public Level() {
        GenerateRooms();
    }

    public void GenerateRooms() {
        rooms = new Room[3][3];
        map = new char[MAP_HEIGHT][MAP_WIDTH];
        mapBuf = new char[MAP_HEIGHT][MAP_WIDTH];
        corridorMap = new char[MAP_HEIGHT][MAP_WIDTH];
        warFog = new int[MAP_HEIGHT][MAP_WIDTH];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Room r = new Room(i, j, rooms);
                r.defineRoomCoords(MIN_SPACE, MAX_SPACE);
                rooms[j][i] = r;
            }
        }
        Room.MakeNeighbors();
        addRoomsToMap();
        while (!addCorridors()) {
        }
        addExitToMap();
        spawnEntities();
        update();
    }

    private void addExitToMap() {
        var exitRoom = rooms[2][2];
        var pos = exitRoom.freeRandomSpace(mapBuf);
        map[pos.getY()][pos.getX()] = EXIT;
        mapBuf[pos.getY()][pos.getX()] = EXIT;
    }

    public void spawnEntities() {
        items = new ArrayList<>();
        enemies = new ArrayList<>();
        var p = rooms[0][0].freeRandomSpace(mapBuf);
        if (character == null) {
            character = new Character(
                    p,
                    mapBuf,
                    enemies,
                    items
            );
        } else {
            character.setMap(mapBuf);
            character.setPosition(p);
            character.setListEnemy(enemies);
            character.setListItems(items);
        }

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                var r = rooms[y][x];
                if (x != 0)
                    while (rand.nextInt(0, 100) <= enemyChance) {
                        var pos = r.freeRandomSpace(mapBuf);
                        enemies.add(new Enemy(pos, mapBuf, character));
                    }
                while (rand.nextInt(0, 100) <= subjectChance) {
                    var subBuf = new Subject[4];
                    var pos = r.freeRandomSpace(mapBuf);
                    subBuf[0] = new Elixir(pos);
                    subBuf[1] = new Food(pos);
                    subBuf[2] = new Scroll(pos);
                    subBuf[3] = new Weapon(pos);
                    items.add(subBuf[rand.nextInt(0, 4)]);
                }
            }
        }

    }

    public void update() {
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, mapBuf[i], 0, map[i].length);
        }
        drawCharacter();
        drawEnemies();
        drawItems();
        Session.saveSession();
        //Session.saveSession();
    }

    private void drawItems() {
        items.forEach(item -> {
            PositionOnMap pos = item.getPosition();
            mapBuf[pos.getY()][pos.getX()] = item.getSymbol();
        });
    }

    private void drawEnemies() {
        enemies.forEach(enemy -> {
            PositionOnMap pos = enemy.getPosition();
            if (enemy.isGhostFl() && checkCharacterNotNear(pos)) {
                mapBuf[pos.getY()][pos.getX()] = enemy.getSymbol();
            } else if (enemy.isMimikFl() && checkCharacterNotNear(pos)) {
                mapBuf[pos.getY()][pos.getX()] = enemy.getSymbolForMimik();
            } else if (!enemy.isGhostFl()) {
                mapBuf[pos.getY()][pos.getX()] = enemy.getSymbol();
            }
        });
    }


    private void drawCharacter() {
        PositionOnMap charPos = character.getPosition();
        mapBuf[charPos.getY()][charPos.getX()] = character.getSymbol();
        for (int x = charPos.getX() - 1; x <= charPos.getX() + 1; x++) {
            for (int y = charPos.getY() - 1; y <= charPos.getY() + 1; y++) {
                warFog[y][x] = 1;
            }
        }
        var r = objectsRoom(charPos);
        if (r != null) {
            for (int x = r.bound1.getX() - 1; x < r.bound2.getX() + 1; x++) {
                for (int y = r.bound1.getY() - 1; y < r.bound2.getY() + 1; y++) {
                    warFog[y][x] = 1;
                }
            }
        }
    }

    void addRoomToMap(Room r) {
        for (int i = r.mapCoord.x; i < r.mapCoord.x + r.width + 2; i++) {
            for (int j = r.mapCoord.y; j < r.mapCoord.y + r.height + 2; j++) {
                if (
                        (i == r.mapCoord.x && j == r.mapCoord.y) ||
                                (i == r.mapCoord.x + r.width + 1 && j == r.mapCoord.y) ||
                                (i == r.mapCoord.x && j == r.mapCoord.y + r.height + 1) ||
                                (i == r.mapCoord.x + r.width + 1 && j == r.mapCoord.y + r.height + 1))
                    map[j][i] = Room.cornerSymb;
                else if (i == r.mapCoord.x || i == r.mapCoord.x + r.width + 1 || j == r.mapCoord.y || j == r.mapCoord.y + r.height + 1)
                    map[j][i] = Room.wallSymb;
                else map[j][i] = Room.floorSymb;
            }
        }
        var pos = rooms[2][2].freeRandomSpace(mapBuf);
    }

    private void addRoomsToMap() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addRoomToMap(rooms[i][j]);
            }
        }
    }

    private void clearCorridors() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (Door d : rooms[i][j].doors)
                    d.isDrawen = false;
        for (char[] chars : corridorMap) Arrays.fill(chars, (char) 0);
    }

    private boolean addCorridors() {
        clearCorridors();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (Door door : rooms[i][j].doors) {
                    map[door.coords.y][door.coords.x] = Room.doorSymb;
                    if (addRoomCorridors(door, rooms[i][j])) return false;
                }
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (corridorMap[y][x] != 0) map[y][x] = corridorMap[y][x];
            }
        }
        return true;
    }

    private boolean addRoomCorridors(Door d, Room r) {
        if (d.isDrawen) return false;
        boolean flag = false;
        if ((d.coords.x == r.mapCoord.x) || (d.coords.x == r.mapCoord.x + r.width + 1)) {
            if (addHorizontalCorridor(d, d.connection)) flag = true;
        } else {
            if (addVerticalCorridor(d, d.connection)) flag = true;
        }
        d.isDrawen = true;
        d.connection.isDrawen = true;
        return flag;
    }

    private boolean addHorizontalCorridor(Door start, Door finish) {
        int deltaX = finish.coords.x - start.coords.x;
        int deltaY = finish.coords.y - start.coords.y;
        int dirX = deltaX > 0 ? 1 : -1;
        int dirY = deltaY > 0 ? 1 : -1;
        int xCorner = rand.nextInt(1, deltaX);
        int y = start.coords.y;
        for (int i = 1; i < Math.abs(deltaX); i++) {
            if (corridorMap[y][start.coords.x + i * dirX] != 0 || map[y][start.coords.x + i * dirX] != 0) {
                return true;
            }
            corridorMap[y][start.coords.x + i * dirX] = '=';
            if (i == xCorner) {
                for (int j = 1; j <= Math.abs(deltaY); j++) {
                    y = start.coords.y + j * dirY;
                    if (map[y][start.coords.x + i * dirX] != 0 ||
                            corridorMap[y][start.coords.x + i * dirX] != 0 ||
                            ((j != 2 || j - 1 >= Math.abs(deltaY)) && corridorsAround(new PositionOnMap(y, start.coords.x + i * dirX)) >= 3) ||
                            ((j == 2 || j - 1 < Math.abs(deltaY)) && corridorsAround(new PositionOnMap(y, start.coords.x + i * dirX)) >= 3)
                    ) {
                        return true;
                    }
                    corridorMap[y][start.coords.x + i * dirX] = '=';
                }
            }
        }
        return false;
    }

    private boolean addVerticalCorridor(Door start, Door finish) {
        int deltaX = finish.coords.x - start.coords.x;
        int deltaY = finish.coords.y - start.coords.y;
        int dirX = deltaX > 0 ? 1 : -1;
        int dirY = deltaY > 0 ? 1 : -1;
        int yCorner = rand.nextInt(1, Math.abs(deltaY));
        int x = start.coords.x;
        for (int i = 1; i < Math.abs(deltaY); i++) {
            if (corridorMap[start.coords.y + i * dirY][x] != 0 || map[start.coords.y + i * dirY][x] != 0) {
                return true;
            }
            corridorMap[start.coords.y + i * dirY][x] = '=';
            if (i == yCorner) {
                for (int j = 1; j <= Math.abs(deltaX); j++) {
                    x = start.coords.x + j * dirX;
                    if (map[start.coords.y + i * dirY][x] != 0 ||
                            corridorMap[start.coords.y + i * dirY][x] != 0 ||
                            ((j != 2 || j - 1 >= Math.abs(deltaX)) && corridorsAround(new PositionOnMap(start.coords.y + i * dirY, x)) >= 3) ||
                            (j == 2 && corridorsAround(new PositionOnMap(start.coords.y + i * dirY, x)) >= 3)
                    ) {
                        return true;
                    }
                    corridorMap[start.coords.y + i * dirY][x] = '=';
                }
            }
        }
        return false;
    }

    private int corridorsAround(PositionOnMap corridor) {
        int col = 0;
        if (corridorMap[corridor.getY() + 1][corridor.getX()] == '=') col++;
        if (corridorMap[corridor.getY() + 1][corridor.getX() + 1] == '=') col++;
        if (corridorMap[corridor.getY() + 1][corridor.getX() - 1] == '=') col++;
        if (corridorMap[corridor.getY()][corridor.getX() + 1] == '=') col++;
        if (corridorMap[corridor.getY()][corridor.getX() - 1] == '=') col++;
        if (corridorMap[corridor.getY() - 1][corridor.getX()] == '=') col++;
        if (corridorMap[corridor.getY() - 1][corridor.getX() + 1] == '=') col++;
        if (corridorMap[corridor.getY() - 1][corridor.getX() - 1] == '=') col++;
        return col;
    }

    private Room objectsRoom(PositionOnMap pos) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                var r = rooms[i][j];
                if (r.isPosInRoom(pos)) return r;
            }
        }
        return null;
    }

    private boolean checkCharacterNotNear(PositionOnMap pos) {
        for (int i = Math.max((pos.getY() - 1), 0); i <= Math.min((pos.getY() + 1), mapBuf.length - 1); ++i) {
            for (int j = Math.max((pos.getX() - 1), 0); j <= Math.min((pos.getX() + 1), mapBuf[0].length - 1); ++j) {
                if (mapBuf[i][j] == ConstCharacter.CHARACTER.getSymbol())
                    return false;
            }
        }
        return true;
    }

}
