package com.example.cardgame.properties;

public class ServerProperties {
    private static final String host = "localhost";
    private static final Integer port = 8889;
    private static final String mainDelimiter = "@";
    private static final String sideDelimiter = "~";
    private static final String storagePath = "C:\\Works\\Durak Card Game" +
            "\\src\\main\\resources\\com\\example\\cardgame\\client\\application\\images\\";
    private static final String cardImagesStoragePath = storagePath + "cards\\";
    private static final String avatarImagesStoragePath = storagePath + "avatar\\";

    public static String getHost() {
        return host;
    }

    public static Integer getPort() {
        return port;
    }

    public static String getMainDelimiter() {
        return mainDelimiter;
    }

    public static String getSideDelimiter() {
        return sideDelimiter;
    }

    public static String getStoragePath() {
        return storagePath;
    }

    public static String getCardImagesStoragePath() {
        return cardImagesStoragePath;
    }

    public static String getAvatarImagesStoragePath() {
        return avatarImagesStoragePath;
    }
}
