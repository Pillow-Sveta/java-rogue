package domain.entities.settings.subjects;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ConstTypesSubjects {
    WEAPON(3, 'W'),
    ELIXIR(4, 'E'),
    FOOD(5, 'F'),
    SCROLL(6, 'P'),
    GOLD(7, '*');

    private final int type;
    private final char symbol;

    ConstTypesSubjects(int type, char symbol) {
        this.type = type;
        this.symbol = symbol;
    }

    public static List<Character> getAllSymbols() {
        return Arrays.stream(values())
                .map(ConstTypesSubjects::getSymbol)
                .collect(Collectors.toList());
    }

    public int getType() {
        return type;
    }

    public char getSymbol() {
        return symbol;
    }
}
