package domain.map;

import domain.utils.Point;

import java.io.Serializable;

public class Door implements Serializable {
    public Point coords = new Point();
    public Door connection;
    public boolean isDrawen = false;

    public Door(Point p) {
        coords = p;
    }
}
