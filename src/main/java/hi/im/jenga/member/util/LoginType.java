package hi.im.jenga.member.util;

import hi.im.jenga.util.SimpleEnumModel;

public enum LoginType implements SimpleEnumModel {

    EMAIL("EMAIL", 20), SOCIAL("SOCIAL", 21);

    private String key;
    private int code;

    LoginType(String key, int code) {
        this.key = key;
        this.code = code;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public int getCode() {
        return code;
    }
}
