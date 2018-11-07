package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BoardDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BoardDAOImpl implements BoardDAO {

    private static final Logger logger = LoggerFactory.getLogger(BoardDAOImpl.class);

    private final SqlSession sqlSession;


    public BoardDAOImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public void writeViewPOST(BoardDTO boardDTO, String session_iuid) {
        Map<String, Object> map = new HashMap();
        map.put("boardDTO", boardDTO);
        map.put("session_iuid", session_iuid);
        logger.info("뭐야 너 "+boardDTO.getBl_smCtg());
        logger.info(" 맵 " + map.get("boardDTO"));
        logger.info(" 맵 " + map.get("session_iuid"));
        logger.info("dddd"+map.size());
        sqlSession.insert("board.writeViewPOST","map");
    }
}
