package datalayer.data;

import java.io.Serial;
import java.io.Serializable;

public class SessionStats implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public int treasureAmount;
    public int level;
    public int killed;
    public int eaten;
    public int drunk;
    public int read;
    public int hitMade;
    public int hitGot;
    public int moved;
    public String playerName;
    public String id;

    public SessionStats() {
    }


    @Override
    public String toString() {
        return String.format("Name:%s\tGold:%d\tLevel:%d\tKilled monsters:%d\tEaten food:%d\tDrunk elixirs:%d\tRead scrolls:%d\tHits made:%d\tHits got:%d\tMoved on:%d\tId:%s",
                playerName, treasureAmount, level, killed, eaten, drunk, read, hitMade, hitGot, moved, id);
    }
}
