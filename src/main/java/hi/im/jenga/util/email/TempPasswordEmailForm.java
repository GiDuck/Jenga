package hi.im.jenga.util.email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Map;

@Component("tempPasswordEmailForm")
public class TempPasswordEmailForm extends EmailForm {

    @Value("#{props['temp_password']}")
    private String filePath;

    public TempPasswordEmailForm() {
        super();
    }

    @Override
    public void setUp(Map<String ,Object> paramContainer){
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
