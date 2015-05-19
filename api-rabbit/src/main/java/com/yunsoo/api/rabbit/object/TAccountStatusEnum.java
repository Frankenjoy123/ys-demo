package com.yunsoo.api.rabbit.object;

public enum TAccountStatusEnum {
    UNDEFINED("UNDEFINED"),
    ENABLED("ENABLED"),
    VERIFIED("VERIFIED"),
    LOCKED("LOCKED"),
    EXPIRED("EXPIRED"),
    TOKEN_EXPIRED("TOKEN_EXPIRED"),
    INVALID_TOKEN("INVALID_TOKEN"),
    ANONYMOUS("ANONYMOUS"),
    DELETED("DELETED");

    private String value = "";

    private TAccountStatusEnum(String value) {
        this.value = value;
    }

//    public static TAccountStatusEnum valueOf(String value) {
//        switch (value) {
//            case "UNDEFINED":
//                return UNDEFINED;
//            case "ENABLED":
//                return ENABLED;
//            case "LOCKED":
//                return LOCKED;
//            case "EXPIRED":
//                return EXPIRED;
//            case "TOKEN_EXPIRED":
//                return TOKEN_EXPIRED;
//            case "INVALID_TOKEN":
//                return INVALID_TOKEN;
//            case "ANONYMOUS":
//                return ANONYMOUS;
//            case "DELETED":
//                return DELETED;
//            default:
//                return UNDEFINED;
//        }
//    }

    public String value() {
        return this.value;
    }
}
