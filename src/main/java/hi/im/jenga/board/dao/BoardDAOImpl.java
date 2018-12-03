package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BlockPathDTO;
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

    public void writeViewReadCount(String bl_uid) {
        sqlSession.insert("board.writeViewReadCount",bl_uid);
    }

    public void likeCheck(String bl_iuid, String session_mem_iuid) {
        String result;
        Map<String, String> map = new HashMap();
        map.put("bl_iuid", bl_iuid);
        map.put("session_mem_iuid", session_mem_iuid);

        /*
        하나의 주소로 들어오고 select해서 있으면 dislike
                                          없으면 like

        select mem_iuid FROM tbl_blocklike WHERE ref = bl_iuid 해서
        있으면  delete from
        없으면  insert into

        */
         result = sqlSession.selectOne("board.likeCheck",map);

         if(result == null){
            sqlSession.insert("board.addLike", map);
            return;
         }
         sqlSession.delete("board.cancelLike", map);

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

    public HashMap modifyViewGET(String bl_uid) {
        Map<String, String> map = new HashMap();

        // tbl_Block (작성자, 제목, 설명, 대분류, 소분류, 날짜, 북마크id), tbl_thumbImg(url)
        map = sqlSession.selectOne("board.modifyViewGET", bl_uid);

        logger.info("맵은 "+map);
        logger.info("그게 왜 "+map.get("BL_WRITER"));

        List<String> list2 = sqlSession.selectList("board.modifyViewGET2", bl_uid);

        logger.info("list2는 "+list2.get(0));
        logger.info("list2는 "+list2.get(1));
        logger.info("list2는 "+list2.get(2));

        for(int i = 0; i < list2.size(); i++){
            map.put("tag"+i,list2.get(i));
        }



/*
        //       List를 배열로 변환
        //방법1
        String[] info2 = list2.toArray(new String[list2.size()]);

        logger.info("배여루 "+ info2[0]);
        logger.info("배여루 "+ info2[1]);
        logger.info("배여루 "+ info2[2]);

        //방법 2
        String [] info2 = new String[list2.size()];

        for(int i = 0; i < list2.size(); i++){
            info2[i] = list2.get(i);
        }
*/

        logger.info("모야모야 "+map.get("BTI_URL"));
        logger.info("탴태탴 "+map.get("tag1"));
        logger.info("탴태탴 "+map.get("tag2"));

        return (HashMap)map;

    }

    public void modifyViewPOST(BoardDTO boardDTO, String uploadName, String[] bt_name) {
        Map<String, String> map = new HashMap();
        sqlSession.update("board.modifyViewBoard", boardDTO);

        map.put("bl_uid", boardDTO.getBl_uid());
        map.put("uploadName", uploadName);
        sqlSession.update("board.modifyViewThumbImg", map);

        for(String tag: bt_name) {
            map.put("tag", tag);
            sqlSession.update("board.modifyViewTag", map);
        }

    }

    public String getUploadName(String bl_uid) {
        return sqlSession.selectOne("board.getUploadName", bl_uid);
    }


    public String checkBmksPath(String session_iuid) {
        String result = sqlSession.selectOne("board.checkBmksPath", session_iuid);
        return result == null? "X" : "O";
    }

    public void insertBmksPath(String session_iuid, BlockPathDTO blockPathDTO) {
        Map<String, Object> map = new HashMap();
        map.put("session_iuid", session_iuid);
        map.put("blockPathDTO", blockPathDTO);
        logger.info(blockPathDTO.getBp_path());
        logger.info(session_iuid);
        logger.info(blockPathDTO.getBp_browstype());
        logger.info(blockPathDTO.getBp_booktype());
        sqlSession.insert("board.insertBmksPath", map);
    }

    public void updateBmksPath(String session_iuid, BlockPathDTO blockPathDTO) {
        Map<String, Object> map = new HashMap();
        map.put("session_iuid", session_iuid);
        map.put("blockPathDTO", blockPathDTO);
        sqlSession.update("board.updateBmksPath", map);
    }
//    경로+파일이름 return

    public String getBookMark(String session_iuid) {
        return sqlSession.selectOne("board.getBookMark", session_iuid);
    }

    public int deleteBlock(String bl_uid) { return sqlSession.delete("board.deleteBlock", bl_uid); }

    public HashMap getView(String bl_uid) {
        Map<String, String> map = new HashMap();

        sqlSession.update("board.addReadCount", bl_uid);    // 조회수를 올림

        map = sqlSession.selectOne("board.getView1", bl_uid);

        List <String> list = sqlSession.selectList("board.getView2", bl_uid);   // 태그 뽑음
        for(int i = 0; i < list.size(); i++){
            map.put("tag"+i, list.get(i));
        }


        return (HashMap)map;
    }


}
