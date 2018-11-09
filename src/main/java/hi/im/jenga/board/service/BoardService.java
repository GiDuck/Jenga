package hi.im.jenga.board.service;

import hi.im.jenga.board.dto.BoardDTO;

import java.util.HashMap;

public interface BoardService {

    String getBookMark();

    void writeViewBlock(String session_iuid, BoardDTO boardDTO) throws Exception;

    void writeViewThumbImg(String bl_uid, String uploadName);

    void writeViewTag(String bl_uid, String[] bt_name);

    HashMap modifyViewGET(String bl_uid);
}
