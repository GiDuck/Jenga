
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.util.session.MemberSession;
import hi.im.jenga.util.session.SessionValidate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.http.HttpSession;

public class SessionTest {

    public static Logger logger = LoggerFactory.getLogger(SessionTest.class);
    public MockServletContext testServletContext = new MockServletContext();
    public HttpSession session = new MockHttpSession(testServletContext);

    @Before
    public void setUp(){

        logger.info("Session Test :: Data injection starting... ");

        MemberDTO memberDTO = (MemberDTO)TestUtil.getMockSession().getAttribute(TestUtil.TEST_SESSION_KEY);
        logger.info("Session Test :: inject result .. " + memberDTO.toString());


    }

    @Ignore
    @Test
    public void testSessionEmpty(){

        session.removeAttribute(TestUtil.TEST_SESSION_KEY);
        Assert.assertTrue("Session이 Null이 아님", SessionValidate.isSessionEmpty(session, TestUtil.TEST_SESSION_KEY));

    }

    @Ignore
    @Test
    public void testSessionNotEmpty(){

        Assert.assertFalse("Session이 Null임", SessionValidate.isSessionEmpty(session, TestUtil.TEST_SESSION_KEY));


    }


    @Ignore
    @Test
    public void testGetSessionValue(){
        Assert.assertNotNull("가져온 값이 없음", SessionValidate.getValidSessionObj(session, TestUtil.TEST_SESSION_KEY) );
        logger.info("Session Test :: session value is.. " + SessionValidate.getValidSessionObj(session, TestUtil.TEST_SESSION_KEY).toString());
    }

    @Ignore
    @Test
    public void testGetSessionValueNotValid(){
        Assert.assertNotNull("가져온 값이 없음", SessionValidate.getValidSessionObj(session, "test") );
    }

    @Ignore
    @Test
    public void testSessionUserUidCheck(){

        Assert.assertTrue("현재 세션에 있는 사용자와 uid가 같지 않음." ,
                SessionValidate.compareValueIsEquals(session, "test1234", new MemberSession()));

    }
    @Ignore
    @Test
    public void testGetOneValueToString(){

        Assert.assertEquals(TestUtil.TEST_SESSION_UID,
                new MemberSession().getMemberUid(session));

    }



}
