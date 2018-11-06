package hi.im.jenga.board.controller;

import hi.im.jenga.board.service.MongoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class BoardController {
    private static final Logger logger= LoggerFactory.getLogger(BoardController.class);
    private final MongoService mongoService;

    public BoardController(MongoService mongoService) {
        this.mongoService = mongoService;
    }


    @RequestMapping(value = "/mongo")
    public String mongo(){
        mongoService.getAnyway();
        return "mongo";
    }
}