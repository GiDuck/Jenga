package hi.im.jenga.board.board.dao;

import hi.im.jenga.board.board.dto.BlockPathDTO;
import hi.im.jenga.board.board.dto.BoardDTO;

import java.util.HashMap;

public interface BoardDAO {

    void writeViewBlock(BoardDTO boardDTO, String session_iuid);

    void writeViewThumbImg(String bl_uid, String uploadName);

    void writeViewTag(String bl_uid, String[] bt_name);

    HashMap modifyViewGET(String bl_uid);

    void modifyViewPOST(BoardDTO boardDTO, String uploadName, String[] bt_name);

    String getUploadName(String bl_uid);

    String checkBmksPath(String session_iuid);

    void insertBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    void updateBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    String getBookMark(String session_iuid);

    int deleteBlock(String bl_uid);

    HashMap getView(String bl_uid);

    void writeViewReadCount(String bl_uid);

    void likeCheck(String bl_iuid, String session_mem_iuid);
}
