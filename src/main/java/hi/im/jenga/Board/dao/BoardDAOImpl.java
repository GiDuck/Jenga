package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BoardDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
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
            logger.info("태그는 "+tag);
            sqlSession.insert("board.writeViewTag", map);
        }
    }

//    info1 [] => tbl_block JOIN tbl_thumbImg
//    info2 [] => tbl_blockTag  / 사용자가 입력한 개수가 다 다르니까 따로 뽑음
    public HashMap modifyViewGET(String bl_uid) {
        Map<String, String []> map = new HashMap();

        List<String> list1 = sqlSession.selectList("board.modifyViewGET", bl_uid);

        logger.info("list1는 "+list1.get(0));
        logger.info("list1는 "+list1.get(1));
        logger.info("list1는 "+list1.get(2));
        logger.info("list1는 "+list1.get(3));
        logger.info("list1는 "+list1.get(4));
        logger.info("list1는 "+list1.get(5));
        logger.info("list1는 "+list1.get(6));
        logger.info("list1는 "+list1.get(7));

        String [] info1 = list1.toArray(new String[list1.size()]);

        logger.info(info1[0]);
        logger.info(info1[1]);
        logger.info(info1[2]);
        logger.info(info1[3]);
        logger.info(info1[4]);

        List<String> list2 = sqlSession.selectList("board.modifyViewGET2", bl_uid);

        logger.info("list2는 "+list2.get(0));
        logger.info("list2는 "+list2.get(1));
        logger.info("list2는 "+list2.get(2));

        //방법1
        String[] info2 = list2.toArray(new String[list2.size()]);

        logger.info("배여루 "+ info2[0]);
        logger.info("배여루 "+ info2[1]);
        logger.info("배여루 "+ info2[2]);

/*
        //방법 2
        String [] info2 = new String[list2.size()];

        for(int i = 0; i < list2.size(); i++){
            info2[i] = list2.get(i);
        }
*/

        map.put("info1", info1);
        map.put("info2", info2);

        return (HashMap)map;
    }

}
