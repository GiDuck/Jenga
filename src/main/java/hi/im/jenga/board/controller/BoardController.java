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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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

    public BoardController(MongoService mongoService, BoardService boardService, BoardUtilFile boardUtilFile) {
        this.mongoService = mongoService;
        this.boardService = boardService;
        this.boardUtilFile = boardUtilFile;
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String SearchGET(String search, String search_check, HttpSession session) throws Exception {


        return "stackBoard/boardSearch";
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
    public String getBoardDetail(@RequestParam("bl_uid") String bl_uid, Model model) throws Exception {

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


            model.addAttribute("category", categoryJSON);
            if(!resultHTML.equals("notExist")) {
                model.addAttribute("resultHTML", resultHTML);
            }

            Map<String, String> map = new HashMap<>();
            map.put("bookmarks", "stack");
            model.addAttribute("map",map);

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



    //TODO ResponseBody로 board_uid 리턴해줘
    // 글쓰는페이지 POST / 작성
    @RequestMapping(value = "/uploadBlock/stack", method = RequestMethod.POST, produces = "multipart/form-data; charset=utf-8")
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

    // block iuid를 조건으로 insert mem_iuid(session에 있는)
    //  TODO 테스트 mongo update도 함
//    수정페이지 POST    /uploadBlock  PATCH or PUT          json받아야함
    @RequestMapping(value = "/uploadBlock/modify", method = RequestMethod.POST)
    @ResponseBody
    public String modifyViewPOST(BoardDTO boardDTO, @RequestParam String bl_uid, @RequestPart(value = "bti_url", required = false) MultipartFile uploadFile, @RequestParam("bl_bookmarks") String bl_bookmarks) {

        String uploadName;
        logger.info(boardDTO.toString());

        String flag = "s";
        boardDTO.setBl_smCtg(boardService.transCtgUID(boardDTO.getBl_smCtg(), flag));
        flag = "m";
        boardDTO.setBl_mainCtg(boardService.transCtgUID(boardDTO.getBl_mainCtg(), flag));

        logger.info(boardDTO.toString());
        // 수정을 안하면 원래 이미지를 줘야함
        // 여기서 nullpointException 뜨면 여기서 boardService.getUploadName() 해야하고
//         넘어가면 서비스impl에서 처리
        if (uploadFile != null) {
            uploadName = boardUtilFile.fileUpload(uploadFile, "image");
        } else {
            uploadName = "";
        }

        boardService.modifyViewPOST(boardDTO, uploadName, bl_bookmarks);
        logger.info(bl_uid);
        return bl_uid;
    }


    @RequestMapping(value = "/like/{bl_iuid}")
    public @ResponseBody int like(@PathVariable String bl_iuid, HttpSession session){
        String session_mem_iuid = sessionCheck.myGetSessionIuid(session);
        boardService.likeCheck(bl_iuid, session_mem_iuid);
        return  boardService.likeCount(bl_iuid);
    }

    @RequestMapping(value = "/isLikeExist/{bl_uid}", method = RequestMethod.GET)
    public @ResponseBody String isLikeExist(HttpSession session, @PathVariable("bl_uid") String bl_iuid) {
        String session_mem_iuid = sessionCheck.myGetSessionIuid(session);
        String check = boardService.isLikeExist(bl_iuid, session_mem_iuid);
        return check != null ? "exist" : "notExist";
    }


    //    TODO 테스트하기  mongo도 지움 / HttpMethod 사용한것 테스트  누가 GET으로 바꿨지 DELETE는 안되는것인가아하아
    @RequestMapping(value = "/delBlock", method = RequestMethod.GET)
    public String deleteBlock(@RequestParam String bl_uid) {

        int result = boardService.deleteBlock(bl_uid);

        return "redirect:/gboard/getMyBlockManage";

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

    @RequestMapping(value = "/followCheck", method = RequestMethod.GET)
    public @ResponseBody String followCheck(@RequestParam("bl_writer")String bl_writer, HttpSession session){
        String session_iuid;
     try{
        session_iuid = sessionCheck.myGetSessionIuid(session);
        if(session_iuid.equals(bl_writer)){
            return "";
        }
        String check = boardService.followCheck(bl_writer, session_iuid);
        return check;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @RequestMapping(value = "/follow", method = RequestMethod.GET)
    @ResponseBody
    public String follower(@RequestParam("bl_writer") String bl_writer, HttpSession session) {

        String session_iuid;
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
    public String unfollower(@RequestParam("bl_writer") String bl_writer, HttpSession session) {
        String session_iuid;
        try {
            session_iuid = sessionCheck.myGetSessionIuid(session);
            boardService.unFollow(bl_writer, session_iuid);
            return "unFollow";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //팔로워한 사람 리스트
    @RequestMapping(value = "/followlist")
    public List<MemberDTO> myFollower(HttpSession session) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String my_iuid = sessionCheck.myGetSessionIuid(session);
        logger.info("내 세션아유아디"+my_iuid);
        List<MemberDTO> list = boardService.getMyFollower(my_iuid);
        logger.info("리스트"+list);
        return list;
    }


// FIXME 아이디 공백으로 넘어오는거 인코딩하기
    // 내가 팔로워 한 사람들 중 한 명 클릭해서 그 사람 블럭 모두 뽑기
    @RequestMapping(value="/getFollowerBoard")
    public @ResponseBody List<Map<String,String>> followerBlock(HttpSession session, String follow_iuid){
        String my_iuid = sessionCheck.myGetSessionIuid(session);
        List<Map<String,String>> board = boardService.getFollowerBoard(follow_iuid, my_iuid);
        return board;
    }

//    내가 좋아요한 블록
    @RequestMapping(value="mylikesBlock")
    @ResponseBody
    public List<Map<String,Object>> myLikesBlock(HttpSession session) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String my_iuid = sessionCheck.myGetSessionIuid(session);
        List<Map<String,Object>> board = boardService.getMyLikesBlock(my_iuid);
        return board;
    }

    @RequestMapping(value="followRecommend")
    @ResponseBody
    public List<Map<String,Object>> followRecommend(HttpSession session) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String my_iuid = sessionCheck.myGetSessionIuid(session);
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


    //인기 블록
    // 기본 10개
    @RequestMapping(value="/getPopularBlock")
    public @ResponseBody List<Map<String, String>> getPopularBlock(@RequestParam(value = "likeCount", required = false) Integer likeCount) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        likeCount = Optional.ofNullable(likeCount).orElse(10);
        logger.info(likeCount.toString());
        List<Map<String, String>> list  = boardService.getPopularBlock(likeCount);
        return list;
    }

    //내 블록 관리 페이지
    @RequestMapping(value="/getMyBlockManage")
    public String getMyBlockManage(Model model, HttpSession session) {

        String my_iuid = sessionCheck.myGetSessionIuid(session);
        List<Map<String,String>> mylist = boardService.getMyBlock(my_iuid);
        String jsonStr = "";
        ObjectMapper mapper = new ObjectMapper();
        try{
            jsonStr =   mapper.writeValueAsString(mylist);
        }catch (Exception e){
            e.printStackTrace();
        }

        model.addAttribute("boards", jsonStr);

        return "myBoard/myBoardManage";
    }

    //팔로워 별로 글을 확인할 수 있는 페이지
    // 팔로워 추천 받아옴
    @RequestMapping(value="/getFollowerPage")
    public String getFollowerPage(HttpSession session,Model model) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String my_iuid = sessionCheck.myGetSessionIuid(session);
        List<Map<String, Object>> maps = boardService.followRecommend(my_iuid);
        logger.info("맵 확인"+maps);
        model.addAttribute("recommend",maps);
        return "following/followerPage";
    }




}