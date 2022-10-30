package com.wixossdeckbuilder.backendservice.model.enums;


public enum Colors {
    BLACK,
    BLUE,
    GREEN,
    RED,
    WHITE,
    COLORLESS,
    ANY;

    public static Colors matchEnum(String type) {
        if (type.equalsIgnoreCase("null")) {
            return Colors.ANY;
        }
        for (Colors color : Colors.values()) {
            if (color.name().equalsIgnoreCase(type)) {
                return color;
            }
        }
        return null;
    }
}
