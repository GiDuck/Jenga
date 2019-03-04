package hi.im.jenga.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hi.im.jenga.board.dao.BoardDAO;
import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.util.BlockCompType;
import hi.im.jenga.board.util.FileIOUtil;
import hi.im.jenga.util.EncryptManager;
import hi.im.jenga.util.FileType;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.util.JsonParseManager;
import hi.im.jenga.util.status_code.BlockStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {


    private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

    private BoardDAO dao;
    private MongoService mongoService;
    private AES256Cipher aes256Cipher;


    private final ObjectMapper mapper = new ObjectMapper();

    @Value("#{data['bookmark.root_path']}")
    private String bookmark_absolute_path;

    @Autowired
    public BoardServiceImpl(BoardDAO dao, MongoService mongoService, AES256Cipher aes256Cipher) {
        this.dao = dao;
        this.mongoService = mongoService;
        this.aes256Cipher = aes256Cipher;
    }


    @Transactional
    public void writeViewBlock(BoardDTO boardDTO, String uploadPath, String bookmark) {

        mongoService.writeViewBmks(boardDTO.getBl_uid(), bookmark);
        boardDTO.setBl_objId(mongoService.getObjId("_refBoardId", boardDTO.getBl_uid()));

        dao.writeViewBlock(boardDTO);
        dao.writeViewReadCount(boardDTO.getBl_uid());
        if(uploadPath != null){
            dao.writeViewThumbImg(boardDTO.getBl_uid(), uploadPath);
        }
        dao.writeViewTag(boardDTO.getBl_uid(), boardDTO.getBt_name());


    }

    public Map<String, Object> getModifyBlock(String blockUid) {

        Map<String, Object> map = dao.getModifyBlock(blockUid);
        map.put("tag", JsonParseManager.parseToString(dao.getBoardDetailTags(blockUid)));
        map.put("bookmarks", mongoService.getView("_refBoardId", blockUid));
        map.put("bti_url", dao.getUploadName(blockUid));
        return map;
    }

    @Transactional
    public void modifyViewPOST(BoardDTO boardDTO, String uploadFileName, String bookmark) {

        dao.modifyViewBoard(boardDTO);

        if (uploadFileName == null) {
            uploadFileName = dao.getUploadName(boardDTO.getBl_uid());
        }

        dao.modifyViewThumbImg(boardDTO, uploadFileName);
        dao.modifyViewTag(boardDTO);
        mongoService.modifyViewPOST("_refBoardId", boardDTO.getBl_uid(), bookmark);
    }

    public void addBookmarkPath(String memUid, BlockPathDTO blockPathDTO) {

        if (dao.checkBmksPath(memUid) < 1) {
            dao.insertBmksPath(memUid, blockPathDTO);
        } else {
            dao.updateBmksPath(memUid, blockPathDTO);
        }
    }

    @Transactional
    public BlockStatusCode deleteBlock(String blockUid) {
        mongoService.deleteBlock("_refBoardId", blockUid);
        if (dao.deleteBlock(blockUid) > 0) return BlockStatusCode.BLOCK_DEL_SUCCESS;
        return BlockStatusCode.BLOCK_DEL_FAIL;
    }


    @Transactional
    public Map<String, Object> getView(String boardUid) {

        dao.getAddReadCount(boardUid);
        Map<String, Object> map = dao.getBoardDetailBlock(boardUid);

        System.out.println(map);

        String nick = String.valueOf(map.get("mem_nick"));
        String introduce = String.valueOf(map.get("mem_introduce"));
        String profile = String.valueOf(map.get("mem_profile"));

        map.put("mem_nick", EncryptManager.aesDecode(aes256Cipher, nick));
        map.put("mem_introduce", EncryptManager.aesDecode(aes256Cipher, introduce));
        map.put("mem_profile", EncryptManager.aesDecode(aes256Cipher, profile));


        map.put("tag", JsonParseManager.parseToString(dao.getBoardDetailTags(boardUid)));
        map.put("likes", dao.likeCount(boardUid));
        map.put("bookmarks", mongoService.getView("_refBoardId", boardUid));


        return map;
    }


    public BlockStatusCode isLikeExist(String blockUid, String memUid) {

        if (dao.likeCheck(blockUid, memUid) > 0) {

            return BlockStatusCode.LIKE_EXISTS;
        }

        return BlockStatusCode.LIKE_NOT_EXISTS;
    }

    public List<MemberDTO> getMyFollower(String memUid) {
        List<MemberDTO> list = dao.getMyFollower(memUid);
        for (MemberDTO member : list) member.setMem_nick(EncryptManager.aesDecode(aes256Cipher, member.getMem_nick()));
        return list;
    }

    public BlockStatusCode likeCheck(String blockUid, String memUid) {

        if (dao.likeCheck(blockUid, memUid) < 1) {
            dao.addLike(blockUid, memUid);
            return BlockStatusCode.FOLLOW;
        }
        dao.cancelLike(blockUid, memUid);
        return BlockStatusCode.NOT_FOLLOW;
    }

    public int likeCount(String blUid) {
        return dao.likeCount(blUid);
    }

    public String getBookMarkFromHTML(String memUid) {
        String fileFullName = dao.getBookMarkFromHTML(memUid);
        String result = null;
        if (fileFullName != null) {
            FileIOUtil fileIOUtil = new FileIOUtil(FileType.BOOKMARK);
            result = fileIOUtil.getFileToChar(fileFullName, bookmark_absolute_path);
        }

        return result;

    }

    public Map<String, List<String>> getCategoryName() {
        return dao.getCategoryName();
    }

    public String transCtgUID(String smallCategory, BlockCompType compType) {
        return dao.transCtgUID(smallCategory, compType);
    }

    public List<BoardDTO> search(String keyword, String searchOption, String memUid, double startRow, double endRow) {
        if (memUid != null) {
            dao.setSearchKeyword(keyword, memUid);
        }

        if (checkComponentType(BlockCompType.NAME, searchOption)) {
            keyword = EncryptManager.aesEnocde(aes256Cipher, keyword);
            return dao.searchName(keyword, startRow, endRow);

        } else if (checkComponentType(BlockCompType.TAG, searchOption)) {
            return dao.searchTag(keyword, startRow, endRow);
        }

        return dao.searchContents(stringTokenizer(keyword), startRow, endRow);

    }

    public BlockStatusCode follow(String writerUid, String memUid) {

        try {
            dao.follow(writerUid, memUid);
            return BlockStatusCode.FOLLOW_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BlockStatusCode.FOLLOW_FAIL;

    }

    public String followCheck(String writerUid, String memUid) {
        return dao.followCheck(writerUid, memUid);
    }

    public BlockStatusCode unFollow(String writerUid, String memUid) {
        if (dao.unFollow(writerUid, memUid) > 0) return BlockStatusCode.NOT_FOLLOW;
        return BlockStatusCode.FOLLOW;
    }

    public List<BoardDTO> getFollowerBoard(String followUid, String memUid) {
        return dao.getFollowerBoard(followUid, memUid);
    }


    public List<BoardDTO> getMyBlock(String memUid) {
        return dao.getMyBlock(memUid);
    }

    public List<String> searchImg(String keyword, String searchOption, int startRow, int endRow) {
        if (checkComponentType(BlockCompType.NAME, searchOption)) {
            keyword = EncryptManager.aesEnocde(aes256Cipher, keyword);
            return dao.searchImgName(keyword, startRow, endRow);
        } else if (checkComponentType(BlockCompType.TAG, searchOption)) {
            return dao.searchImgTag(keyword, startRow, endRow);
        }
        return dao.searchImgContents(stringTokenizer(keyword), startRow, endRow);
    }

    public int countSearch(String keyword, String searchOption) {
        if (checkComponentType(BlockCompType.NAME, searchOption)) {
            keyword = EncryptManager.aesEnocde(aes256Cipher, keyword);
            return dao.countSearchName(keyword);

        } else if (checkComponentType(BlockCompType.TAG, searchOption)) {
            return dao.countSearchTag(keyword);
        }

        return dao.countSearchContents(stringTokenizer(keyword));

    }

    private boolean checkComponentType(BlockCompType compType, String keyword) {

        if (compType.name().equals(keyword.toUpperCase())) return true;
        else return false;


    }


    @SuppressWarnings("unchecked")
    private List<String> stringTokenizer(String str) {

        String[] strArr = str.split(" ");
        return new ArrayList(Arrays.asList(strArr));

    }


}
