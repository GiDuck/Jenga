package hi.im.jenga.board.controller;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.board.util.BlockCompType;
import hi.im.jenga.board.util.FileIOUtil;
import hi.im.jenga.board.util.PageAccumluator;
import hi.im.jenga.board.util.Paging;
import hi.im.jenga.util.AuthUser;
import hi.im.jenga.util.JsonParseManager;
import hi.im.jenga.util.status_code.BlockStatusCode;
import hi.im.jenga.util.status_code.FileStatusCode;
import hi.im.jenga.util.FileType;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.util.session.MemberSession;
import hi.im.jenga.util.session.SessionValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/board")
public class BoardController {
    public static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    private FileIOUtil fileIOUtil;

    @Value("#{data['image.block_path']}")
    private String imagePath;
    @Value("#{data['image.block_absolute_path']}")
    private String imageAbsolutePath;

    @Value("#{data['bookmark.root_path']}")
    private String bookmarkPath;
    @Value("#{data['bookmark.absolute_path']}")
    private String bookmarkAbsolutePath;


    @GetMapping(value = "/search")
    public String SearchGET() {
        return "stackBoard/boardSearch";
    }


    @GetMapping(value = "/searchAction")
    public @ResponseBody List<BoardDTO> SearchPOST(@RequestParam("search") String keyword, @RequestParam("search_check") String checkType,
                                                   @RequestParam("pageNum") int page, @AuthUser MemberDTO authUser) {

       Paging pageInfo = PageAccumluator.getPageInfo(page, boardService.countSearch(keyword, checkType));
       return boardService.search(keyword, checkType, authUser.getMem_iuid(), pageInfo.getStartPage(), pageInfo.getEndPage());
    }

    @GetMapping(value = "/boardView")
    public String getBoardDetail(@RequestParam("bl_uid") String bl_uid, Model model) {
        model.addAttribute("bl_uid", bl_uid);
        model.addAttribute("map", boardService.getView(bl_uid));
        return "stackBoard/boardDetailView";
    }


    @GetMapping(value = "/stackBlock")
    public String getWriteView(Model model, @AuthUser MemberDTO authUser) {

          model.addAttribute("category", JsonParseManager.parseToString(boardService.getCategoryName()));
          String resultHTML = boardService.getBookMarkFromHTML(authUser.getMem_iuid());
          if (resultHTML != null) {  model.addAttribute("resultHTML", resultHTML);  }
          model.addAttribute("statusToken", "stack");
          return "stackBoard/stackBlock";
    }


    @GetMapping(value = "/modifyBlock")
    public String getBlockModifyView(Model model,@RequestParam(value = "bl_uid", required = false) String bl_uid, @AuthUser MemberDTO authUser) {

        String resultHTML = boardService.getBookMarkFromHTML(authUser.getMem_iuid());
        if (resultHTML!=null) {
            model.addAttribute("resultHTML", resultHTML);
        }
        model.addAttribute("map", boardService.getModifyBlock(bl_uid));
        model.addAttribute("category", JsonParseManager.parseToString(boardService.getCategoryName()));
        model.addAttribute("statusToken", "modify");

        return "stackBoard/stackBlock";


    }

    @PostMapping(value = "/uploadBlock", produces = "multipart/form-data; charset=utf-8")
    public @ResponseBody String WriteViewPOST(BoardDTO boardDTO, @RequestPart(value = "bti_url", required = false) MultipartFile uploadFile,
                         @RequestParam("bl_bookmarks") String bl_bookmarks, @AuthUser MemberDTO authUser) {

        boardDTO.setBl_uid(UUID.randomUUID().toString());
        boardDTO.setBl_writer(authUser.getMem_iuid());
        String uploadPath = null;
        logger.info("들어온 img...");
        System.out.println(uploadFile);

        if (uploadFile != null) {
            fileIOUtil = new FileIOUtil(FileType.IMAGE);
            uploadPath = fileIOUtil.fileUpload(uploadFile, imageAbsolutePath);
            logger.info("upload Path... " + uploadPath);
        }

        boardDTO.setBl_smCtg(boardService.transCtgUID(boardDTO.getBl_smCtg(), BlockCompType.CATEGORY_SAMLL));
        boardDTO.setBl_mainCtg(boardService.transCtgUID(boardDTO.getBl_mainCtg(), BlockCompType.CATEGORY_MAIN));
        boardService.writeViewBlock(boardDTO, uploadPath, bl_bookmarks);


        return boardDTO.getBl_uid();
    }


