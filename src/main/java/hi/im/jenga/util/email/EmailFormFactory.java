package hi.im.jenga.util.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailFormFactory implements AbstractEmailFormFactory{

    @Autowired
    @Qualifier("tempPasswordEmailForm")
    private EmailForm tempPasswordEmailForm;

    @Autowired
    @Qualifier("authEmailEmailForm")
    private EmailForm authKeyEmailForm;


    @Override
    public String publish(EmailFormType type, Map<String ,Object> paramContainer) {

        EmailForm emailForm = null;

        if(EmailFormType.TEMP_PASSWORD_EMAIL.equals(type)){

            emailForm = tempPasswordEmailForm;

        }else if(EmailFormType.AUTH_EMAIL.equals(type)){

            emailForm = authKeyEmailForm;

        }

        emailForm.setUp(paramContainer);
        return emailForm.getHtml();
    }
}
