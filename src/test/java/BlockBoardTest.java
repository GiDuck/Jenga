
import hi.im.jenga.board.controller.BoardController;
import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.member.dto.MemberDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private BoardService service;


    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        MemberDTO member = new MemberDTO();
        //member.setMem_iuid();
        //session.setAttribute("Member", );
        logger.info("Set up....");
    }

    @Ignore
    @Test
    public void likeTest() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/board/like/a824a15b-10f5-4da2-8573-046972842cbf"));

    }

    @Ignore
    @Test
    public void getMyBlocksTest() throws Exception{

        List<Map<String, Object>> result = service.getMyBlock("s9QNmieeutgR/WYIWhdL+aP25vpoPgeK0zToHnb0o5go7yCc1BOGiPJgUk/GPukC");
        Assert.assertNotNull(result);
        System.out.println(result);


    }

    @Ignore
    @Test
    public void getMyFavoriteBlockTest() throws Exception{
        System.out.println(service.getMyFavoriteBlocks("s9QNmieeutgR/WYIWhdL+aP25vpoPgeK0zToHnb0o5go7yCc1BOGiPJgUk/GPukC"));

    }


    @Test
    public void getMyFavoirteBlockWithParamTest() throws Exception{
        String searchOps = "keyword";
        String category =  null;
        String keyword = "북마크";
        String memUid = "s9QNmieeutgR/WYIWhdL+aP25vpoPgeK0zToHnb0o5go7yCc1BOGiPJgUk/GPukC";

        System.out.println(service.getMyFavoirteBlockWithParam(memUid, searchOps, category, keyword));


    }



}
