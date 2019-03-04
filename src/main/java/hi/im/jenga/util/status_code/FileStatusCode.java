package hi.im.jenga.util.status_code;

import hi.im.jenga.util.SimpleEnumModel;

public enum FileStatusCode implements SimpleEnumModel {

    FILE_UPLOAD_SUCCESS("FILE_UPLOAD_SUCCESS", 201), FILE_UPLOAD_FAIL("FILE_UPLOAD_FAIL", 202);

    private String key;
    private int code;


    FileStatusCode(String key, int code) {
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
