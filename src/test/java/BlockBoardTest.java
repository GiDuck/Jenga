
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
        "file:src/main/webapp/WEB-INF/spring/mongo-config.xml"})
public class BlockBoardTest {

    public static Logger logger = LoggerFactory.getLogger(BlockBoardTest.class);

    @Autowired
    private WebApplicationContext wac;


    private MockMvc mockMvc;
    private MockHttpSession session;


    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        MemberDTO member = new MemberDTO();
        //member.setMem_iuid();
        //session.setAttribute("Member", );
        logger.info("Set up....");
    }

    @Test
    public void testLike() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/board/like/a824a15b-10f5-4da2-8573-046972842cbf"));

    }



}
