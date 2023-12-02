package com.example.cardgame.properties;

public class ServerProperties {
    private static final String host = "localhost";
    private static final Integer port = 8889;
    private static final String mainDelimiter = "@";
    private static final String sideDelimiter = "~";

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
}
