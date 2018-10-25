package hi.im.jenga.member.util.login;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Util {

    public String getIuid(){
        return UUID.randomUUID().toString();
    }
}
