package hi.im.jenga.util;

import org.codehaus.jackson.map.util.EnumValues;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumManager {


    public static List<EnumValue> getEnumValue(Class<? extends SimpleEnumModel> e){

        return Arrays
                .stream(e.getEnumConstants())
                .map(EnumValue::new)
                .collect(Collectors.toList());

    }
}
