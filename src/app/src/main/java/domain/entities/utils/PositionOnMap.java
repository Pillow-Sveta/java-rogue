package domain.entities.utils;

import java.io.Serializable;

public class PositionOnMap implements Serializable {
    private int y;
    private int x;

    public PositionOnMap() {
        this.y = -1;
        this.x = -1;
    }

    public PositionOnMap(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPos(int delY, int delX) {
        y = delY;
        x = delX;
    }

    public boolean checkPos(int y, int x) {
        return this.y == y && this.x == x;
    }
}
