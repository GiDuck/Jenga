package hi.im.jenga.board.controller;

import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.board.service.BoardServiceImpl;
import hi.im.jenga.board.service.MongoService;
import hi.im.jenga.member.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/board")
public class BoardController {
    private static final Logger logger= LoggerFactory.getLogger(BoardController.class);
    private final MongoService mongoService;
    private final BoardService boardService;

    @Autowired
    public BoardController(MongoService mongoService, BoardServiceImpl boardService) {
        this.mongoService = mongoService;
        this.boardService = boardService;
    }


    @RequestMapping(value="/stackBlock", method = RequestMethod.GET)
    public String getWriteView(Model model) {
        String resultJSON = boardService.getBookMark();
        model.addAttribute("resultJSON", resultJSON);


        return "editor/stackBoard/stackBlock";
    }

    /*
    *
    * stackBlock에서 작성한 북마크, 글, 사진을 업로드하는 메서드(POST)
    *
    * session_iuid => sql의 조건   bl_writer -> mem_iuid
    * objId 생성해서 MongoDB랑 연결해야함
    *
    * BoardDTO = bl_writer, bl_title, bl_description, bl_date
    *
    * Main Image 받아와야함
    *
    * tbl_block
    * tbl_blockTags
    * tbl_thumbImg
    */
    @RequestMapping(value="/stackBlock", method = RequestMethod.POST)
    public String WriteViewPOST(BoardDTO boardDTO, HttpSession session) throws Exception {
        logger.info("session에서 뽑아온 iuid는 "+((MemberDTO)(session.getAttribute("Member"))).getMem_iuid());
        String session_iuid = ((MemberDTO)session.getAttribute("Member")).getMem_iuid();

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

        boardService.writeViewPOST(session_iuid, boardDTO);

        return "/";
    }

    @RequestMapping(value = "/mongo")
    public String mongo(){
        mongoService.getAnyway();
        return "/mongo";
    }
}
