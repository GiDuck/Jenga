package hi.im.jenga.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.board.service.MongoService;
import hi.im.jenga.board.util.BoardUtilFile;
import hi.im.jenga.member.dto.MemberDTO;
import org.apache.http.HttpResponse;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springsource.loaded.ri.Exceptions;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * 글 조회 GET
 * 글 작성 GET / POST
 * 글 수정 GET / POST (PATCCH)
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
    private static final Logger logger= LoggerFactory.getLogger(BoardController.class);
    private final MongoService mongoService;
    private final BoardService boardService;
    private final BoardUtilFile boardUtilFile;

    @Autowired
    public BoardController(MongoService mongoService, BoardService boardService, BoardUtilFile boardUtilFile) {
        this.mongoService = mongoService;
        this.boardService = boardService;
        this.boardUtilFile = boardUtilFile;
    }


    /*
    * 검색창 하나만 띄우는 페이지
    * */
    @RequestMapping("/search")
    public String boardSearch(String search, String search_check,HttpSession session){
            String session_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();
            boardService.search(search,search_check, session_iuid);

        return "/search";
    }


    @GetMapping(value="/boardView")
    public String getBoardList() {

        return "stackBoard/boardView";
    }

    /*
     * 글 조회 GET
     * 조회수, 좋아요 표시
     * @param bl_uid : 글 UID
     * */
    @GetMapping(value="/boardDetail")
    public String getBoardDetail(@RequestParam("bl_uid") String bl_uid, Model model,  MongoDTO mongoDTO) {

        Map<String, String[]> map = boardService.getView(bl_uid);
        String bl_writer = String.valueOf(map.get("BL_WRITER"));
        logger.info(bl_writer);
        String resultHTML = boardService.getBookMarkFromHTML(bl_writer);    // writer 줘야함
        mongoDTO = mongoService.getView("_refBoardId", bl_uid);

        logger.info(resultHTML);
        logger.info(mongoDTO.get_value().toString());

        model.addAttribute("map", map);
        model.addAttribute("mongoDTO", mongoDTO);
        model.addAttribute("resultHTML", resultHTML);
        return "stackBoard/boardDetailView";
    }








    // 글쓰는 페이지 GET, 글 수정 페이지 GET
    @RequestMapping(value = "/stackBlock", method = RequestMethod.GET)
    public String getWriteView(HttpSession session, Model model, String status, MongoDTO mongoDTO, @RequestParam (value = "bl_uid", required = false)String bl_uid) throws JsonProcessingException {
//  TODO  status 없이 그냥 url로 접근하면 잘못된 페이지 띄우기 -> 임시로 / 로 감
        if(status == null) return "redirect:/";
        if(status.equals("stack")) {
            String session_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();

            Map<String, List<String>> category = boardService.getCategoryName();
            ObjectMapper mapper = new ObjectMapper();

            String categoryJSON = mapper.writeValueAsString(category);

            String resultHTML = boardService.getBookMarkFromHTML(session_iuid);         // 세션체크

            logger.info(resultHTML);

            model.addAttribute("category", categoryJSON);
            model.addAttribute("resultHTML", resultHTML);

            return "editor/stackBoard/stackBlock";

        }else if(status.equals("modify")) {


            Map<String, String[]> map = boardService.modifyViewGET(bl_uid);

            mongoDTO = mongoService.modifyViewGET("_refBoardId", bl_uid);

            logger.info(mongoDTO.get_value().toString());
            logger.info("컨트롤러 맵은 " + map);
            /*
             * 뽑는 예시
             * map.get("BL_WRITER")
             * map.get("tag1")
             * map.get("tag2")
             */

            model.addAttribute("map", map);
            model.addAttribute("mongoDTO", mongoDTO);

            return "editor/stackBoard/stackBlock";

        }

        return null;

    }

    /*
    * stackBlock에서 작성한 북마크, 글, 사진을 업로드하는 메서드(POST)
    *
    * session_iuid => sql의 조건   bl_writer -> mem_iuid
    * objId 생성해서 MongoDB랑 연결해야함
    *
    * BoardDTO = bl_writer, bl_title, bl_description, bl_date
    *
    * Main Image 받아와야함
    *
    * tbl_block, tbl_blockTags, tbl_thumbImg
    *
    * Bookmarks 값 json으로 받아야함
    *
    * // TODO WriteViewPOST => WriteBlockPOST 로 이름 바꾸기
    * // TODO 임시로 데이터 넣은거임. 받아서 해야함 / 조회수 Default 0, 좋아요(mem_iuid) nullable, 관심(mem_iuid) nullable
    */




    //    public String WriteViewPOST(BoardDTO boardDTO, HttpSession session/*, @RequestParam("bti_url") MultipartFile uploadFile, MultipartHttpServletRequest request, @RequestParam String [] bt_name*/) throws Exception {


        /*
        logger.info(boardDTO.getBl_mainCtg());
        logger.info(boardDTO.getBl_smCtg());
        logger.info(boardDTO.getBl_description());
        logger.info(boardDTO.getBl_introduce());
        logger.info(boardDTO.getBl_title());
        logger.info(boardDTO.getBt_name().toString());
        logger.info(boardDTO.getBt_name()[0]);
        logger.info(boardDTO.getBt_name()[1]);
        logger.info(boardDTO.getBl_date().toString());
        logger.info(boardDTO.getBl_writer());           // mem_iuid
        */

    // 글쓰는페이지 POST / 작성
    @RequestMapping(value="/uploadBlock", method = RequestMethod.POST, produces="multipart/form-data; charset=utf-8")
    public @ResponseBody String WriteViewPOST(BoardDTO boardDTO, HttpSession session, @RequestPart(value = "bti_url", required = false) MultipartFile uploadFile,
                                              @RequestParam("bl_bookmarks") String bl_bookmarks) throws Exception {

        logger.info("session에서 뽑아온 iuid는 " + ((MemberDTO) (session.getAttribute("Member"))).getMem_iuid());
        boardDTO.setBl_uid(UUID.randomUUID().toString());

        boardDTO.setBl_writer(((MemberDTO) session.getAttribute("Member")).getMem_iuid());
        String uploadName;
       if(uploadFile != null){
           uploadName = boardUtilFile.fileUpload(uploadFile, "image");
       }else{
           uploadName = "";
       }
//        service에서 디폴트이미지 처리

        String flag = "s";
        boardDTO.setBl_smCtg(boardService.transCtgUID(boardDTO.getBl_smCtg(),flag));
        flag = "m";
        boardDTO.setBl_mainCtg(boardService.transCtgUID(boardDTO.getBl_mainCtg(),flag));

        logger.info("DTO"+boardDTO.getBl_uid()+"/스몰/"+boardDTO.getBl_smCtg()+"/메인/"+boardDTO.getBl_mainCtg());

        boardService.writeViewBlock(boardDTO, uploadName, bl_bookmarks);

        logger.info("글작성 성공");

        return "success";
    }


    // TODO  like 상태값으로 비교   이거먼저하자
    // block iuid를 조건으로 insert mem_iuid(session에 있는)
    @RequestMapping(value = "/like/{bl_iuid}")
    public ResponseEntity<Void> like(@PathVariable String bl_iuid, HttpSession session){

        String session_mem_iuid = ((MemberDTO)(session.getAttribute("Member"))).getMem_iuid();

        boardService.likeCheck(bl_iuid, session_mem_iuid);

        int likeCount = boardService.likeCount(bl_iuid);

//      optional

        return new ResponseEntity<Void>(HttpStatus.OK);
//      return new ResponseEntity<Void>(Http.Status.BAD_REQUEST);
    }


