package hi.im.jenga.util.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Map;

@Component("authEmailEmailForm")
public class AuthKeyEmailForm extends EmailForm {

    @Value("#{props['auth_key']}")
    private String filePath;

    @Override
    public void setUp(Map<String, Object> paramContainer) {

        try {
            setData(filePath);
            if(paramContainer != null){
                injectHtmlById("pwd", (String)paramContainer.get("pwd"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
