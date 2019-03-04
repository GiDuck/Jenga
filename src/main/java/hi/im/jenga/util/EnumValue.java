package hi.im.jenga.util;

public class EnumValue {

    private String key;
    private int value;

    public EnumValue(SimpleEnumModel e) {
        this.key = e.getKey();
        this.value = e.getCode();
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

}
