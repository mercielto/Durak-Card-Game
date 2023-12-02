package com.example.cardgame.properties;

public enum SearchTypeProperty {
    ROOM_NAME("By room name", "1"), ROOM_ID("By room id", "2"), GET_ALL("", "0");

    private final String value;
    private final String requestValue;

    SearchTypeProperty(String value1, String req) {
        value = value1;
        requestValue = req;
    }


    public String getValue() {
        return value;
    }

    public static SearchTypeProperty define(String obj) {
        for (SearchTypeProperty property : values()) {
            if (property.value.equals(obj)) {
                return property;
            }
        }
        throw new RuntimeException();
    }

    public static SearchTypeProperty defineByRequestValue(String obj) {
        for (SearchTypeProperty property : values()) {
            if (property.requestValue.equals(obj)) {
                return property;
            }
        }
        throw new RuntimeException();
    }

    public String getRequestValue() {
        return requestValue;
    }
}
