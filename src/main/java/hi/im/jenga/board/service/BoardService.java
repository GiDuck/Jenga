package hi.im.jenga.board.service;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.util.BlockCompType;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.util.status_code.BlockStatusCode;

import java.util.List;
import java.util.Map;

public interface BoardService {

    void writeViewBlock(BoardDTO boardDTO, String uploadPath, String bookmark);

    Map<String, Object> getModifyBlock(String blockUid);

    void modifyViewPOST(BoardDTO boardDTO, String uploadFileName, String bookmark);

    void addBookmarkPath(String memUid, BlockPathDTO blockPathDTO);

    BlockStatusCode deleteBlock(String blockUid);

    Map<String, Object> getView(String boardUid);

    BlockStatusCode likeCheck(String blockUid, String memUid);

    String getBookMarkFromHTML(String memUid);

    Map<String, List<String>> getCategoryName();

    String transCtgUID(String smallCategory, BlockCompType compType);

    List<BoardDTO> search(String keyword, String searchOption, String memUid, double startRow, double endRow);

    BlockStatusCode follow(String writerUid, String memUid);

    String followCheck(String writerUid, String memUid);

    BlockStatusCode unFollow(String writerUid, String memUid);

    List<BoardDTO> getFollowerBoard(String followUid, String memUid);

    int likeCount(String blockUid);

    List<BoardDTO> getMyBlock(String memUid);

    List<String> searchImg(String keyword, String searchOption, int startRow, int endRow);

    int countSearch(String keyword, String searchOption);

    BlockStatusCode isLikeExist(String blockUid, String memUid);

    List<MemberDTO> getMyFollower(String memUid);
}
