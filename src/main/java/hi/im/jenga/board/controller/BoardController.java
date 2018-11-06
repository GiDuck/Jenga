package hi.im.jenga.board.controller;

import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.board.service.MongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {
    private static final Logger logger= LoggerFactory.getLogger(BoardController.class);
    private final MongoService mongoService;
    private final BoardService boardService;

    @Autowired
    public BoardController(MongoService mongoService, BoardService boardService) {
        this.mongoService = mongoService;
        this.boardService = boardService;
    }
    @RequestMapping(value="/stackBlock")
    public String getWriteView(Model model) {


        String resultJSON = boardService.getBookMark();
        model.addAttribute("resultJSON", resultJSON);


        return "editor/stackBoard/stackBlock";
    }

    @RequestMapping(value = "/mongo")
    public String mongo(){
        mongoService.getAnyway();
        return "/mongo";
    }
}
