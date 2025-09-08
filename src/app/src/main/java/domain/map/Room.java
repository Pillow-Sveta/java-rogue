package domain.map;

import domain.entities.utils.PositionOnMap;
import domain.utils.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Room implements Serializable {

    static final int MaxWidth = 20;
    static final int MinWidth = 2;
    static final int MaxHeight = 5;
    static final int MinHeight = 2;
    private final static int corridorChance = 50;
    public static char wallSymb = '#';
    public static char floorSymb = '.';
    public static char cornerSymb = '+';
    public static char doorSymb = '\\';
    static Random rand = new Random();
    static private Room[][] rooms;
    public Point mapCoord = new Point();
    public Point roomCoord = new Point();
    public ArrayList<Door> doors = new ArrayList<>(); //global(map) coords of doors
    public int width;
    public int height;
    public PositionOnMap bound1;
    public PositionOnMap bound2;
    public boolean isConnectedToEnd = false;
    public ArrayList<Room> neighbors = new ArrayList<>();

    public Room(int i_, int j_, Room[][] lvl) {
        roomCoord.x = i_;
        roomCoord.y = j_;
        rooms = lvl;
        if (i_ == 2 && j_ == 2) isConnectedToEnd = true;
        GenerateRoom();
    }

    public static void MakeNeighbors() {
        for (int i = 2; i >= 0; i--) {
            for (int j = 2; j >= 0; j--) {
                DefineNeighbors(rooms[j][i]);
            }
        }
    }

    public static void DefineNeighbors(Room r) {
        do {
            if (r.roomCoord.y - 1 >= 0 && rand.nextInt(0, corridorChance) == 0) {
                var nei = rooms[r.roomCoord.y - 1][r.roomCoord.x];
                r.DefineNeighbor(nei, new Point(0, -1));
            }
            if (r.roomCoord.x - 1 >= 0 && rand.nextInt(0, corridorChance) == 0) {
                var nei = rooms[r.roomCoord.y][r.roomCoord.x - 1];
                r.DefineNeighbor(nei, new Point(-1, 0));
            }
            if (r.roomCoord.x + 1 < 3 && rand.nextInt(0, corridorChance) == 0) {
                var nei = rooms[r.roomCoord.y][r.roomCoord.x + 1];
                r.DefineNeighbor(nei, new Point(1, 0));
            }
            if (r.roomCoord.y + 1 < 3 && rand.nextInt(0, corridorChance) == 0) {
                var nei = rooms[r.roomCoord.y + 1][r.roomCoord.x];
                r.DefineNeighbor(nei, new Point(0, 1));
            }
        } while (r.neighbors.isEmpty() || !r.isConnectedToEnd);
    }

    static Room isRoomsConnected() {
        for (int i = 2; i >= 0; i--) {
            for (int j = 2; j >= 0; j--) {
                if (!rooms[j][i].isConnectedToEnd) return rooms[j][i];
            }
        }
        return null;
    }

    public void GenerateRoom() {
        Random r = new Random();
        mapCoord.x = 0;
        mapCoord.y = 0;
        width = r.nextInt(MinWidth, MaxWidth);
        height = r.nextInt(MinHeight, MaxHeight);
    }

    private void DefineNeighbor(Room nei, Point dir) {
        if (nei.isConnectedToEnd) isConnectedToEnd = true;
        if (isConnectedToEnd) nei.isConnectedToEnd = true;
        if (neighbors.contains(nei)) return;
        int right = dir.x == -1 ? 0 : dir.x;
        int down = dir.y == -1 ? 0 : dir.y;
        int left = dir.x == -1 ? 1 : 0;
        int up = dir.y == -1 ? 1 : 0;

        neighbors.add(nei);
        int yDoorCord = dir.y == 0 ? rand.nextInt(mapCoord.y + 1, mapCoord.y + height + 1) : mapCoord.y;
        int xDoorCord = dir.x == 0 ? rand.nextInt(mapCoord.x + 1, mapCoord.x + width + 1) : mapCoord.x;
        Door firstDoor = new Door(new Point(xDoorCord + (width + 1) * right, yDoorCord + (height + 1) * down));
        doors.add(firstDoor);

        nei.neighbors.add(this);
        yDoorCord = dir.y == 0 ? rand.nextInt(nei.mapCoord.y + 1, nei.mapCoord.y + nei.height + 1) : nei.mapCoord.y;
        xDoorCord = dir.x == 0 ? rand.nextInt(nei.mapCoord.x + 1, nei.mapCoord.x + nei.width + 1) : nei.mapCoord.x;
        Door secondDoor = new Door(new Point(xDoorCord + (nei.width + 1) * left, yDoorCord + (nei.height + 1) * up));
        nei.doors.add(secondDoor);


        firstDoor.connection = secondDoor;
        secondDoor.connection = firstDoor;
    }

    void defineRoomCoords(int minSpace, int maxSpace) {
        if (roomCoord.x == 0) mapCoord.x = rand.nextInt(0, 5);
        else
            mapCoord.x = rooms[roomCoord.y][roomCoord.x - 1].mapCoord.x + rooms[roomCoord.y][roomCoord.x - 1].width + rand.nextInt(minSpace, maxSpace);
        if (roomCoord.y == 0) mapCoord.y = rand.nextInt(0, 5);
        else
            mapCoord.y = rooms[roomCoord.y - 1][roomCoord.x].mapCoord.y + rooms[roomCoord.y - 1][roomCoord.x].height + rand.nextInt(8, 11);
        bound1 = new PositionOnMap(mapCoord.y + 1, mapCoord.x + 1);
        bound2 = new PositionOnMap(mapCoord.y + height + 1, mapCoord.x + width + 1);
    }

    public PositionOnMap freeRandomSpace(char[][] map) {
        var pos = new PositionOnMap();
        do {
            pos = new PositionOnMap(rand.nextInt(bound1.getY(), bound2.getY()),
                    rand.nextInt(bound1.getX(), bound2.getX()));
        } while (map[pos.getY()][pos.getX()] != 0);
        return pos;
    }

    public boolean isPosInRoom(PositionOnMap pos) {
        return (pos.getX() >= bound1.getX() && pos.getX() <= bound2.getX()) &&
                (pos.getY() >= bound1.getY() && pos.getY() <= bound2.getY());
    }
}
