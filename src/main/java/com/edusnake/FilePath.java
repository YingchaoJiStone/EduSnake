package com.edusnake;

import java.io.File;

public enum FilePath {

    UPIMAGE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "up.png"),
    DOWNIMAGE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "down.png"),
    LEFTIMAGE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "left.png"),
    RIGHTIMAGE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "right.png"),
    TITLE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separator + "edusnakeTitle.png"),
    LEVELFILE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "file" + File.separator + "levelFile"),
    SUBJECTFILE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "file" + File.separator + "subjectFile"),
    MUSICFILE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "file" + File.separator + "musicFile"),
    TWOPLAYERFILE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "file" + File.separator + "twoPlayerFile"),
    SINGLEGAMEFILE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "file" + File.separator + "singleGameFile"),
    SUPERMARIOMAIN("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "music" + File.separator + "SuperMarioMain.mp3"),
    SUPERMARIOCOIN("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "music" + File.separator + "SuperMarioCoin.mp3"),
    SUPERMARIODEATH("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "music" + File.separator + "SuperMarioDeath.mp3"),
    GOKARTS("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "music" + File.separator + "GoKarts.mp3"),
    LIGHTANDLIVELY("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "music" + File.separator + "LightAndLively.mp3"),
    CLICKBUTTON("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "music" + File.separator + "ClickButton.mp3"),
    STRIKE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "music" + File.separator + "Strike.mp3"),
    WRONGLETTER("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "music" + File.separator + "WrongLetter.mp3"),
    WORDFILE("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "file" + File.separator + "wordFile.txt");


    String path;

    FilePath(String path) {
        this.path = path;
    }
}
