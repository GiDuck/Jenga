package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BoardDTO;

import java.util.HashMap;

public interface BoardDAO {

    void writeViewBlock(BoardDTO boardDTO, String session_iuid);

    void writeViewThumbImg(String bl_uid, String uploadName);

    void writeViewTag(String bl_uid, String[] bt_name);

    HashMap modifyViewGET(String bl_uid);
}
