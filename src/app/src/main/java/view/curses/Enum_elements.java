package view.curses;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;

import static jcurses.system.Toolkit.*;
import static view.curses.Render.*;

public enum Enum_elements {
    HERO('Y', whiteColor),
    HEROinTUNNEL('y', white_reverse_Color),
    FLOOR('.', greenColor),
    corner('+', whiteColor),
    LR_corner('@', yellowColor),
    UL_corner('%', yellowColor),
    LL_corner('^', yellowColor),
    UR_corner('~', yellowColor),
    HorizontalLine('-', yellowColor),
    VerticalLine('|', yellowColor),
    TUNNEL('=', whiteColor),
    WALL('#', yellowColor),
    DOOR('\\', yellowColor),
    EMPTY(' ', whiteColor),
    STAIRCASE('D', whiteColor),

    WEAPON('W', blueColor),
    ARMOR('(', cyanColor),

    GOLD('*', yellowColor),
    ELIXIR('E', redColor),
    FOOD('F', greenColor),
    SCROLL('P', magentaColor),

    ZOMBIE('Z', greenColor),
    VAMPIRE('V', redColor),
    GHOST('G', whiteColor),
    OGRE('O', yellowColor),
    SNAKE('S', whiteColor),
    MIMIC('M', whiteColor),

    ZOMBIE_in_TUNNEL('z', white_reverse_Color),
    VAMPIRE_in_TUNNEL('v', white_reverse_Color),
    GHOST_in_TUNNEL('g', white_reverse_Color),
    OGRE_in_TUNNEL('o', white_reverse_Color),
    SNAKE_in_TUNNEL('s', white_reverse_Color),
    MIMIC_in_TUNNEL('m', white_reverse_Color);

    private final char sign;
    private final CharColor color;

    Enum_elements(char sign, CharColor color) {
        this.sign = sign;
        this.color = color;
    }

    public static Enum_elements fromSign(char sign) {
        Enum_elements result = Enum_elements.EMPTY;
        for (Enum_elements element : values()) {
            if (element.getSign() == sign) {
                result = element;
            }
        }
        return result;
    }

    public char getSign() {
        return sign;
    }

    public CharColor getColor() {
        return color;
    }

    public void drawChar(int i, int j) {
        if (this == HorizontalLine) {
            Toolkit.drawHorizontalLine(i, j, i, this.getColor());
        } else if (this == VerticalLine) {
            Toolkit.drawVerticalLine(i, j, j, this.getColor());
        } else if (this == UL_corner) {
            Toolkit.drawCorner(i, j, i, j, this.getColor(), UL_CORNER);
        } else if (this == UR_corner) {
            Toolkit.drawCorner(i, j, i, j, this.getColor(), UR_CORNER);
        } else if (this == LR_corner) {
            Toolkit.drawCorner(i, j, i, j, this.getColor(), LR_CORNER);
        } else if (this == LL_corner) {
            Toolkit.drawCorner(i, j, i, j, this.getColor(), LL_CORNER);
        } else if (this == TUNNEL) {
            Toolkit.drawHorizontalThickLine(i, j, i, this.getColor());
        } else if ((this == HEROinTUNNEL) || (this == ZOMBIE_in_TUNNEL) || (this == VAMPIRE_in_TUNNEL) || (this == GHOST_in_TUNNEL) || (this == OGRE_in_TUNNEL) || (this == SNAKE_in_TUNNEL) || (this == MIMIC_in_TUNNEL)) {
            printString(
                    String.valueOf((char) ((int) this.getSign() - ((int) 'a' - (int) 'A'))),
                    i,
                    j,
                    this.getColor());
        } else {
            Toolkit.printString(
                    String.valueOf(this.getSign()),
                    i,
                    j,
                    this.getColor());
        }
    }
}
