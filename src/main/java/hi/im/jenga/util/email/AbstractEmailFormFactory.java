package hi.im.jenga.util.email;

import java.util.Map;

public interface AbstractEmailFormFactory {

    public abstract String publish(EmailFormType type, Map<String ,Object> paramContainer);

}
