package me.th.system.security.enums;

import lombok.Getter;

@Getter
public enum RequestMethodMode {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    ALL("ALL"),
    ;

    private final String type;

    RequestMethodMode(String type) {
        this.type = type;
    }

    public static RequestMethodMode find(String type) {
        for (RequestMethodMode value : RequestMethodMode.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return ALL;
    }
}
