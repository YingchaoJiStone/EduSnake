package com.edusnake;

import java.io.File;

public enum Direction {

    RIGHT(new File(FilePath.RIGHTIMAGE.path).toURI().toString()),
    LEFT(new File(FilePath.LEFTIMAGE.path).toURI().toString()),
    UP(new File(FilePath.UPIMAGE.path).toURI().toString()),
    DOWN(new File(FilePath.DOWNIMAGE.path).toURI().toString()),

    RIGHT2(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "snake2right.png").toURI().toString()),
    LEFT2(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "snake2left.png").toURI().toString()),
    UP2(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "snake2up.png").toURI().toString()),
    DOWN2(new File("src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "com" + File.separator + "edusnake" + File.separator + "image" + File.separatorChar + "snake2down.png").toURI().toString());

    String path;
    Direction(String path) {
        this.path = path;
    }
}
