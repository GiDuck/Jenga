package hi.im.jenga.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.board.service.MongoService;
import hi.im.jenga.board.util.BoardUtilFile;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.util.login.SessionCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
// TODO  검색      (테스트) / 회원정보수정 / 댓글 / 팔로우

/**
 * 글 조회 GET / 글 수정 GET (/stackBlock?stack=stack, modify)
 * 글 작성 GET / POST
 * POST (PATCH)
 * 글 삭제 DELETE
 *
 * 검색
 *
 * 북마크 파일 업로드 POST  (모달 GET ? )
 *
 * */

@Controller
@RequestMapping("/board")
public class BoardController {
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    private final MongoService mongoService;
    private final BoardService boardService;
    private final BoardUtilFile boardUtilFile;
    SessionCheck sessionCheck = SessionCheck.getInstance();

    @Autowired
    public BoardController(MongoService mongoService, BoardService boardService, BoardUtilFile boardUtilFile) {
        this.mongoService = mongoService;
        this.boardService = boardService;
        this.boardUtilFile = boardUtilFile;
    }

   /* @RequestMapping(value="/formattingBk", method = RequestMethod.POST)
    @ResponseBody
    public String formattingBK(@RequestParam("bookmark") String bookmark){

        return boardService.formattingBK(bookmark);

    }*/

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String SearchGET(String search, String search_check, HttpSession session) throws Exception {
     /*   String session_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();
        List<BoardDTO> result = boardService.search(search, search_check, session_iuid);
        boardService.searchImg(search,search_check);
        boardService.searchMemInfo(search,search_check);
        boardService.searchLike(search,search_check);
*/


        return "stackBoard/boardSearch";
    }
    /**
    * list 팔로워 이름 / 소개 / 프로필 사진
    * 무한스크롤
    * */

    @RequestMapping(value = "/getFollowingMember", method = RequestMethod.GET)
    public @ResponseBody List<BoardDTO> getFollowingMember(@RequestParam("pageNum") int page, @RequestParam(value = "search", required = false) String search, HttpSession session) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String session_iuid = null;

        logger.info("search는 "+ search);

