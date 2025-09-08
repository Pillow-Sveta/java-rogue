package view.curses;

import datalayer.data.Session;
import domain.entities.items.subjects.Subject;
import domain.entities.items.subjects.weapon.Weapon;
import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static jcurses.system.Toolkit.UL_CORNER;
import static jcurses.system.Toolkit.UR_CORNER;
import static view.curses.Enum_elements.*;

public class Render {

    public final static int SCREEN_HEIGHT = Toolkit.getScreenHeight();
    public final static int SCREEN_WIDTH = Toolkit.getScreenWidth();

    public final static CharColor redColor = new CharColor(CharColor.BLACK, CharColor.RED, CharColor.NORMAL, CharColor.BOLD);
    public final static CharColor whiteColor = new CharColor(CharColor.BLACK, CharColor.WHITE, CharColor.NORMAL, CharColor.BOLD);
    public final static CharColor white_reverse_Color = new CharColor(CharColor.BLACK, CharColor.WHITE, CharColor.NORMAL, CharColor.REVERSE);
    public final static CharColor greenColor = new CharColor(CharColor.BLACK, CharColor.GREEN, CharColor.NORMAL, CharColor.BOLD);
    public final static CharColor yellowColor = new CharColor(CharColor.BLACK, CharColor.YELLOW, CharColor.NORMAL, CharColor.BOLD);
    public final static CharColor blueColor = new CharColor(CharColor.BLACK, CharColor.BLUE, CharColor.NORMAL, CharColor.BOLD);
    public final static CharColor cyanColor = new CharColor(CharColor.BLACK, CharColor.CYAN, CharColor.NORMAL, CharColor.BOLD);
    public final static CharColor magentaColor = new CharColor(CharColor.BLACK, CharColor.MAGENTA, CharColor.NORMAL, CharColor.BOLD);
    public final static CharColor blackColor = new CharColor(CharColor.BLACK, CharColor.BLACK, CharColor.NORMAL, CharColor.BOLD);


    private static void transformation_corner(char[][] field, int i, int j) {
        boolean hasRight = (i + 1 < SCREEN_WIDTH) && (field[i + 1][j] == HorizontalLine.getSign() || field[i + 1][j] == DOOR.getSign());
        boolean hasDown = (j + 1 < SCREEN_HEIGHT) && (field[i][j + 1] == VerticalLine.getSign() || field[i][j + 1] == DOOR.getSign());
        boolean hasLeft = (i - 1 >= 0) && (field[i - 1][j] == HorizontalLine.getSign() || field[i - 1][j] == DOOR.getSign());
        boolean hasUp = (j - 1 >= 0) && (field[i][j - 1] == VerticalLine.getSign() || field[i][j - 1] == DOOR.getSign());
        boolean has2Right = (i + 2 < SCREEN_WIDTH) && (field[i + 2][j] == HorizontalLine.getSign() || field[i + 2][j] == DOOR.getSign() || field[i + 2][j] == corner.getSign() || field[i + 2][j] == UR_corner.getSign() || field[i + 2][j] == LR_corner.getSign());
        boolean has2Down = (j + 2 < SCREEN_HEIGHT) && (field[i][j + 2] == VerticalLine.getSign() || field[i][j + 2] == DOOR.getSign() || field[i][j + 2] == corner.getSign() || field[i][j + 2] == LL_corner.getSign() || field[i][j + 2] == LR_corner.getSign());
        boolean has2Left = (i - 2 >= 0) && (field[i - 2][j] == HorizontalLine.getSign() || field[i - 2][j] == DOOR.getSign() || field[i - 2][j] == corner.getSign() || field[i - 2][j] == UR_corner.getSign() || field[i - 2][j] == LR_corner.getSign());
        boolean has2Up = (j - 2 >= 0) && (field[i][j - 2] == VerticalLine.getSign() || field[i][j - 2] == DOOR.getSign() || field[i][j - 2] == corner.getSign() || field[i][j - 2] == UL_corner.getSign() || field[i][j - 2] == UR_corner.getSign());


        if ((hasRight || has2Right) && (hasDown || has2Down)) {
            field[i][j] = UL_corner.getSign();
        } else if ((hasLeft || has2Left) && (hasDown || has2Down)) {
            field[i][j] = UR_corner.getSign();
        } else if ((hasRight || has2Right) && (hasUp || has2Up)) {
            field[i][j] = LL_corner.getSign();
        } else if ((hasLeft || has2Left) && (hasUp || has2Up)) {
            field[i][j] = LR_corner.getSign();
        }
    }


