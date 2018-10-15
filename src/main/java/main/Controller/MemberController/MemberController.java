package main.Controller.MemberController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    @Autowired
    private MongoService mongoService;

    @RequestMapping(value = "")
    public String hi(){
        mongoService.getAnyway();
        return "home";
    }
}