        try {
            session_iuid = sessionCheck.myGetSessionIuid(session);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        int limit = 20;
        int nowpage = page;
        int listcount = 0;
        int startrow = (page - 1) * 10 + 1;
        int endrow = startrow + limit - 1;

        listcount = boardService.countFollowingMember(session_iuid, search);

        int maxpage = (int) ((double) listcount / limit + 0.95);
        int startpage = (((int) ((double) page / 10 + 0.9)) - 1) * 10 + 1;
        int endpage = maxpage;

        if (endpage > startpage + 10 - 1) {
            endpage = startpage + 10 - 1;
        }

        List<BoardDTO> container = null;
        try {

            container = boardService.getFollowingMember(session_iuid, startrow, endrow);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return container;

    }

    @RequestMapping(value = "/searchAction", method = RequestMethod.GET)
    @ResponseBody
    public List<BoardDTO> SearchPOST(@RequestParam("search") String search, @RequestParam("search_check") String search_check, @RequestParam("pageNum") int page, HttpSession session) throws Exception {

        String session_iuid = null;

        try {
            session_iuid = sessionCheck.myGetSessionIuid(session);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        int limit = 20;
        int nowpage = page;
        int listcount = 0;
        int startrow = (page - 1) * 10 + 1;
        int endrow = startrow + limit - 1;

        if(search == null && search.replaceAll(" ","").equals("")){
            //return "error"; 에러 보내야함...
        }
        listcount = boardService.countSearch(search,search_check);


        int maxpage = (int) ((double) listcount / limit + 0.95);
        int startpage = (((int) ((double) page / 10 + 0.9)) - 1) * 10 + 1;
        int endpage = maxpage;

        if (endpage > startpage + 10 - 1) {
            endpage = startpage + 10 - 1;
        }




        List<BoardDTO> container = null;
        try {

            container = boardService.search(search, search_check, session_iuid, startrow, endrow);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return container;
    }

    @GetMapping(value = "/boardView")
    public String getBoardDetail(@RequestParam("bl_uid") String bl_uid, Model model, MongoDTO mongoDTO) throws Exception {

        Map<String, Object> map = boardService.getView(bl_uid);
        model.addAttribute("map", map);

        return "stackBoard/boardDetailView";
    }


    // 글쓰는 페이지 GET, 글 수정 페이지 GET
    @RequestMapping(value = "/stackBlock", method = RequestMethod.GET)
    public String getWriteView(HttpSession session, Model model, String status, @RequestParam(value = "bl_uid", required = false) String bl_uid) throws JsonProcessingException {
//  TODO  status 없이 그냥 url로 접근하면 잘못된 페이지 띄우기 -> 임시로 / 로 감
        if (status == null) return "redirect:/";
        if (status.equals("stack")) {
            String session_iuid = sessionCheck.myGetSessionIuid(session);
            String resultHTML = null;
            Map<String, List<String>> category = null;
            try {
                category = boardService.getCategoryName();

            } catch (Exception e) {
                e.printStackTrace();
            }

            ObjectMapper mapper = new ObjectMapper();
            String categoryJSON = mapper.writeValueAsString(category);
            try {
                resultHTML = boardService.getBookMarkFromHTML(session_iuid);         // 세션체크
            } catch (Exception e) {
                e.printStackTrace();
            }


            logger.info(resultHTML);

            model.addAttribute("category", categoryJSON);
            if(!resultHTML.equals("notExist")) {
                model.addAttribute("resultHTML", resultHTML);
            }

        }else if(status.equals("modify")) {         //  service 나누기
            String session_iuid = sessionCheck.myGetSessionIuid(session);
            String resultHTML = null;
            Map<String, Object> map = boardService.getModifyBlock(bl_uid);

            logger.info("컨트롤러 맵은 " + map);

            Map<String, List<String>> category = null;
            try {
                category = boardService.getCategoryName();
            }catch(Exception e){
                e.printStackTrace();
            }
            ObjectMapper mapper = new ObjectMapper();
            String categoryJSON = mapper.writeValueAsString(category);

            try {
                resultHTML = boardService.getBookMarkFromHTML(session_iuid);         // 세션체크
            }catch(Exception e){
                e.printStackTrace();
            }
            if(!resultHTML.equals("notExist")) {
                model.addAttribute("resultHTML", resultHTML);
            }
            model.addAttribute("category", categoryJSON);
            model.addAttribute("map", map);

        }

        return "editor/stackBoard/stackBlock";

    }


    // 글쓰는페이지 POST / 작성
    @RequestMapping(value = "/uploadBlock", method = RequestMethod.POST, produces = "multipart/form-data; charset=utf-8")
    public @ResponseBody
    String WriteViewPOST(BoardDTO boardDTO, HttpSession session, @RequestPart(value = "bti_url", required = false) MultipartFile uploadFile,
                         @RequestParam("bl_bookmarks") String bl_bookmarks) throws Exception {

        logger.info("session에서 뽑아온 iuid는 " + sessionCheck.myGetSessionIuid(session));
        boardDTO.setBl_uid(UUID.randomUUID().toString());

        boardDTO.setBl_writer(sessionCheck.myGetSessionIuid(session));
        String uploadName;

//        logger.info("uploadFile.getOriginalFilename()은 ? "+ uploadFile.getOriginalFilename());
        if (uploadFile == null) {
            logger.info("null!");
        }

        if (uploadFile != null) {
            uploadFile.getOriginalFilename();
            uploadName = boardUtilFile.fileUpload(uploadFile, "image");
            logger.info("이미지 파일이 있네 이름은 ?" + uploadName);
        } else {
            uploadName = "";
        }

        logger.info("이미지 파일은 ?" + uploadName);

//        service에서 디폴트이미지 처리

        String flag = "s";
        boardDTO.setBl_smCtg(boardService.transCtgUID(boardDTO.getBl_smCtg(), flag));
        flag = "m";
        boardDTO.setBl_mainCtg(boardService.transCtgUID(boardDTO.getBl_mainCtg(), flag));

        logger.info("DTO" + boardDTO.getBl_uid() + "/스몰/" + boardDTO.getBl_smCtg() + "/메인/" + boardDTO.getBl_mainCtg());

        boardService.writeViewBlock(boardDTO, uploadName, bl_bookmarks);

        logger.info("글작성 성공");

        return boardDTO.getBl_uid();
    }


    // TODO  like 상태값으로 비교   이거먼저하자
    // block iuid를 조건으로 insert mem_iuid(session에 있는)
    @RequestMapping(value = "/like/{bl_iuid}")
    public @ResponseBody int like(@PathVariable String bl_iuid, HttpSession session){
        logger.info("like 들어옴");
        String session_mem_iuid = sessionCheck.myGetSessionIuid(session);
        boardService.likeCheck(bl_iuid, session_mem_iuid);

        int likeCount = boardService.likeCount(bl_iuid);

        return likeCount;
    }

    @RequestMapping(value = "/isLikeExist/{bl_uid}", method = RequestMethod.GET)
    public @ResponseBody String isLikeExist(HttpSession session, @PathVariable("bl_uid") String bl_iuid) {
        String session_mem_iuid = sessionCheck.myGetSessionIuid(session);
        String check = boardService.isLikeExist(bl_iuid, session_mem_iuid);
        logger.info(check);
        String result = check != null ? "exist" : "notExist";
        logger.info(result);
        return result;
    }


//  TODO 테스트 mongo update도 함
//    수정페이지 POST    /modView  PATCH or PUT          json받아야함
    @RequestMapping(value = "/modView", method = RequestMethod.PATCH)
    public String modifyViewPOST(BoardDTO boardDTO, @RequestPart(value = "bti_url", required = false) MultipartFile uploadFile, @RequestParam("bl_bookmarks") String bl_bookmarks) {

        String uploadName;
        // 수정을 안하면 원래 이미지를 줘야함
        // 여기서 nullpointException 뜨면 여기서 boardService.getUploadName() 해야하고
//         넘어가면 서비스impl에서 처리
        if (uploadFile != null) {
            uploadName = boardUtilFile.fileUpload(uploadFile, "image");
        } else {
            uploadName = "";
        }

        boardService.modifyViewPOST(boardDTO, uploadName, bl_bookmarks);

        return "/board/boardView?bl_uid=" + boardDTO.getBl_uid();
    }

    //    TODO 테스트하기  mongo도 지움 / HttpMethod 사용한것 테스트
//    View에서 받는거 테스트해야함
//    삭제페이지 POST
    @RequestMapping(value = "/delBlock", method = RequestMethod.GET)
    public ResponseEntity deleteBlock(@RequestParam String bl_uid) {

        int result = boardService.deleteBlock(bl_uid);

        if (result == 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);

    }


    // 북마크 파일업로드  /  완료
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity fileUpload(@RequestParam String bp_browstype, @RequestParam String bp_booktype, @RequestParam("file") MultipartFile uploadFile, HttpSession session) {
        String session_iuid = sessionCheck.myGetSessionIuid(session);
        ResponseEntity<String> result;
        BlockPathDTO blockPathDTO = new BlockPathDTO();
        logger.info("북마크 타입은 " + bp_booktype);
        logger.info("브라우저 타입은 " + bp_browstype);

        try {
            logger.info("업로드 된 파일");
            logger.info("파일 이름은 " + uploadFile.getOriginalFilename());
            logger.info("파일 사이즈 " + uploadFile.getSize());
            logger.info("머고이건 " + uploadFile.getBytes().toString());

            String uploadPath = boardUtilFile.fileUpload(uploadFile, "block");

            logger.info(uploadPath);
            blockPathDTO.setBp_booktype(bp_booktype);
            blockPathDTO.setBp_browstype(bp_browstype);
            blockPathDTO.setBp_path(uploadPath);

            logger.info(blockPathDTO.getBp_booktype());
            logger.info(blockPathDTO.getBp_browstype());
            logger.info(blockPathDTO.getBp_path());

            boardService.addBmksPath(session_iuid, blockPathDTO);


            result = new ResponseEntity(HttpStatus.OK);

        } catch (Exception e) {

            e.printStackTrace();

            result = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return result;

    }


    @RequestMapping(value = "/follow", method = RequestMethod.GET)
    @ResponseBody
    public String follower(@RequestParam("bl_writer") String bl_writer, HttpSession session, HttpServletResponse response) throws Exception {

        String session_iuid = null;
        try {
            session_iuid = sessionCheck.myGetSessionIuid(session);
            boardService.follow(bl_writer, session_iuid);
            return "follow";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/unFollow", method = RequestMethod.GET)
    @ResponseBody
    public String unfollower(@RequestParam("bl_writer") String bl_writer, HttpSession session, HttpServletResponse response) throws Exception {
        String session_iuid = null;

        try {
            session_iuid = sessionCheck.myGetSessionIuid(session);
            boardService.unFollow(bl_writer, session_iuid);
            return "unFollow";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //TODO 일단 팔로워한 사람 글 뽑느거 했는데 필요하면 쓰셈
    @RequestMapping(value = "/followerBoard")
    public String followerBoard(HttpSession session) {
        String my_iuid = sessionCheck.myGetSessionIuid(session);
        //List<BoardDTO> list = boardService.getFollowerBoard(my_iuid);
        return "/board/boardManage"; //임시 리턴
    }


    //TODO 수정 필요함 일단 만들어둠...! 마이블럭
    @RequestMapping(value = "/myBlock")
    public String manageBlock(HttpSession session, @RequestParam String token, Model model) {

        String my_iuid = sessionCheck.myGetSessionIuid(session);

        if(token.equals("my")) {
            List<BoardDTO> list = boardService.getMyBlock(my_iuid);
        }else if(token.equals("follow")){

        }

        return "/board/boardManage";
    }

    //팔로워한 사람 리스트
    @RequestMapping(value = "/followlist")
    public List<MemberDTO> myFollower(HttpSession session) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String my_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();
        logger.info("내 세션아유아디"+my_iuid);
        List<MemberDTO> list = boardService.getMyFollower(my_iuid);



        logger.info("리스트"+list);

        return list;
    }



    // 팔로워한사람 블럭
    @RequestMapping(value="follwerBlock")
    public String followerBlock(HttpSession session, String follow_iuid){
        String my_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();
        List<BoardDTO> board = boardService.getFollowerBoard(follow_iuid, my_iuid);
        return "";
    }

    @RequestMapping(value="mylikesBlock")
    @ResponseBody
    public List<Map<String,Object>> myLikesBlock(HttpSession session) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String my_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();
        List<Map<String,Object>> board = boardService.getMyLikesBlock(my_iuid);
        return board;
    }

    @RequestMapping(value="followRecommend")
    @ResponseBody
    public List<Map<String,Object>> followRecommend(HttpSession session){
        String my_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();
        List<Map<String,Object>> followRecommend = boardService.followRecommend(my_iuid);
        return followRecommend;
    }

    /*@RequestMapping(value = "/getUserLikedBlock", method = RequestMethod.GET)
    public List<BoardDTO> getUserLikedBlock(HttpSession session){
        String my_iuid = sessionCheck.myGetSessionIuid(session);
        logger.info(my_iuid);
        List<BoardDTO> list = boardService.getUserLikedBlock(my_iuid);
        logger.info(""+list.get(0).getBl_writer());
        return list;
    }*/

}