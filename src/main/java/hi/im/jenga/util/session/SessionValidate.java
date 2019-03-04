package hi.im.jenga.util.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

public class SessionValidate {

    public static Logger logger = LoggerFactory.getLogger(SessionValidate.class);

    //세션이 비었는지 확인하는 함수. 비었으면 true
    public static boolean isSessionEmpty(HttpSession session, String key) throws IllegalArgumentException {

        if (session == null) {

            logger.warn("Session Validate :: Session이 비었습니다.");

            return true;

        } else if (session.getAttribute(key) == null) {

            logger.warn("Session Validate :: Session은 존재하지만 주어진 키 값이 비었습니다.");

            return true;

        }

        return false;


    }

    //유효한 세션을 꺼내오는 함수.
    public static Object getValidSessionObj(HttpSession session, String key){


        logger.info("session is null ? ");
        if (!isSessionEmpty(session, key)) {

            return session.getAttribute(key);

        }else{
            throw new RuntimeException("적합하지 않은 세션입니다.");

        }


    }

    public static boolean compareValueIsEquals(HttpSession session, String compareUid, SessionAction action) throws IllegalArgumentException{

       return action.equalsToSomething(session, compareUid);

    }




}
