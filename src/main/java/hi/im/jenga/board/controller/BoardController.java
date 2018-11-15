package hi.im.jenga.board.controller;

import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.board.service.MongoService;
import hi.im.jenga.board.util.BoardUtilFile;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.util.*;

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

    // 글쓰는페이지 GET
    // TODO 사용자의 업로드 파일을 읽어와서 String으로 반환 -> html경로 저장해서 html 파일을 읽어와서 String으로 반환해야함 /  기덕이 최신코드 주기로 함

    @RequestMapping(value="/stackBlock", method = RequestMethod.GET)
    public String getWriteView(HttpSession session, Model model) {
        String session_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();

        String resultJSON = boardService.getBookMark(session_iuid);

        logger.info(resultJSON);

        // TODO 코드 받고 다시
        model.addAttribute("resultJSON", resultJSON);


        return "editor/stackBoard/stackBlock";
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
    * //TODO WriteViewPOST => WriteBlockPOST 로 이름 바꾸기
    *
    */

    // 글쓰는페이지 POST
    @RequestMapping(value="/stackBlock", method = RequestMethod.POST)
    public String WriteViewPOST(BoardDTO boardDTO, HttpSession session/*, @RequestParam("bti_url") MultipartFile uploadFile, MultipartHttpServletRequest request, @RequestParam String [] bt_name*/) throws Exception {
        logger.info("session에서 뽑아온 iuid는 "+((MemberDTO)(session.getAttribute("Member"))).getMem_iuid());
        String session_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();

        // tbl_block
        // 임시   / 실제는 DTO 바로넘김 여기서 set안하고
        String bl_uid = UUID.randomUUID().toString();
        String bl_writer = "PvPmRRt6cKp0/qyTaXJw2UjVzUvG+voo0ux1oj1/N2Sj44pBLUiDiiyM+bJcgZJi";
        String bl_title = "조오오오은 사이트를 공유합니다";
        String bl_description = "개좋죠 이러쿵저러쿵쿵따리쿵쿵따쿵쿵따리쿵쿵따쿵쿵따리쿵쿵따쿵쿵쿵쿵유호준쿵쿵따준코쿵쿵따코주부쿵쿵따부잣집쿵쿵따집돌이쿵쿵따이새끼쿵쿵따끼인각쿵쿵따각시탈쿵쿵따탈의실쿵쿵따실실쪼개지마라쿵쿵따라라라라라";
        String bl_mainCtg = "category1";      // 문화/예술
        String bl_smCtg = "scategory1-4";     // 미술
//        String bl_date = "18/10/31";          // 임시로 String으로 받음
        String bl_objId = UUID.randomUUID().toString();

        boardDTO.setBl_uid(bl_uid);
        boardDTO.setBl_writer(bl_writer);
        boardDTO.setBl_title(bl_title);
        boardDTO.setBl_description(bl_description);
        boardDTO.setBl_mainCtg(bl_mainCtg);
        boardDTO.setBl_smCtg(bl_smCtg);
//        boardDTO.setBl_date(bl_date);
        boardDTO.setBl_objId(bl_objId);

        boardService.writeViewBlock(session_iuid, boardDTO);

        /////////////////////////썸네일이미지///////////////////////////
//        String uploadName = boardUtilFile.fileUpload(request,uploadFile, "image");
//
//        boardService.writeViewThumbImg(bl_uid, uploadName);



        ////////////////////////태그////////////////////////
        String [] bt_name = {"더치", "커피", "카페"};
        System.out.println(bt_name[1]);
        boardService.writeViewTag(bl_uid, bt_name);



//        TODO json mongo에 INSERT 하기
        /*
        mongo Block Insert
        블록쌓은거 (json)도 넘겨줘야함
        _Value는 json
        _refBoardId 는 bl_uid
        _blockId 는 ObjectId니까 따로 줄거 없다.

        mongoService.writeViewBmks(bl_uid, json);

        */

        mongoService.writeViewBmks(bl_uid);


        return "redirect:/";  // 임시로 보냄
    }

    /*
    * 수정페이지 GET
    * 뷰단에 Map 던져줌
    *
    * /modView?bl_uid=asdfasdfasdfasdf
    * /stackBlock
    */
    @RequestMapping(value = "/modView", method = RequestMethod.GET)
    public String modifyViewGET(@RequestParam String bl_uid, Model model, MongoDTO mongoDTO){
        Map<String, String[]> map = boardService.modifyViewGET(bl_uid);

        mongoDTO = mongoService.modifyViewGET("_refBoardId", bl_uid);

        logger.info("컨트롤러 맵은 "+map);
        /*
        * 뽑는 예시
        * map.get("BL_WRITER")
        * map.get("tag1")
        * map.get("tag2")
        */

        model.addAttribute("map",map);
        model.addAttribute("mongoDTO", mongoDTO);
        return "/modBlock";

    }

//    수정페이지 POST    /modView  PATCH or PUT          json받아야함
    @RequestMapping(value = "/modView", method = RequestMethod.POST)
    public String modifyViewPOST(BoardDTO boardDTO, @RequestParam("bti_url") MultipartFile uploadFile, MultipartHttpServletRequest request, @RequestParam String[] bt_name) {


        // 수정을 안하면 원래 이미지를 줘야함
        String uploadName = boardUtilFile.fileUpload(request, uploadFile, "image");

        boardService.modifyViewPOST(boardDTO, uploadName, bt_name);

//        TODO : Mongo json을 받아야함 / mongoService.modifyViewPOST();


        return "";
    }

//    TODO 삭제 테스트
//    View에서 받는거 테스트해야함
//    HttpMethod  DELETE로 준거 테스트
//    삭제페이지 POST
    @RequestMapping(value = "/delBlock", method=RequestMethod.DELETE)
    public ResponseEntity deleteBlock(@RequestParam String bl_uid){

        int result = boardService.deleteBlock(bl_uid);

        if(result == 0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);

    }




    // 북마크 파일업로드
    @RequestMapping(value="/fileUpload", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity fileUpload(@RequestParam String bp_browstype, @RequestParam String bp_booktype, @RequestParam("file") MultipartFile uploadFile, MultipartHttpServletRequest request, HttpSession session) {
        String session_iuid  = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();
        ResponseEntity<String> result;
        BlockPathDTO blockPathDTO = new BlockPathDTO();
        logger.info("북마크 타입은 "+ bp_booktype);
        logger.info("브라우저 타입은 "+ bp_browstype);

        try {
            logger.info("업로드 된 파일");
            logger.info("파일 이름은 "+uploadFile.getOriginalFilename());
            logger.info("사진 사이즈 "+uploadFile.getSize());
            logger.info("머고이건 "+uploadFile.getBytes().toString());

            String uploadPath = boardUtilFile.fileUpload(request, uploadFile, "block");

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

    @RequestMapping(value = "/mongoUpdate")
    public String mongoUpdate(){


        return "/";
    }

}
