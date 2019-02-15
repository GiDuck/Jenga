
import hi.im.jenga.board.dto.BoardDTO;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

public class BlockBoardTest {

    public static Logger logger = LoggerFactory.getLogger(BlockBoardTest.class);
    public HttpSession session;

    private void setMockBlockObject(){


    }


    @Before
    public void setUp(){
        logger.info("Starting Block Board Testing... being setUp...");
        session = TestUtil.getMockSession();
        logger.info("Session Object Load Complete..");

    }

    @Test
    public void testInsertBlock(){

        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBl_mainCtg("");



    }


    @Test
    public void testGetBlock(){


    }

    @Test
    public void testModifyBlock(){


    }

    @Test
    public void testDeleteBlock(){


    }



}
