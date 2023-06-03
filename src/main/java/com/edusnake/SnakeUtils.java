package com.edusnake;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class SnakeUtils {

    public static final int unitSize = 30; // The size of each cell in the game, for example, snake head and food all are 25px
    public static double topBarHeight = 40; // topBar's height
    public static double width = unitSize * 40; // Main interface's width
    public static double height = unitSize * 26 + topBarHeight; // Main interface's height
    public static  String[] dictionary = readWordFile(FilePath.WORDFILE.path);// Read words from file

    public static void moveOneStep(Snake snake) { // Snake moved one step forward
        for (int i = snake.getLength(); i > 0; i--) {//Move the data for each index in the snake array forward
            snake.getSnakeX()[i] = snake.getSnakeX()[i - 1];
            snake.getSnakeY()[i] = snake.getSnakeY()[i - 1];
        }
    }

    public static void snakeRollBack(Snake snake) {// Snake took a step back
        for (int i = 0; i < snake.getLength() + 1; i++) {//Move the data for each index in the snake array back
            snake.getSnakeX()[i] = snake.getSnakeX()[i + 1];
            snake.getSnakeY()[i] = snake.getSnakeY()[i + 1];
        }
    }


    public static String readFile(String path) { // Read data from a file
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Reader reader = new FileReader(path);
            int index;
            while ((index = reader.read()) != -1) {
                stringBuilder.append(String.valueOf((char) index));
            }
            reader.close();
            return stringBuilder.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeFile(String path, String item) { // Write the data to a file
        try {
            File levelFile = new File(path);
            if (levelFile.exists()) {
                levelFile.createNewFile(); // Delete original file and create a new file that has same name
            }
            Writer writer = new FileWriter(levelFile);
            writer.write(item);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String[] readWordFile(String path) {//Reading content from files and return a word array
        String str = readFile(path);
        return str.split(System.lineSeparator());
    }

    public static int getLevel() { // Each difficulty level corresponds to a different game speed
        int period = 0;
        String level = readFile(FilePath.LEVELFILE.path);
        switch (level) {
            case "Easy" -> {
                period = 220;
            }
            case "Normal" -> {
                period = 180;
            }
            case "Hard" -> {
                period = 130;
            }
            case "Master" -> {
                period = 100;
            }
            case "Extreme" -> {
                period = 70;
            }
        }

        return period;
    }

    public static MusicControl getMusic() {
        MusicControl music = null;
        String musicName = readFile(FilePath.MUSICFILE.path);
        switch (musicName) {
            case "SuperMario" -> {
                music = new MusicControl(new File(FilePath.SUPERMARIOMAIN.path).toURI().toString(),
                        new File(FilePath.SUPERMARIOCOIN.path).toURI().toString(),
                        new File(FilePath.SUPERMARIODEATH.path).toURI().toString());
            }
            case "GoKarts" -> {
                music = new MusicControl(new File(FilePath.GOKARTS.path).toURI().toString(),
                        new File(FilePath.SUPERMARIOCOIN.path).toURI().toString(),
                        new File(FilePath.STRIKE.path).toURI().toString());
            }
            case "LightAndLively" -> {
                music = new MusicControl(new File(FilePath.LIGHTANDLIVELY.path).toURI().toString(),
                        new File(FilePath.SUPERMARIOCOIN.path).toURI().toString(),
                        new File(FilePath.STRIKE.path).toURI().toString());
            }
            case "Mute" -> {
                music = new MusicControl();
            }

        }
        return music;
    }


}
