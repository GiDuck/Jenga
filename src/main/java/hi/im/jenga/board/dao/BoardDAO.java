package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.util.BlockCompType;
import hi.im.jenga.member.dto.MemberDTO;

import java.util.List;
import java.util.Map;

public interface BoardDAO {

    void writeViewBlock(BoardDTO boardDTO);

    void writeViewReadCount(String bl_uid);

    void writeViewThumbImg(String bl_uid, String uploadName);

    void writeViewTag(String bl_uid, String[] bt_name);

    Map<String, Object> getModifyBlock(String bl_uid);

    String getUploadName(String bl_uid);

    int checkBmksPath(String session_iuid);

    void insertBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    void updateBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    String getBookMarkFromHTML(String session_iuid);

    int deleteBlock(String bl_uid);

    int likeCount(String bl_iuid);

    int likeCheck(String bl_iuid, String session_mem_iuid);

    Map<String, List<String>> getCategoryName();

    void modifyViewBoard(BoardDTO boardDTO);

    void modifyViewThumbImg(BoardDTO boardDTO, String uploadName);

    void modifyViewTag(BoardDTO boardDTO);

    String transCtgUID(String bl_smCtg, BlockCompType flag);

    List<BoardDTO> searchName(String search, double startRow, double endRow);

    List<BoardDTO> searchTag(String search, double startrow, double endrow);

    List<BoardDTO> searchContents(List<String> search, double startRow, double endRow);

    void setSearchKeyword(String search, String session_iuid);

    void getAddReadCount(String bl_uid);

    Map<String, Object> getBoardDetailBlock(String bl_uid);

    List<String> getBoardDetailTags(String bl_uid);

    void follow(String bl_writer, String session_iuid);

    String followCheck(String bl_writer, String session_iuid);

    int unFollow(String bl_writer, String session_iuid);

    List<BoardDTO> getFollowerBoard(String follow_iuid,String my_iuid);

    List<BoardDTO> getMyBlock(String my_iuid);

    List<String> searchImgName(String search, int startrow, int endrow);

    List<String> searchImgTag(String search, int startrow, int endrow);

    List<String> searchImgContents(List<String> search, int startrow, int endrow);

    int countSearchName(String search);

    int countSearchTag(String search);

    int countSearchContents(List<String> list);

    void addLike(String bl_iuid, String session_mem_iuid);

    void cancelLike(String bl_iuid, String session_mem_iuid);

    List<MemberDTO> getMyFollower(String my_iuid);
}
