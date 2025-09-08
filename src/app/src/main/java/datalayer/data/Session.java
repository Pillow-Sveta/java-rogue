package datalayer.data;

import domain.map.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String GLOBAL_STATS_PATH = "global_stats.txt";
    public static Level currenrLevel;
    public static int levelNum = 1;
    public static SessionStats curStats;
    private static String currentUser;
    private static String savingPath;
    private static int status_of_load;
    private static boolean flagSave = false;

    public Session() {
        flagSave = loadOrInitialize();
    }

    public static void setCurrentUser(String name, int status) {
        currentUser = name;
        savingPath = "save/" + currentUser + ".ser";
        status_of_load = status;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static boolean getFlagSave() {
        return flagSave;
    }

    public static void nextLevel() {
        if (currenrLevel == null) {
            currenrLevel = new Level();
        }
        currenrLevel.GenerateRooms();
        levelNum++;
        curStats.level++;
        saveSession();
    }

    public static void saveSession() {
        if (currenrLevel == null || curStats == null) return;

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(savingPath)))) {
            currenrLevel.count = levelNum;
            oos.writeObject(currenrLevel);
            oos.writeObject(curStats);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private static void loadSession() throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(savingPath)))) {
            currenrLevel = (Level) ois.readObject();
            curStats = (SessionStats) ois.readObject();

            if (currenrLevel == null || curStats == null) {
                throw new Exception("Файл поврежден");
            }

            levelNum = currenrLevel.count;
            currenrLevel.update();
        }
    }

    public static void deleteSaveFile() {
        File saveFile = new File(savingPath);
        if (saveFile.exists() && !saveFile.delete()) {
            System.err.println("Не удалось удалить файл сохранения");
        }
        levelNum = 1;
    }

    public static void recordGlobalStats(String status, String killerName) {
        if (curStats == null) return;

        String newLine = String.format("%d gold pieces.\t%s %s%s on level %s.\tId = %s",
                currenrLevel.character.getGold(),
                curStats.playerName,
                status,
                killerName == null ? "" : killerName,
                curStats.level,
                curStats.id
        );

        File statsFile = new File(GLOBAL_STATS_PATH);
        List<String> updatedLines = new ArrayList<>();

        // Читаем все строки, исключая строку с таким же ID
        if (statsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(statsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().endsWith(curStats.id)) {
                        updatedLines.add(line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения общей статистики: " + e.getMessage());
            }
        }

        updatedLines.add(newLine); // добавляем новую строку

        // Сохраняем обратно
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statsFile))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи общей статистики: " + e.getMessage());
        }
    }

    private boolean loadOrInitialize() {
        File saveFile = new File(savingPath);

        if (status_of_load == 2) {
            if (saveFile.exists() && saveFile.length() > 0) {
                try {
                    loadSession();
                    return true;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    return false;
                }
            } else {
                System.err.println("");
                return false;
            }

        } else {
            initNewSession();
            return true;
        }
    }

    private void initNewSession() {
        currenrLevel = new Level();
        levelNum = 1;

        curStats = new SessionStats();
        curStats.playerName = currentUser;
        curStats.level = levelNum;
        curStats.id = UUID.randomUUID().toString();

        saveSession();
    }
}