    @GetMapping(value = "/like/{bl_iuid}")
    public @ResponseBody int like(@PathVariable String bl_iuid, @AuthUser MemberDTO authUser) {
        boardService.likeCheck(bl_iuid, authUser.getMem_iuid());
        return boardService.likeCount(bl_iuid);

    }

    @GetMapping(value = "/isLikeExist/{bl_uid}")
    public @ResponseBody BlockStatusCode isLikeExist(@PathVariable("bl_uid") String bl_iuid, @AuthUser MemberDTO authUser) {

        return boardService.isLikeExist(bl_iuid, authUser.getMem_iuid());
    }


    @PatchMapping(value = "/modView")
    public String modifyViewPOST(BoardDTO boardDTO, @RequestPart(value = "bti_url", required = false) MultipartFile uploadFile, @RequestParam("bl_bookmarks") String bl_bookmarks) {

        String uploadName = null;

        if (uploadFile != null) {
            fileIOUtil = new FileIOUtil(FileType.BOOKMARK);
            uploadName = fileIOUtil.fileUpload(uploadFile, bookmarkAbsolutePath);
        }

        boardService.modifyViewPOST(boardDTO, uploadName, bl_bookmarks);

        return "/board/boardView?bl_uid=" + boardDTO.getBl_uid();
    }


    @GetMapping(value = "/delBlock")
    public BlockStatusCode deleteBlock(@RequestParam String bl_uid) {
        return boardService.deleteBlock(bl_uid);
    }


    @PostMapping(value = "/fileUpload")
    public @ResponseBody int fileUpload(@RequestParam("bp_browstype") String bp_browstype, @RequestParam("bp_booktype") String bp_booktype,
                                        @RequestParam("file") MultipartFile uploadFile, HttpSession session) {

        MemberDTO memberSession = (MemberDTO)SessionValidate.getValidSessionObj(session, MemberSession.MEMBER_SESSION_KEY);
        BlockPathDTO blockPathDTO = new BlockPathDTO();

        try {

            String uploadPath =  FileIOUtil.getUploadedFilePath(uploadFile, bookmarkAbsolutePath, bookmarkPath, FileType.BOOKMARK);
            blockPathDTO.setBp_booktype(bp_booktype);
            blockPathDTO.setBp_browstype(bp_browstype);
            blockPathDTO.setBp_path(uploadPath);

            boardService.addBookmarkPath(memberSession.getMem_iuid(), blockPathDTO);

            return FileStatusCode.FILE_UPLOAD_SUCCESS.getCode();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return FileStatusCode.FILE_UPLOAD_FAIL.getCode();

    }


    @GetMapping(value = "/follow")
    public @ResponseBody BlockStatusCode follower(@RequestParam("bl_writer") String bl_writer, @AuthUser MemberDTO authUser) {

      return boardService.follow(bl_writer, authUser.getMem_iuid());

    }

    @GetMapping(value = "/unFollow")
    public @ResponseBody BlockStatusCode unFollow(@RequestParam("bl_writer") String bl_writer, @AuthUser MemberDTO authUser) {
       return boardService.unFollow(bl_writer, authUser.getMem_iuid());
    }


    //팔로워한 사람 리스트
    @GetMapping(value = "/followlist")
    public List<MemberDTO> myFollower(@AuthUser MemberDTO authUser) {
        return boardService.getMyFollower(authUser.getMem_iuid());
    }


    @GetMapping(value = "follwerBlock")
    public @ResponseBody List<BoardDTO> followerBlock(String follow_iuid, @AuthUser MemberDTO authUser) {
        return boardService.getFollowerBoard(follow_iuid, authUser.getMem_iuid());
    }

    @GetMapping(value = "/getFavoriteBlock")
    public String getFavoriteBlock() {
        return "favoriteBoard/favoriteBoard";
    }

    @GetMapping(value = "/getMyBlockManage")
    public String getMyBlockManage(Model model, @AuthUser MemberDTO authUser) {
        model.addAttribute("boards", JsonParseManager.parseToString(boardService.getMyBlock(authUser.getMem_iuid())));
        return "myBoard/myBoardManage";
    }

    @GetMapping(value = "/getMyFavorBlock")
    public String getMyFavorBlock() {
        return "myBoard/myFavorBlock";
    }

    @GetMapping(value = "/getNoticeBoard")
    public String getNoticeBoard() {
        return "noticeBoard/noticeBoard";

    }

}