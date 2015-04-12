package com.yunsoo.api.rabbit.object;

public enum TAccountStatusEnum {
    UNDEFINED(1),
    ENABLED(2),
    LOCKED(3),
    EXPIRED(4),
    TOKEN_EXPIRED(5),
    INVALID_TOKEN(6),
    ANONYMOUS(7);

    private int value = 0;

    private TAccountStatusEnum(int value) {
        this.value = value;
    }

    public static TAccountStatusEnum valueOf(int value) {
        switch (value) {
            case 1:
                return UNDEFINED;
            case 2:
                return ENABLED;
            case 3:
                return LOCKED;
            case 4:
                return EXPIRED;
            case 5:
                return TOKEN_EXPIRED;
            case 6:
                return INVALID_TOKEN;
            case 7:
                return ANONYMOUS;
            default:
                return UNDEFINED;
        }
    }

    public int value() {
        return this.value;
    }
}
