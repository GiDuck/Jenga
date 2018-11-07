package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.BoardDTO;

public interface BoardDAO {

    void writeViewPOST(BoardDTO boardDTO, String session_iuid);
}
