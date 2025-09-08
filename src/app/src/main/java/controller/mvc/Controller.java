package controller.mvc;

import datalayer.data.Session;
import domain.entities.creatures.enemies.Enemy;
import domain.entities.items.subjects.weapon.Weapon;
import domain.entities.utils.ConstMove;
import domain.map.Level;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import sun.misc.Signal;
import view.curses.Render;

import java.util.ArrayList;

import static jcurses.system.InputChar.*;
import static view.curses.Render.*;


public class Controller {

    private static final int SCREEN_HEIGHT = Toolkit.getScreenHeight();
    private static final int SCREEN_WIDTH = Toolkit.getScreenWidth();
    private static final int offsetX = 5;
    private static final int offsetY = 5;

    private static final char[][] field = new char[SCREEN_WIDTH][SCREEN_HEIGHT];
    private static final int[][] fog = new int[SCREEN_WIDTH][SCREEN_HEIGHT];
    private static String message = "";
    private static String message_monster = "";
    private static boolean exitRequested = false;


    public static void matrix_transformation() {
        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {
                int targetX = i + offsetX;
                int targetY = j + offsetY;

                if (targetX < SCREEN_WIDTH && targetY < SCREEN_HEIGHT) {
                    if (j < Level.MAP_WIDTH && i < Level.MAP_HEIGHT) {
                        field[targetX][targetY] = Session.currenrLevel.mapBuf[j][i];
                    } else {
                        field[targetX][targetY] = ' ';
                    }
                }

            }
        }

        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {
                int targetX = i + offsetX;
                int targetY = j + offsetY;

                if (targetX < SCREEN_WIDTH && targetY < SCREEN_HEIGHT) {
                    if (j < Level.MAP_WIDTH && i < Level.MAP_HEIGHT) {
                        fog[targetX][targetY] = Session.currenrLevel.warFog[j][i];
                    } else {
                        fog[targetX][targetY] = 0;
                    }
                }

            }
        }


    }

    public static void stats_output() {
        boolean flag_mes_mos = false;
        boolean flag_mes = false;
        Toolkit.startPainting();
        Toolkit.clearScreen(blackColor);

        Render.render(field, fog);
        if (!message.isEmpty()) {
            Toolkit.startPainting();
            Toolkit.printString(message, 2, 2, whiteColor);
            flag_mes = true;
            message = "";
        }
        if (!message_monster.isEmpty()) {
            Toolkit.startPainting();
            Toolkit.printString(message_monster, 2, 1, whiteColor);
            flag_mes_mos = true;
            message_monster = "";
        }
        Render.stats(offsetX, SCREEN_HEIGHT - offsetY, Session.levelNum, Session.currenrLevel.character.getHealth(), Session.currenrLevel.character.getMaxHealth(), Session.currenrLevel.character.getStrength(), Session.currenrLevel.character.getAgility(), Session.currenrLevel.character.getGold(), getActiveWeaponStrength(Session.currenrLevel.character.getInventory().getWeapons()), Session.currenrLevel.character.getScore());

        if (flag_mes_mos || flag_mes) {

            Toolkit.printString("Click -Space- to continue", 2, 4, whiteColor);
            Toolkit.endPainting();

            InputChar space = new InputChar(' ');
            InputChar input = Toolkit.readCharacter();
            while (!input.equals(space)) {
                input = Toolkit.readCharacter();
            }
            Toolkit.startPainting();
            clear_string(2, 1);
            clear_string(2, 2);
            clear_string(2, 4);
            Toolkit.endPainting();
        } else {
            Toolkit.endPainting();
        }
    }

    public static int getActiveWeaponStrength(ArrayList<Weapon> inventory) {
        for (Weapon weapon : inventory) {
            if (weapon.isActiveFl()) {
                return weapon.getStrength();
            }
        }
        return 0;
    }


    public static void clear_string(int x, int y) {
        Toolkit.startPainting();
        StringBuilder empty = new StringBuilder();
        int i = 0;
        while (i < SCREEN_WIDTH) {
            empty.append(' ');
            i++;
        }
        Toolkit.printString(empty.toString(), x, y, whiteColor);
        Toolkit.endPainting();
    }

    private static void moving(ConstMove move) {
        message = Session.currenrLevel.character.tryMove(move);
        Session.currenrLevel.update();
        for (Enemy enemy : Session.currenrLevel.enemies) {
            message_monster = enemy.tryMove();
            Session.currenrLevel.update();
        }
    }


    public static void matrix_update() {
        InputChar input = Toolkit.readCharacter();


        if (input.isSpecialCode()) {
            if (KEY_UP == input.getCode()) {
                moving(ConstMove.UP);
            } else if (KEY_DOWN == input.getCode()) {
                moving(ConstMove.DOWN);
            } else if (KEY_LEFT == input.getCode()) {
                moving(ConstMove.LEFT);
            } else if (KEY_RIGHT == input.getCode()) {
                moving(ConstMove.RIGHT);
            }
        } else {
            char c = input.getCharacter();
            if (c == 'q' || c == 'Q') {
                Session.recordGlobalStats("quit", null);
                exitRequested = true;
            } else if ((c == 'w') || (c == 'W')) {
                moving(ConstMove.UP);
            } else if ((c == 's') || (c == 'S')) {
                moving(ConstMove.DOWN);
            } else if ((c == 'a') || (c == 'A')) {
                moving(ConstMove.LEFT);
            } else if ((c == 'd') || (c == 'D')) {
                moving(ConstMove.RIGHT);
            } else if (c == '?') {
                Render.help(offsetX, offsetY);
            } else if (c == 'k') {
                message = Render.subjects(offsetX, offsetY, Session.currenrLevel.character.getInventory().getElixirs());
            } else if (c == 'j') {
                message = Render.subjects(offsetX, offsetY, Session.currenrLevel.character.getInventory().getFood());
            } else if (c == 'e') {
                message = Render.subjects(offsetX, offsetY, Session.currenrLevel.character.getInventory().getScrolls());
            } else if (c == 'h') {
                message = Render.weapon(offsetX, offsetY, Session.currenrLevel.character.getInventory().getWeapons());
            }
//            else if (c == 'z'){
//                Session.currenrLevel.character.changeLevelFl();
//            }
        }

        if (Session.currenrLevel.character.isLvlFl()) {
            Session.currenrLevel.character.changeLevelFl();
            Toolkit.startPainting();
            Toolkit.clearScreen(blackColor);
            Session.nextLevel();
            Toolkit.endPainting();


        }
    }


    private static void updateGameState() {
        matrix_transformation();
        stats_output();
        matrix_update();
    }

    private static String name_last_monster(char last_monster) {
        String monster = "";
        if (last_monster == 'Z') {
            monster = "zombie";
        } else if (last_monster == 'V') {
            monster = "vampire";
        } else if (last_monster == 'G') {
            monster = "ghost";
        } else if (last_monster == 'O') {
            monster = "ogre";
        } else if (last_monster == 'S') {
            monster = "snake";
        } else if (last_monster == 'M') {
            monster = "mimic";
        }

        return monster;

    }


    public static void controller() {

        System.out.print("\u001B[?25l");
        Toolkit.init();

        Signal.handle(new Signal("INT"), // SIGINT
                signal -> {
                    Toolkit.shutdown();
                    System.out.println("Interrupted with ctrl+c");
                    System.exit(2);
                });

        // Проверка инициализации


        while (!Session.getFlagSave()) {
            int start_or_load = new_or_save_game();
            Session.setCurrentUser(start_menu(), start_or_load);
            new Session();
        }
        success_load(Session.getCurrentUser());
        Level currentLevel = Session.currenrLevel;
        if (currentLevel == null) {
            System.err.println("Ошибка: уровень не создан");
            return;
        }

        while (!exitRequested && !Session.currenrLevel.character.isDead() && Session.levelNum <= 21) {
            updateGameState();
            if (Session.currenrLevel.character.isDead() || Session.levelNum == 22) {
                if (Session.currenrLevel.character.isDead()) {
                    char lastMonster = ' ';
                    for (Enemy enemy : Session.currenrLevel.enemies) {
                        if (enemy.getLastMonster() != ' ') {
                            lastMonster = enemy.getLastMonster();
                            break;
                        }
                    }
                    String name_monster = name_last_monster(lastMonster);
                    Session.recordGlobalStats("killed by a ", name_monster);
                    Session.deleteSaveFile();
                    Render.finish_death_menu(Session.getCurrentUser(), name_monster);

                } else if (Session.levelNum == 22) {
                    Session.levelNum--;
                    Session.curStats.level--;
                    Session.recordGlobalStats("won", null);
                    Session.deleteSaveFile();
                    Render.finish_success_menu();
                }
                Toolkit.startPainting();
                Toolkit.clearScreen(blackColor);
                Toolkit.printString(Session.curStats.toString(), offsetX, offsetY, yellowColor);
                Toolkit.printString("Click -space- to continue", offsetX, SCREEN_HEIGHT - 3, yellowColor);
                Toolkit.endPainting();
                InputChar space = new InputChar(' ');
                InputChar input = Toolkit.readCharacter();
                while (!input.equals(space)) {
                    input = Toolkit.readCharacter();
                }
                output_global_stats(offsetX, offsetY);
                break;
            }


        }


        Toolkit.shutdown();
        System.out.print("\u001B[?25h");
    }


}
