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

    public void writeViewBlock(BoardDTO boardDTO, String session_iuid) {
        Map<String, Object> map = new HashMap();

        map.put("boardDTO", boardDTO);
        map.put("session_iuid", session_iuid);


        sqlSession.insert("board.writeViewBlock", map);
    }

    public void writeViewThumbImg(String bl_uid, String uploadName) {
        Map<String, String> map = new HashMap();

        map.put("uploadName", uploadName);
        map.put("bl_uid", bl_uid);

        sqlSession.insert("board.writeViewThumbImg", map);
    }

    public void writeViewTag(String bl_uid, String[] bt_name) {
        Map<String, String> map = new HashMap();

        map.put("bl_uid", bl_uid);

        for(String tag: bt_name){
            map.put("tag", tag);
            sqlSession.insert("board.writeViewTag", map);
        }
    }

//    블록정보들, 썸네일 JOIN으로 뽑고
//    태그는 []이니까 따로 뽑고
    public HashMap modifyViewGET(String bl_uid) {
        Map<String, String []> map = new HashMap();

        String [] info1 = sqlSession.selectOne("board.modifyViewGET", bl_uid);
        String [] info2 = sqlSession.selectOne("board.modifyViewGET2", bl_uid);

        map.put("info1", info1);
        map.put("info2", info2);

        return (HashMap)map;

    }

}
