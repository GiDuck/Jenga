import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.service.MemberService;
import hi.im.jenga.util.email.EmailFormFactory;
import hi.im.jenga.util.email.EmailFormType;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml","file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml", "file:src/main/webapp/WEB-INF/spring/mongoConfig.xml"})
@WebAppConfiguration
public class MemberTest {


    private final HttpServletRequest request = new MockHttpServletRequest();
    private final HttpServletResponse response = new MockHttpServletResponse();
    private final HttpSession session = new MockHttpSession();
    private final Model model = new ConcurrentModel();
    public static final Logger logger = LoggerFactory.getLogger(MemberTest.class);

    @Autowired
    public MemberService memberService;



    @Before
    public void setUpTest() {
        logger.info("Logger :: Start test on MemberTest... ");
    }


    @Ignore
    @Test
    public void testJoinMemberByEmail(){


    }


    @Ignore
    @Test
    public void testCheckAuthByEmail() {
        EmailMemberDTO dto = new EmailMemberDTO();
        dto.setEm_id("gdtbgl93@gmail.com");
        dto.setEm_pwd("godqhrgkek93@");
        Assert.assertEquals("success", memberService.checkEmail(dto));
    }

    @Test
    public void testFindPwd(){

        Assert.assertEquals(1, memberService.findEPwd("gdtbgl93@gmail.com"));


    }




}
