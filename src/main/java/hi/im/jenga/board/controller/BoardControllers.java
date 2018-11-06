package hi.im.jenga.board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class BoardControllers {
    /*public static final Logger logger = LoggerFactory.getLogger(BoardController.class);*/

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return "aestest";
    }
}
