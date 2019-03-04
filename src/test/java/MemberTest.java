import hi.im.jenga.member.dao.MemberDAO;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.service.MemberService;
import hi.im.jenga.util.status_code.AuthStatusCode;
import hi.im.jenga.member.util.UserInfoType;
import hi.im.jenga.util.EnumManager;
import hi.im.jenga.util.EnumValue;
import hi.im.jenga.util.session.MemberSession;
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
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml","file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml", "file:../../main/webapp/WEB-INF/spring/mongo-config.xml"})
@WebAppConfiguration
public class MemberTest {


    private final HttpServletRequest request = new MockHttpServletRequest();
    private final HttpServletResponse response = new MockHttpServletResponse();
    private final HttpSession session = new MockHttpSession();
    private final Model model = new ConcurrentModel();
    public static final Logger logger = LoggerFactory.getLogger(MemberTest.class);

    @Autowired
    public MemberService memberService;

    @Autowired
    public MemberDAO memberDao;


    @Before
    public void setUpTest() {

        logger.info("Logger :: Start test on MemberTest... ");
        logger.info("Logger :: Injecting data to Mock Session Object... ");

        MemberDTO member = new MemberDTO();
        member.setMem_iuid("8+Nt6GD5WIHt58d6Lsuc+dnoWptOmF36w8aYo9URp7b4mzEp25GyE3baD0TMQC6t");
        session.setAttribute("Member", member);

    }


    @Ignore
    @Test
    public void testEnumParse(){

        List<EnumValue> list = EnumManager.getEnumValue(AuthStatusCode.class);
         for(EnumValue e : list){
            System.out.println(e.getKey());
            System.out.println(e.getValue());
        }


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
        dto.setEm_pwd("6Y609yVp1f");
        Assert.assertEquals(AuthStatusCode.LOGIN_SUCCESS, memberService.loginCheck(dto));
    }


    @Ignore
    @Test
    public void testFindPwd(){
        Assert.assertEquals(1, memberService.findEmailPwd("gdtbgl93@gmail.com"));

    }


    @Ignore
    @Test
    public void emailCheckTest(){

        AuthStatusCode result= memberService.isEmailMemberExists("gdtbgl93@gmail.com");
        Assert.assertNotNull(result);
        System.out.println(result);

        result= memberService.isEmailMemberExists("gdtbgl@gmail.com");
        Assert.assertNotNull(result);

        result= memberService.isEmailMemberExists("gdtbgl@gmail.com");
        System.out.println(result);

    }

    @Ignore
    @Test
    public void emailJoinTest(){

        String email = "gnnovation@naver.com";
        String pwd = "godqhrgkek93@";
        AuthStatusCode result= memberService.isEmailMemberExists(email);
            EmailMemberDTO emailMemberDTO = new EmailMemberDTO();
            emailMemberDTO.setEm_id(email);
            emailMemberDTO.setEm_pwd(pwd);

            if (result == AuthStatusCode.AUTH_NOT_EXIST || result == AuthStatusCode.AUTH_NOT_VALID) {
                System.out.println(memberService.sendKey(emailMemberDTO));
            }

    }



    @Ignore
    @Test
    public void emailAuthTest(){

        String email = "gnnovation@naver.com";
        String authKey = "RV7QzbsSsn";

        EmailMemberDTO emailMemberDTO = new EmailMemberDTO();
        emailMemberDTO.setEm_id(email);
        emailMemberDTO.setEm_akey(authKey);
        AuthStatusCode status = memberService.authCheck(emailMemberDTO);
       Assert.assertEquals(AuthStatusCode.AUTH_SUCCESS , status);

    }


   @Ignore
   @Test
    public void testGetBookmarkUploadedDate(){

        Assert.assertNotNull(memberService.getBookmarkUploadDate(new MemberSession().getMemberUid(session)));

    }


    @Ignore
    @Test
    public void checkUserInfoParam(){

        List<UserInfoType> typeList = new ArrayList<>();
        typeList.add(UserInfoType.PROFILE);
        typeList.add(UserInfoType.NICK);
        typeList.add(UserInfoType.INTRODUCE);

        Assert.assertNotNull(memberService.getUserInfo(((MemberDTO)session.getAttribute("Member")).getMem_iuid(), typeList));


    }

    @Ignore
    @Test
    public void insertWhetherRegToken(){
        memberDao.insertWhetherRegInfo("6cd088b9-91f8-4eae-a5cf-c6d0434ec84b");
    }



    @Ignore
    @Test
    public void deleteWhetherRegToken(){
        Assert.assertNotEquals(0, memberDao.deleteWhetherRegInfo("6cd088b9-91f8-4eae-a5cf-c6d0434ec84b"));
    }










}
