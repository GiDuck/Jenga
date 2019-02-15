import hi.im.jenga.member.dto.MemberDTO;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

public class TestUtil {


    public static final String TEST_SESSION_UID = "test1234";
    public static final String TEST_SESSION_NICK = "scott";
    public static final String TEST_SESSION_KEY = "Member";

    public static HttpSession getMockSession(){

        HttpSession session = new MockHttpSession();
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMem_iuid(TEST_SESSION_UID);
        memberDTO.setMem_nick(TEST_SESSION_NICK);
        session.setAttribute(TEST_SESSION_KEY, memberDTO);
        return session;
    }
}
