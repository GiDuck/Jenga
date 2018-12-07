package hi.im.jenga.board.service;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BoardService {

    void writeViewBlock(BoardDTO boardDTO, String uploadName, String bl_bookmarks) throws Exception;

    HashMap modifyViewGET(String bl_uid);

    void modifyViewPOST(BoardDTO boardDTO, String uploadName, String[] bt_name);

    void addBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    int deleteBlock(String bl_uid);

    HashMap getView(String bl_uid);

    void likeCheck(String bl_iuid, String session_mem_iuid);

    String getBookMarkFromHTML(String session_iuid);

    Map<String, List<String>> getCategoryName();
}
