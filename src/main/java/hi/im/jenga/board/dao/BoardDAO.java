package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;

import java.util.List;
import java.util.Map;

public interface BoardDAO {

    void writeViewBlock(BoardDTO boardDTO);

    void writeViewReadCount(String bl_uid);

    void writeViewThumbImg(String bl_uid, String uploadName);

    void writeViewTag(String bl_uid, String[] bt_name);

    Map<String, Object> getModifyBlock(String bl_uid);

    String getUploadName(String bl_uid);

    String checkBmksPath(String session_iuid);

    void insertBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    void updateBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    String getBookMarkFromHTML(String session_iuid);

    int deleteBlock(String bl_uid);

    void likeCheck(String bl_iuid, String session_mem_iuid);

    Map<String, List<String>> getCategoryName();

    void modifyViewBoard(BoardDTO boardDTO);

    void modifyViewThumbImg(BoardDTO boardDTO, String uploadName);

    void modifyViewTag(BoardDTO boardDTO);

    String transCtgUID(String bl_smCtg, String flag);

    List<BoardDTO> searchName(String search);

    List<BoardDTO> searchTag(String search);

    List<BoardDTO> searchContents(String search);

    void setSearchKeyword(String search, String session_iuid);

    void getAddReadCount(String bl_uid);

    Map<String, Object> getBoardDetailBlock(String bl_uid);

    List<String> getBoardDetailTags(String bl_uid);

    void follow(String bl_writer, String session_iuid);

    void unFollow(String bl_writer, String session_iuid);

    List<BoardDTO> getFollowerBoard(String my_iuid);

    int likeCount(String bl_iuid);

    List<BoardDTO> getMyBlock(String my_iuid);

}
