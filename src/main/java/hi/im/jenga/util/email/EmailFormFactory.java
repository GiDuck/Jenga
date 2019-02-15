package hi.im.jenga.util.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EmailFormFactory implements AbstractEmailFormFactory{

    @Autowired
    @Qualifier("tempPasswordEmailForm")
    private EmailForm tempPasswordEmailForm;

    @Override
    public String publish(EmailFormType type) {

        EmailForm emailForm = null;

        if(EmailFormType.TEMP_PASSWORD_EMAIL.equals(type)){

            emailForm = tempPasswordEmailForm;


        }

        emailForm.test();
        return null;
//        return emailForm.getHtml();
    }
}