    private static boolean isHorizontalWall(char[][] field, int i, int j) {
        boolean horizontal = false;
        if (i - 1 >= 0) {
            char left = field[i - 1][j];
            if (left == HorizontalLine.getSign() || left == WALL.getSign() || ((i - 2 >= 0) && (left == HERO.getSign()) && (field[i - 2][j] == WALL.getSign() || field[i - 2][j] == HorizontalLine.getSign() || field[i - 2][j] == corner.getSign())) || left == DOOR.getSign() || left == corner.getSign()) {
                horizontal = true;
            }
        }
        if (!horizontal && i + 1 < SCREEN_WIDTH) {
            char right = field[i + 1][j];
            if (right == HorizontalLine.getSign() || right == WALL.getSign() || ((i + 2 < SCREEN_WIDTH) && (right == HERO.getSign()) && (field[i + 2][j] == WALL.getSign() || field[i + 2][j] == HorizontalLine.getSign() || field[i + 2][j] == corner.getSign())) || right == DOOR.getSign() || right == corner.getSign()) {
                horizontal = true;
            }
        }

        return horizontal;
    }


    private static boolean isVerticalWall(char[][] field, int i, int j) {
        boolean vertical = false;
        if (j - 1 >= 0) {
            char up = field[i][j - 1];
            if (up == VerticalLine.getSign() || up == WALL.getSign() || ((j - 2 >= 0) && (up == HERO.getSign()) && (field[i][j - 2] == WALL.getSign() || field[i][j - 2] == VerticalLine.getSign() || field[i][j - 2] == corner.getSign())) || up == DOOR.getSign() || up == corner.getSign()) {
                vertical = true;
            }
        }
        if (!vertical && j + 1 < SCREEN_HEIGHT) {
            char down = field[i][j + 1];
            if (down == VerticalLine.getSign() || down == WALL.getSign() || ((j + 2 < SCREEN_HEIGHT) && (down == HERO.getSign()) && (field[i][j + 2] == WALL.getSign() || field[i][j + 2] == VerticalLine.getSign() || field[i][j + 2] == corner.getSign())) || down == DOOR.getSign() || down == corner.getSign()) {
                vertical = true;
            }
        }

        return vertical;
    }