//  TODO 테스트 mongo update도 함
//    수정페이지 POST    /modView  PATCH or PUT          json받아야함
    @RequestMapping(value = "/modView", method = RequestMethod.PATCH)
    public String modifyViewPOST(BoardDTO boardDTO, @RequestPart(value = "bti_url", required = false) MultipartFile uploadFile, @RequestParam("bl_bookmarks") String bl_bookmarks) {

        String uploadName;
        // 수정을 안하면 원래 이미지를 줘야함
        // 여기서 nullpointException 뜨면 여기서 boardService.getUploadName() 해야하고
//         넘어가면 서비스impl에서 처리
        if(uploadFile != null){
            uploadName = boardUtilFile.fileUpload(uploadFile, "image");
        }else{
            uploadName = "";
        }

        boardService.modifyViewPOST(boardDTO, uploadName, bl_bookmarks);

        return "";
    }

//    TODO 테스트하기  mongo도 지움 / HttpMethod 사용한것 테스트
//    View에서 받는거 테스트해야함
//    삭제페이지 POST
    @RequestMapping(value = "/delBlock", method=RequestMethod.DELETE)
    public ResponseEntity deleteBlock(@RequestParam String bl_uid){

        int result = boardService.deleteBlock(bl_uid);

        if(result == 0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);

    }


    // 북마크 파일업로드  /  완료
    @RequestMapping(value="/fileUpload", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity fileUpload(@RequestParam String bp_browstype, @RequestParam String bp_booktype, @RequestParam("file") MultipartFile uploadFile, HttpSession session) {
        String session_iuid  = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();
        ResponseEntity<String> result;
        BlockPathDTO blockPathDTO = new BlockPathDTO();
        logger.info("북마크 타입은 "+ bp_booktype);
        logger.info("브라우저 타입은 "+ bp_browstype);

        try {
            logger.info("업로드 된 파일");
            logger.info("파일 이름은 "+uploadFile.getOriginalFilename());
            logger.info("파일 사이즈 "+uploadFile.getSize());
            logger.info("머고이건 "+uploadFile.getBytes().toString());

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

        }catch(Exception e) {

            e.printStackTrace();

            result = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return result;

    }

    @PostMapping(value="/uploadBookmarks")
    public void uploadBookmark (@RequestPart("bookmark") String bookmark) {






    }

                                  /***   임시   ***/


    @RequestMapping(value = "/mongo")
    public String mongo(HttpSession session) throws ParseException {

//        MemberDTO member = (MemberDTO)session.getAttribute("Member");
//        JSONParser jsonParser = new JSONParser();
//        System.out.println(boardService.getBookMark().length());
//        JSONObject json = (JSONObject)jsonParser.parse(boardService.getBookMark());
//
//        mongoService.getAnyway(member,json);
        return "/mongo";
    }



    @RequestMapping(value = "/follow", method=RequestMethod.POST)
    @ResponseBody
    public void follower(String bl_writer, HttpSession session, HttpServletResponse response) throws IOException {
        String session_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();

        try {
            boardService.follow(bl_writer,session_iuid);
            response.getWriter().println("success");
        }catch (Exception e){
            response.getWriter().println("error");
        }
    }

    @RequestMapping(value = "/unfollow", method=RequestMethod.POST)
    @ResponseBody
    public void unfollower(String bl_writer, HttpSession session, HttpServletResponse response) throws IOException {
        String session_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();

        try {
            boardService.unfollow(bl_writer,session_iuid);
            response.getWriter().println("success");
        }catch (Exception e){
            response.getWriter().println("error");
        }
    }


    //TODO 일단 팔로워한 사람 글 뽑느거 했는데 필요하면 쓰셈
    @RequestMapping(value = "/followerboard")   //팔로워 한 사람 글 뽑아오기.  필요하면 받아쓰셈 ㅋ
    public String followerboard(HttpSession session){
        String My_iuid = ((MemberDTO)session.getAttribute("member")).getMem_iuid();
        return ""; //임시 리턴
    }




    //TODO 수정 필요함 일단 만들어둠...!
    @RequestMapping(value = "/myBlock")
    public String myBlock(HttpSession session){

        String my_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();


        List<BoardDTO> mylist = boardService.getMyBlock(my_iuid);


        return ""; //임시 리턴
    }





}






/*
 * 수정페이지 GET
 * 뷰단에 회원정보를 Map으로 던져줌
 * //TODO 세션체크 없거나 틀리면 '권한이 없습니다'
 * /modView?bl_uid=asdfasdfasdfasdf
 * /stackBlock
 *//*

    @RequestMapping(value = "/modView/{bl_uid}", method = RequestMethod.GET)
    public String modifyViewGET(@PathVariable("bl_uid") String bl_uid, Model model, MongoDTO mongoDTO){
        Map<String, String[]> map = boardService.modifyViewGET(bl_uid);

        mongoDTO = mongoService.modifyViewGET("_refBoardId", bl_uid);

        logger.info("컨트롤러 맵은 "+map);
        */
/*
 * 뽑는 예시
 * map.get("BL_WRITER")
 * map.get("tag1")
 * map.get("tag2")
 *//*


        model.addAttribute("map",map);
        model.addAttribute("mongoDTO", mongoDTO);
        return "/modBlock";

    }
*/

/*
    @RequestMapping(value = "/{bl_uid}", method = RequestMethod.GET)
    public String getView(@PathVariable String bl_uid, Model model, MongoDTO mongoDTO){

        Map<String, String[]> map = boardService.getView(bl_uid);
        mongoDTO = mongoService.getView("_refBoardId", bl_uid);

        model.addAttribute("map", map);
        model.addAttribute("mongoDTO", mongoDTO);

        return ""; // 수정페이지/{bl_uid}
    }

    */