package hi.im.jenga.util.status_code;

import hi.im.jenga.util.SimpleEnumModel;

public enum BlockStatusCode implements SimpleEnumModel {

    BLOCK_STACK_SUCCESS(401, "BLOCK_STACK_SUCCESS"),
    BLOCK_STACK_FAIL(402, "BLOCK_STACK_FAIL"),
    BLOCK_MOD_SUCCESS(403, "BLOCK_MOD_SUCCESS"),
    BLOCK_MOD_FAIL(404, "BLOCK_MOD_FAIL"),
    BLOCK_DEL_SUCCESS(405, "BLOCK_DEL_SUCCESS"),
    BLOCK_DEL_FAIL(406, "BLOCK_DEL_FAIL"),
    LIKE_SUCCESS(407, "LIKE_SUCCESS"),
    LIKE_FAIL(408, "LIKE_FAIL"),
    LIKE_EXISTS(409, "LIKE_EXISTS"),
    LIKE_NOT_EXISTS(410, "LIKE_NOT_EXISTS"),
    FOLLOW_SUCCESS(411, "FOLLOW_SUCCESS"),
    FOLLOW_FAIL(412, "FOLLOW_FAIL"),
    FOLLOW(413, "FOLLOW"),
    NOT_FOLLOW(414, "NOT_FOLLOW");


    private int code;
    private String key;

    BlockStatusCode(int code, String key) {
        this.code = code;
        this.key = key;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public int getCode() {
        return 0;
    }
}
