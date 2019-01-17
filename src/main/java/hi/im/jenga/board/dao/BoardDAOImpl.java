package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.member.dto.MemberDTO;
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


    public void writeViewBlock(BoardDTO boardDTO) {
        sqlSession.insert("board.writeViewBlock", boardDTO);
    }

    public void writeViewReadCount(String bl_uid) {
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
            sqlSession.insert("board.writeViewTag", map);
        }
    }


    public String likeCheck(String bl_iuid, String session_mem_iuid) {
        Map<String, String> map = new HashMap();
        map.put("bl_iuid", bl_iuid);
        map.put("session_mem_iuid", session_mem_iuid);
        String result = sqlSession.selectOne("board.likeCheck", map);
        return result;
    }

    public void addLike(String bl_iuid, String session_mem_iuid) {
        logger.info("like추가");
        logger.info("아유아디    "+bl_iuid);
        logger.info("session     "+session_mem_iuid);
        Map<String, String> map = new HashMap();
        map.put("bl_iuid", bl_iuid);
        map.put("session_mem_iuid", session_mem_iuid);
        sqlSession.insert("board.addLike", map);
    }

    public void cancelLike(String bl_iuid, String session_mem_iuid) {
        logger.info("like삭제");
        Map<String, String> map = new HashMap();
        map.put("bl_iuid", bl_iuid);
        map.put("session_mem_iuid", session_mem_iuid);
        sqlSession.delete("board.cancelLike", map);
    }

    public List<MemberDTO> getMyFollower(String my_iuid) {
        return sqlSession.selectList("board.getFollowerList", my_iuid);
    }

    public List<Map<String, Object>> followRecommend(String my_iuid) {
        logger.info("뽑기"+sqlSession.selectList("board.followRecommend",my_iuid));
        return sqlSession.selectList("board.followRecommend",my_iuid);
    }

    public int likeCount(String bl_iuid) {
        return sqlSession.selectOne("board.likeCount", bl_iuid);
    }

    public Map<String, List<String>> getCategoryName() {
        Map<String, List<String>> category = new HashMap();

        List<String> uids = sqlSession.selectList("board.mCtgAllUids");
        for (String uid : uids) {
            List<String> list = sqlSession.selectList("board.sCtgAllNames", uid);
            String name = sqlSession.selectOne("board.mCtgAllNames", uid);

            category.put(name, list);
        }
        return category;
    }


    public Map<String, Object> getModifyBlock(String bl_uid) {
        return sqlSession.selectOne("board.getModifyBlock", bl_uid);
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
        String[] bt_name = boardDTO.getBt_name();

        for (String tag : bt_name) {
            map.put("tag", tag);
            sqlSession.update("board.modifyViewTag", map);
        }

    }

    public String transCtgUID(String bl_smCtg, String flag) {
        String trans;
        if (flag.equals("s")) {
            return sqlSession.selectOne("board.sctgUID", bl_smCtg);
        }
        return sqlSession.selectOne("board.mctgUID", bl_smCtg);
    }

    public List<BoardDTO> searchName(String search, int startrow, int endrow) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("search", search);
        map.put("startrow",startrow);
        map.put("endrow", endrow);
        return sqlSession.selectList("board.searchName", map);
    }

    public List<BoardDTO> searchTag(String search, int startrow, int endrow) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("search", search);
        map.put("startrow",startrow);
        map.put("endrow", endrow);
        return sqlSession.selectList("board.searchTag", map);
    }

    public List<BoardDTO> searchContents(List<String> search, int startrow, int endrow) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("search", search);
        map.put("startrow",startrow);
        map.put("endrow", endrow);
        return sqlSession.selectList("board.searchTitle", map);
    }

    public void setSearchKeyword(String search, String session_iuid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("search", search);
        map.put("session_iuid", session_iuid);
        sqlSession.insert("board.setSearchKeyword", map);
    }




    public List<Map<String, Object>> getMyLikesBlock(String my_iuid) {
        return sqlSession.selectList("board.getMyLikesBlock",my_iuid);

    }

    public List<Map<String, String>> getPopularBlock(Integer likeCount) {
        return sqlSession.selectList("board.getPopularBlock", likeCount);
    }

    public void follow(String bl_writer, String session_iuid) {
        System.out.println(bl_writer);
        System.out.println(session_iuid);
        Map<String, String> map = new HashMap<String, String>();
        map.put("bl_writer", bl_writer);
        map.put("session_iuid", session_iuid);
        sqlSession.insert("board.follow", map);
    }

    public String followCheck(String bl_writer, String session_iuid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("bl_writer", bl_writer);
        map.put("session_iuid", session_iuid);
        return sqlSession.selectOne("board.followCheck", map) == null ? "success" : "error";
    }

    public void unFollow(String bl_writer, String session_iuid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("bl_writer", bl_writer);
        map.put("session_iuid",session_iuid);
        sqlSession.delete("board.unFollow",map);
    }

    public List<Map<String,String>> getFollowerBoard(String follow_iuid, String my_iuid) { //follower한 사람 글
        Map<String, String> map = new HashMap<String, String>();
        map.put("my_iuid", my_iuid);
        map.put("follow_iuid", follow_iuid);
        return sqlSession.selectList("board.getFollowerBoard", map);
    }

    public List<BoardDTO> getMyBlock(String my_iuid) {
        return sqlSession.selectList("board.getMyBlock", my_iuid);
    }

    public List<String> searchImgName(String search, int startrow, int endrow) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("search", search);
        map.put("startrow",startrow);
        map.put("endrow", endrow);
        return sqlSession.selectList("board.searchImgName", map);
    }

    public List<String> searchImgTag(String search, int startrow, int endrow) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("search", search);
        map.put("startrow",startrow);
        map.put("endrow", endrow);
        return sqlSession.selectList("board.searchImgTag", map);
    }

    public List<String> searchImgContents(List<String> search, int startrow, int endrow) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("search", search);
        map.put("startrow",startrow);
        map.put("endrow", endrow);

        return sqlSession.selectList("board.searchImgTitle", map);
    }

    public int countSearchName(String search) {
        return sqlSession.selectOne("board.countSearchName",search);
    }

    public int countSearchTag(String search) {
        return sqlSession.selectOne("board.countSearchTag",search);
    }

    public int countSearchContents(List<String> search) {
        return sqlSession.selectOne("board.countSearchTitle",search);
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

    public void getAddReadCount(String bl_uid) {
        sqlSession.update("board.addReadCount", bl_uid);    // 조회수 + 1
    }

    public Map<String, Object> getBoardDetailBlock(String bl_uid) {
        return sqlSession.selectOne("board.getBoardDetailBlock", bl_uid);
    }

    public List<String> getBoardDetailTags(String bl_uid) {
        return sqlSession.selectList("board.getBoardDetailTags", bl_uid);   // 태그 뽑음
    }
}




