package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BoardDAO {

    void writeViewBlock(BoardDTO boardDTO);

    void writeViewReadCount(String bl_uid);

    void writeViewThumbImg(String bl_uid, String uploadName);

    void writeViewTag(String bl_uid, String[] bt_name);

    HashMap modifyViewGET(String bl_uid);

    String getUploadName(String bl_uid);

    String checkBmksPath(String session_iuid);

    void insertBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    void updateBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    String getBookMarkFromHTML(String session_iuid);

    int deleteBlock(String bl_uid);

    HashMap getView(String bl_uid);

    void likeCheck(String bl_iuid, String session_mem_iuid);

    Map<String, List<String>> getCategoryName();

    void modifyViewBoard(BoardDTO boardDTO);

    void modifyViewThumbImg(BoardDTO boardDTO, String uploadName);

    void modifyViewTag(BoardDTO boardDTO);
}
