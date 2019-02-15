package hi.im.jenga.board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.member.dto.MemberDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface BoardService {

    void writeViewBlock(BoardDTO boardDTO, String uploadName, String bl_bookmarks) throws Exception;

    Map<String, Object> getModifyBlock(String bl_uid) throws JsonProcessingException;

    void modifyViewPOST(BoardDTO boardDTO, String uploadName, String bl_bookmarks);

    void addBmksPath(String session_iuid, BlockPathDTO blockPathDTO);

    int deleteBlock(String bl_uid);

    Map<String, Object> getView(String bl_uid) throws Exception;

    void likeCheck(String bl_iuid, String session_mem_iuid);

    String getBookMarkFromHTML(String session_iuid);

    Map<String, List<String>> getCategoryName();

    String transCtgUID(String bl_smCtg, String flag);

    List<BoardDTO> search(String search, String search_check, String session_iuid, int startrow, int endrow) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    void follow(String bl_writer, String session_iuid);

    String followCheck(String bl_writer, String session_iuid);

    void unFollow(String bl_writer, String session_iuid);

    List<BoardDTO> getFollowerBoard(String follow_iuid, String my_iuid);

    int likeCount(String bl_iuid);

    List<BoardDTO> getMyBlock(String my_iuid);

    List<String> searchImg(String search, String search_check, int startrow, int endrow) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    int countSearch(String search, String search_check) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    String isLikeExist(String bl_iuid, String session_mem_iuid);

    List<MemberDTO> getMyFollower(String my_iuid) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;
}