    private static void transformation_matrix(char[][] field) {

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {
                if (field[i][j] == WALL.getSign()) {

                    boolean horizontal = isHorizontalWall(field, i, j);
                    boolean vertical = isVerticalWall(field, i, j);

                    if (horizontal && !vertical) {
                        field[i][j] = HorizontalLine.getSign();
                    } else if (!horizontal && vertical) {
                        field[i][j] = VerticalLine.getSign();
                    }

                }
            }
        }

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {
                if (field[i][j] == corner.getSign()) {
                    transformation_corner(field, i, j);
                } else if (field[i][j] == HERO.getSign() || field[i][j] == ZOMBIE.getSign() || field[i][j] == VAMPIRE.getSign() || field[i][j] == GHOST.getSign() || field[i][j] == OGRE.getSign() || field[i][j] == SNAKE.getSign() || field[i][j] == MIMIC.getSign()) {
                    tunnel(field, i, j);
                }
            }
        }
    }

    private static void tunnel(char[][] field, int i, int j) {
        boolean hasRight = (i + 1 < SCREEN_WIDTH) && (field[i + 1][j] == TUNNEL.getSign() || field[i + 1][j] == EMPTY.getSign());
        boolean hasDown = (j + 1 < SCREEN_HEIGHT) && (field[i][j + 1] == TUNNEL.getSign() || field[i][j + 1] == EMPTY.getSign());
        boolean hasLeft = (i - 1 >= 0) && (field[i - 1][j] == TUNNEL.getSign() || field[i - 1][j] == EMPTY.getSign());
        boolean hasUp = (j - 1 >= 0) && (field[i][j - 1] == TUNNEL.getSign() || field[i][j - 1] == EMPTY.getSign());

        if (hasRight || hasLeft || hasUp || hasDown) {
            field[i][j] = (char) ((int) field[i][j] + ((int) 'a' - (int) 'A'));

        }
    }


    public static boolean valid_character(char ch) {
        return ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '-') || (ch == '_'));
    }

    public static int new_or_save_game() {
        Toolkit.startPainting();
        Toolkit.clearScreen(blackColor);
        Toolkit.printString(
                "1) start a new game",
                1,
                1,
                yellowColor);
        Toolkit.printString(
                "2) load a saved game",
                1,
                2,
                yellowColor);
        Toolkit.printString(
                "Choose (1-2)",
                1,
                5,
                yellowColor);
        Toolkit.endPainting();

        InputChar input = Toolkit.readCharacter();
        while (true) {
            if (!input.isSpecialCode()) {
                char ch = input.getCharacter();
                if (ch >= '1' && ch <= (char) ('0' + 2)) {
                    break;
                }
            }

            input = Toolkit.readCharacter();
        }

        return (int) input.getCharacter() - (int) '0';
    }

    public static String start_menu() {
        Toolkit.clearScreen(blackColor);
        String question = "Rogue's Name?";
        StringBuilder name = new StringBuilder();
        int startX = 1;
        int startY = 1;
        InputChar enter = new InputChar('\n');
        InputChar null_symbol = new InputChar('\0');
        InputChar carriage_symbol = new InputChar('\r');

        int x = question.length() + 2;

        Toolkit.startPainting();
        Toolkit.printString(
                question,
                startX,
                startY,
                yellowColor);
        Toolkit.endPainting();

        InputChar input = Toolkit.readCharacter();
        while (!input.equals(enter) && !input.equals(null_symbol) && !input.equals(carriage_symbol) && name.length() < 25) {
            Toolkit.startPainting();
            if (input.isSpecialCode()) {
                if (input.getCode() == InputChar.KEY_BACKSPACE) {
                    if (!name.isEmpty()) {
                        name.deleteCharAt(name.length() - 1);

                        Toolkit.printString(
                                String.valueOf(' '),
                                x - 1,
                                startY,
                                yellowColor);
                        x--;
                    }
                }

            } else if (!input.isSpecialCode() && valid_character(input.getCharacter())) {
                name.append(input.getCharacter());
                Toolkit.printString(
                        String.valueOf(input),
                        x,
                        startY,
                        yellowColor);
                x++;
            }
            Toolkit.endPainting();
            input = Toolkit.readCharacter();

        }

        if (name.isEmpty()) {
            name.append("Rodney");
        }


        return name.toString();
    }

    public static void success_load(String name) {
        InputChar space = new InputChar(' ');
        Toolkit.startPainting();
        Toolkit.printString("Hello " + name + ", Welcome to the Dungeons of Doom", 1, 2, yellowColor);
        Toolkit.printString("Click -Space- to continue", 1, 6, yellowColor);
        Toolkit.endPainting();
        InputChar input = Toolkit.readCharacter();
        while (!input.equals(space)) {
            input = Toolkit.readCharacter();
        }
    }

    public static void stats(int x, int y, int level, int hits, int max_hits, int str, int agl, int gold, int armor, int exp) {
        Toolkit.printString(
                String.format("Level:%-6dHits: %d(%d)    Str:%d    Agl:%d    Gold:%-6dArmor:%-6dExp: %d", level, hits, max_hits, str, agl, gold, armor, exp),
                x,
                y,
                yellowColor);
    }

    public static void help(int offsetX, int offsetY) {

        Toolkit.startPainting();
        Toolkit.clearScreen(blackColor);
        String path = "menu.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                Toolkit.printString(
                        line,
                        offsetX,
                        offsetY + y,
                        yellowColor);
                y++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        Toolkit.endPainting();

        InputChar space = new InputChar(' ');
        InputChar input = Toolkit.readCharacter();
        while (!input.equals(space)) {
            input = Toolkit.readCharacter();
        }

    }


    public static String weapon(int offsetX, int offsetY, ArrayList<Weapon> Inventory) {
        String answer = "";
        Toolkit.startPainting();
        Toolkit.clearScreen(blackColor);


        String name_weapon = "";
        int useWeaponSubtype = -1;
        for (Weapon weapon : Inventory) {
            if (weapon.isActiveFl()) {
                name_weapon = weapon.getName();
                useWeaponSubtype = weapon.getSubtype();
            }
        }

        int i = 0;
        if (useWeaponSubtype >= 0) {
            Toolkit.printString("You use " + name_weapon, offsetX, offsetY + i, yellowColor);
            Toolkit.printString(i + ") Take off weapon", offsetX, offsetY + i + 1, yellowColor);
        }


        for (Weapon weapon : Inventory) {
            Toolkit.printString(i + 1 + ") " + weapon.getName(), offsetX, offsetY + i + 2, yellowColor);
            i++;
        }
        Toolkit.endPainting();
        if (Inventory.size() > 1) {
            Toolkit.printString("Choose to use (1-" + Inventory.size() + ")", offsetX, offsetY + i + 4, yellowColor);
            Toolkit.printString("Click -t- to throw a weapon", offsetX, offsetY + i + 6, yellowColor);
        } else if (Inventory.size() == 1) {
            Toolkit.printString("Choose to use (" + Inventory.size() + ")", offsetX, offsetY + i + 4, yellowColor);
            Toolkit.printString("Click -t- to throw a weapon", offsetX, offsetY + i + 6, yellowColor);
        } else {
            Toolkit.printString("Inventory is empty", offsetX, offsetY + i + 4, yellowColor);

        }


        Toolkit.printString("Click -Space- to continue", offsetX, offsetY + i + 7, yellowColor);


        InputChar space = new InputChar(' ');
        InputChar input = Toolkit.readCharacter();
        char first_el = '1';
        if (useWeaponSubtype >= 0) {
            first_el = '0';
        }

        while (true) {
            if (!input.isSpecialCode()) {
                char ch = input.getCharacter();
                if ((ch >= first_el && ch <= (char) ('0' + Inventory.size())) || ch == ' ' || ch == 't') {
                    break;
                }
            }

            input = Toolkit.readCharacter();
        }

        if (!input.equals(space)) {
            if (input.getCharacter() == 't') {
                answer = throwWeapon(offsetX, offsetY, Inventory);
            } else if (input.getCharacter() == '0') {
                answer = Session.currenrLevel.character.takeOffWeapon();
            } else {
                answer = Session.currenrLevel.character.useSubject(Inventory.get((int) input.getCharacter() - (int) '0' - 1).getSubtype());
            }


        }

        return answer;

    }


    public static String throwWeapon(int offsetX, int offsetY, ArrayList<Weapon> Inventory) {
        String answer = "";
        Toolkit.startPainting();
        Toolkit.clearScreen(blackColor);


        int i = 0;

        Toolkit.printString("What weapon do you want to throw? ", offsetX, offsetY + i, yellowColor);

        for (Weapon weapon : Inventory) {
            Toolkit.printString(i + 1 + ") " + weapon.getName(), offsetX, offsetY + i + 1, yellowColor);
            i++;
        }
        Toolkit.endPainting();
        if (Inventory.size() > 1) {
            Toolkit.printString("Choose to use (1-" + Inventory.size() + ")", offsetX, offsetY + i + 3, yellowColor);
        } else if (Inventory.size() == 1) {
            Toolkit.printString("Choose to use (" + Inventory.size() + ")", offsetX, offsetY + i + 3, yellowColor);
        }

        Toolkit.printString("Click -Space- to cancel", offsetX, offsetY + i + 5, yellowColor);


        InputChar space = new InputChar(' ');
        InputChar input = Toolkit.readCharacter();

        while (true) {
            if (!input.isSpecialCode()) {
                char ch = input.getCharacter();
                if ((ch >= '1' && ch <= (char) ('0' + Inventory.size())) || ch == ' ' || ch == 't') {
                    break;
                }
            }

            input = Toolkit.readCharacter();
        }

        if (!input.equals(space)) {
            answer = Session.currenrLevel.character.throwSubject(Inventory.get((int) input.getCharacter() - (int) '0' - 1).getSubtype());
        }

        return answer;
    }


    public static String subjects(int offsetX, int offsetY, ArrayList<Subject> Inventory) {
        String answer = "";
        Toolkit.startPainting();
        Toolkit.clearScreen(blackColor);
        int i = 0;
        for (Subject subject : Inventory) {
            Toolkit.printString(i + 1 + ") " + subject.getName(), offsetX, offsetY + i, yellowColor);
            i++;
        }
        Toolkit.endPainting();
        if (Inventory.size() > 1) {
            Toolkit.printString("Choose to use (1-" + Inventory.size() + ")", offsetX, offsetY + i + 2, yellowColor);
        } else if (Inventory.size() == 1) {
            Toolkit.printString("Choose to use (" + Inventory.size() + ")", offsetX, offsetY + i + 2, yellowColor);
        } else {
            Toolkit.printString("Inventory is empty", offsetX, offsetY + i + 2, yellowColor);
        }

        Toolkit.printString("Click -Space- to continue", offsetX, offsetY + i + 6, yellowColor);


        InputChar space = new InputChar(' ');
        InputChar input = Toolkit.readCharacter();
        while (true) {
            if (!input.isSpecialCode()) {
                char ch = input.getCharacter();
                if ((ch >= '1' && ch <= (char) ('0' + Inventory.size())) || ch == ' ') {
                    break;
                }
            }

            input = Toolkit.readCharacter();
        }

        if (!input.equals(space)) {
            answer = Session.currenrLevel.character.useSubject(Inventory.get((int) input.getCharacter() - (int) '0' - 1).getSubtype());

        }

        return answer;

    }

    public static void finish_death_menu(String name, String monster) {

        int startX = SCREEN_WIDTH / 2;
        int startY = SCREEN_HEIGHT / 4;
        Toolkit.startPainting();
        Toolkit.clearScreen(blackColor);
        Toolkit.printString(
                "REST",
                startX - 2,
                startY,
                whiteColor);
        Toolkit.printString(
                "IN",
                startX - 1,
                startY + 1,
                whiteColor);
        Toolkit.printString(
                "PEACE",
                startX - 3,
                startY + 2,
                whiteColor);
        Toolkit.printString(
                name,
                startX - name.length() / 2,
                startY + 4,
                whiteColor);
        Toolkit.printString(
                "killed by a",
                startX - 6,
                startY + 5,
                whiteColor);
        Toolkit.printString(
                monster,
                startX - monster.length() / 2,
                startY + 6,
                whiteColor);
        Toolkit.printString(
                "___\\/(\\/)/(\\/ \\\\(//)\\)\\/(//)\\\\)//(\\___",
                startX - 37 / 2,
                startY * 2 - 2,
                greenColor);
        Toolkit.printString(
                "*      *        *",
                startX - 8,
                startY * 2 - 3,
                redColor);
        Toolkit.drawVerticalLine(startX - 37 / 2 + 3, startY * 2 - 3, startY - 3, yellowColor);
        Toolkit.drawVerticalLine(startX + 37 / 2 - 3, startY * 2 - 3, startY - 3, yellowColor);
        Toolkit.drawHorizontalLine(startX - 37 / 2 + 3, startY - 3, startX + 37 / 2 - 3, yellowColor);
        Toolkit.drawCorner(startX - 37 / 2 + 3, startY - 3, startX - 37 / 2 + 3, startY - 3, yellowColor, UL_CORNER);
        Toolkit.drawCorner(startX + 37 / 2 - 3, startY - 3, startX + 37 / 2 - 3, startY - 3, yellowColor, UR_CORNER);

        Toolkit.endPainting();

        InputChar space = new InputChar(' ');
        InputChar input = Toolkit.readCharacter();
        while (!input.equals(space)) {
            input = Toolkit.readCharacter();
        }

    }

    public static void finish_success_menu() {
        int startX = 1;
        int finishY = SCREEN_HEIGHT;
        Toolkit.startPainting();
        Toolkit.clearScreen(blackColor);
        Toolkit.printString(
                "Congratulations!",
                startX,
                finishY - 13,
                yellowColor);
        Toolkit.printString(
                "You have made it to the light of day!",
                startX,
                finishY - 12,
                yellowColor);
        Toolkit.printString(
                "Your journey home and sell your",
                startX,
                finishY - 8,
                yellowColor);
        Toolkit.printString(
                "loot at a great profit and are",
                startX,
                finishY - 7,
                yellowColor);
        Toolkit.printString(
                "admitted to the fighters guild.",
                startX,
                finishY - 6,
                yellowColor);
        Toolkit.printString(
                "--Press space to continue--",
                startX,
                finishY - 3,
                yellowColor);
        Toolkit.endPainting();

        InputChar space = new InputChar(' ');
        InputChar input = Toolkit.readCharacter();
        while (!input.equals(space)) {
            input = Toolkit.readCharacter();
        }

    }

    public static void output_global_stats(int offsetX, int offsetY) {
        final int SCREEN_WIDTH = Toolkit.getScreenWidth();
        final int SCREEN_HEIGHT = Toolkit.getScreenHeight();

        final int MAX_WIDTH = SCREEN_WIDTH - offsetX - 2;
        final int MAX_LINES_PER_PAGE = SCREEN_HEIGHT - offsetY - 3;

        InputChar space = new InputChar(' ');
        final String STATS_PATH = "global_stats.txt";

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(STATS_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading stats: " + e.getMessage());
            return;
        }

        if (lines.isEmpty()) {
            System.err.println("No statistics available");
            return;
        }

        int currentLine = 0;
        boolean shouldContinue = true;

        while (shouldContinue && currentLine < lines.size()) {
            Toolkit.startPainting();
            Toolkit.clearScreen(blackColor);

            int linesDisplayed = 0;
            while (currentLine < lines.size() && linesDisplayed < MAX_LINES_PER_PAGE) {
                String line = lines.get(currentLine);
                if (line.length() > MAX_WIDTH) {
                    line = line.substring(0, MAX_WIDTH);
                }

                Toolkit.printString(line, offsetX, offsetY + linesDisplayed, yellowColor);
                linesDisplayed++;
                currentLine++;
            }

            if (currentLine < lines.size()) {
                String prompt = "Click -space- to continue or Q to quit";
                Toolkit.printString(prompt, offsetX, SCREEN_HEIGHT - 2, yellowColor);
            } else {
                String prompt = "Click any key to exit";
                Toolkit.printString(prompt, offsetX, SCREEN_HEIGHT - 2, yellowColor);
            }

            Toolkit.endPainting();

            if (currentLine < lines.size()) {
                InputChar input = Toolkit.readCharacter();
                if (input.getCharacter() == 'q' || input.getCharacter() == 'Q') {
                    shouldContinue = false;
                } else if (!input.equals(space)) {
                    while (true) {
                        input = Toolkit.readCharacter();
                        if (input.equals(space)) break;
                        if (input.getCharacter() == 'q' || input.getCharacter() == 'Q') {
                            shouldContinue = false;
                            break;
                        }
                    }
                }
            } else {
                Toolkit.readCharacter();
                shouldContinue = false;
            }
        }
    }

    public static void render(char[][] field, int[][] fog) {
        transformation_matrix(field);

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {
                if (fog[i][j] == 0) {
                    field[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {

                Enum_elements element = Enum_elements.fromSign(field[i][j]);
                element.drawChar(i, j);
            }
        }
    }
}