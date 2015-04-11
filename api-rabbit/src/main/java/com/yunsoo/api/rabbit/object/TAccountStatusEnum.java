package com.yunsoo.api.rabbit.object;

public enum TAccountStatusEnum {
    UNDEFINED(0),
    ENABLED(1),
    LOCKED(2),
    EXPIRED(3),
    TOKEN_EXPIRED(4),
    INVALID_TOKEN(5),
    ANONYMOUS(6);

    private int value = 0;

    private TAccountStatusEnum(int value) {
        this.value = value;
    }

    public static TAccountStatusEnum valueOf(int value) {
        switch (value) {
            case 0:
                return UNDEFINED;
            case 1:
                return ENABLED;
            case 2:
                return LOCKED;
            case 3:
                return EXPIRED;
            case 4:
                return TOKEN_EXPIRED;
            case 5:
                return INVALID_TOKEN;
            case 6:
                return ANONYMOUS;
            default:
                return UNDEFINED;
        }
    }

    public int value() {
        return this.value;
    }
}
