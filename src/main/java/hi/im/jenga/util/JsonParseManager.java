package hi.im.jenga.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParseManager
{

    public static ObjectMapper mapper = new ObjectMapper();
    public static String parseToString(Object origin){

        try{
            return mapper.writeValueAsString(origin);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
