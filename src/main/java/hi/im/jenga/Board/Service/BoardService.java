package hi.im.jenga.board.service;

import hi.im.jenga.board.dto.BoardDTO;

public interface BoardService {

    String getBookMark();

    void writeViewPOST(String session_iuid, BoardDTO boardDTO) throws Exception;
}
