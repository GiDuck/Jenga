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






    public void writeViewBlock(BoardDTO boardDTO) { sqlSession.insert("board.writeViewBlock", boardDTO); }

    public void writeViewReadCount(String bl_uid) {
        logger.info("유아이이디디디디"+bl_uid);
        sqlSession.insert("board.writeViewReadCount", bl_uid);
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

        for (String tag : bt_name) {
            map.put("tag", tag);
            logger.info("태그는 " + tag);
            sqlSession.insert("board.writeViewTag", map);
        }
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
        result = sqlSession.selectOne("board.likeCheck", map);

        if (result == null) {
            sqlSession.insert("board.addLike", map);
            return;
        }
        sqlSession.delete("board.cancelLike", map);

    }

    public Map<String, List<String>> getCategoryName() {
        Map<String, List<String>> category = new HashMap();

        List<String> uids = sqlSession.selectList("board.mCtgAllUids");
        for(String uid : uids){
            List<String> list = sqlSession.selectList("board.sCtgAllNames", uid);
            String name = sqlSession.selectOne("board.mCtgAllNames",uid);

            category.put(name, list);
        }
        return category;
    }




    public HashMap modifyViewGET(String bl_uid) {
        Map<String, String> map = new HashMap();

        // tbl_Block (작성자, 제목, 설명, 대분류, 소분류, 날짜, 북마크id), tbl_thumbImg(url)
        map = sqlSession.selectOne("board.modifyViewGET", bl_uid);

        logger.info("맵은 " + map);
        logger.info("그게 왜 " + map.get("BL_WRITER"));

        List<String> list2 = sqlSession.selectList("board.modifyViewGET2", bl_uid);

        for (int i = 0; i < list2.size(); i++) {
            logger.info(list2.get(i));
            map.put("tag" + i, list2.get(i));
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

        logger.info("모야모야 " + map.get("BTI_URL"));
        logger.info("탴태탴 " + map.get("tag1"));
        logger.info("탴태탴 " + map.get("tag2"));

        return (HashMap) map;

    }







    public void modifyViewBoard(BoardDTO boardDTO) {
        sqlSession.update("board.modifyViewBoard", boardDTO);
    }

    public void modifyViewThumbImg(BoardDTO boardDTO, String uploadName) {
        Map<String, String> map = new HashMap();
        map.put("bl_uid", boardDTO.getBl_uid());
        map.put("uploadName", uploadName);
        sqlSession.update("board.modifyViewThumbImg", map);
    }

    public void modifyViewTag(BoardDTO boardDTO) {
        Map<String, String> map = new HashMap();
        String [] bt_name = boardDTO.getBt_name();

        for (String tag : bt_name) {
            map.put("tag", tag);
            sqlSession.update("board.modifyViewTag", map);
        }

    }

    public String transCtgUID(String bl_smCtg, String flag) {
        String trans;
        if(flag.equals("s")){
            return sqlSession.selectOne("board.sctgUID", bl_smCtg);
        }
        return sqlSession.selectOne("board.mctgUID", bl_smCtg);
    }

    public List<BoardDTO> searchName(String search) {
        return sqlSession.selectList("board.selectName",search);
    }

    public List<BoardDTO> searchTag(String search) {
        return sqlSession.selectList("board.selectTag",search);
    }

    public List<BoardDTO> searchContents(String search) {
        return sqlSession.selectList("board.selectTitle",search);
    }

    public void setSearchKeyword(String search, String session_iuid) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("search",search);
        map.put("session_iuid",session_iuid);
        sqlSession.insert("board.setSearchKeyword",map);
    }


    public String getUploadName(String bl_uid) {
        return sqlSession.selectOne("board.getUploadName", bl_uid);
    }


    public String checkBmksPath(String session_iuid) {
        String result = sqlSession.selectOne("board.checkBmksPath", session_iuid);
        return result == null ? "X" : "O";
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
    public String getBookMarkFromHTML(String session_iuid) {
        return sqlSession.selectOne("board.getBookMarkFromHTML", session_iuid);
    }

    public int deleteBlock(String bl_uid) {
        return sqlSession.delete("board.deleteBlock", bl_uid);
    }

    public HashMap getView(String bl_uid) {
        Map<String, String> map = new HashMap();

        sqlSession.update("board.addReadCount", bl_uid);    // 조회수를 올림              //나누기

        map = sqlSession.selectOne("board.getView1", bl_uid);

//        for(int i =0; i< map.size(); i++){
//            logger.info(map.get(i));
//        }
//        B.bl_description, B.bl_introduce, B.bl_mainCtg, B.bl_smCtg, B.bl_date, B.bl_objId,
//                I.bti_url, R.blrc_count, (SELECT COUNT(L.blk_writer) FROM tbl_blockLikes) AS likes
        logger.info(map.toString());
                logger.info(map.get("BL_WRITER"));  // 대문자로 뽑아야함
                logger.info(map.get("BL_TITLE"));
                logger.info(String.valueOf(map.get("BL_DESCRIPTION")));
                logger.info(map.get("BL_INTRODUCE"));
                logger.info(map.get("BL_MAINCTG"));
                logger.info(map.get("BL_SMCTG"));
                logger.info(String.valueOf(map.get("BL_DATE")));
                logger.info(map.get("BL_OBJID"));
                logger.info(map.get("BTI_URL"));
                logger.info(String.valueOf(map.get("BLRC_COUNT")));
                logger.info(String.valueOf(map.get("LIKES")));

        List<String> list = sqlSession.selectList("board.getView2", bl_uid);   // 태그 뽑음
        for (int i = 0; i < list.size(); i++) {
            logger.info(list.get(i));
            map.put("tag" + i, list.get(i));
        }


        return (HashMap) map;
    }
}



