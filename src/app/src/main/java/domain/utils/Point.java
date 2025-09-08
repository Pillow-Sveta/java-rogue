package domain.utils;

import java.io.Serializable;

public class Point implements Serializable {
    public int x;
    public int y;

    public Point() {
    }

    public Point(int x_, int y_) {
        x = x_;
        y = y_;
    }
}