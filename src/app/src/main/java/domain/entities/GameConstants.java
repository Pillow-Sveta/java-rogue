package domain.entities;

import java.util.Set;

public class GameConstants {
    public static final Set<Character> EMPTY = Set.of('.', '\\', '=');
    public static final char EXIT = 'D';
    public static final int MAX_COUNT_TYPE_SUBJECTS = 9;
    public static final int MAX_COUNT_TYPE_SUBJECTS_WEAPONS = 3;
    private GameConstants() {
    } // Запрет создания экземпляров
}
