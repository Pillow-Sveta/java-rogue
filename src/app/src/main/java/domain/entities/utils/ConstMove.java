package domain.entities.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ConstMove {
    RIGHT(0, 1),
    LEFT(0, -1),
    UP(-1, 0),
    DOWN(1, 0);

    private final int delY;
    private final int delX;


    ConstMove(int delY, int delX) {
        this.delY = delY;
        this.delX = delX;
    }

    public static List<ConstMove> getShuffledMoves() {
        List<ConstMove> moves = new ArrayList<>(Arrays.asList(values()));
        Collections.shuffle(moves);
        return moves;
    }

    public int getDelX() {
        return delX;
    }

    public int getDelY() {
        return delY;
    }
}
