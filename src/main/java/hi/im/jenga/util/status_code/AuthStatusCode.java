package hi.im.jenga.util.status_code;


import hi.im.jenga.util.SimpleEnumModel;

public enum AuthStatusCode implements SimpleEnumModel {

    MISMATCHED_ID(101),
    MISMATCHED_PWD(102),
    NO_AUTH(103),
    AUTH_EXIST(104),
    AUTH_FAIL(105),
    AUTH_SUCCESS(106),
    LOGIN_SUCCESS(107),
    LOGIN_ERROR(108),
    LOGIN_FAIL(109),
    AUTH_NOT_EXIST(110),
    JOIN_SUCCESS(111),
    JOIN_FAIL(112),
    MOD_SUCCESS(113),
    MOD_FAIL(114),
    LOGOUT_SUCCESS(115),
    LOGOUT_FAIL(116),
    AUTH_NOT_VALID(117),
    REG_SUCCESS(118),
    REG_FAIL(119),
    REG_ALREADY_EXISTS(120);

    AuthStatusCode(int code) {
        this.code = code;
        this.key = this.name();
    }

    private int code;
    private String key;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public int getCode() {
        return code;
    }


}