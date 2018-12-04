package hi.im.jenga.board.service;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;

import java.util.HashMap;

public interface BoardService {

    void writeViewBlock(String session_iuid, BoardDTO boardDTO) throws Exception;

    void writeViewThumbImg(String bl_uid, String uploadName);

    void writeViewTag(String bl_uid, String[] bt_name);

    HashMap modifyViewGET(String bl_uid);

    void modifyViewPOST(BoardDTO boardDTO, String uploadName, String[] bt_name);

    void addBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    int deleteBlock(String bl_uid);

    HashMap getView(String bl_uid);

    void likeCheck(String bl_iuid, String session_mem_iuid);

    String getBookMarkFromHTML(String session_iuid);
}
