package hi.im.jenga.Board.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberController {

    @RequestMapping(value = "/")
    public String hi(){

        return "main/main";
    }
    
    @RequestMapping(value = "/join")
    public String join(){

        return "member/join";
    }   
    
    @RequestMapping(value = "/setMemInfo")
    public String setMemberInfoPage(){ 	
    	
        return "member/setMemInfo";
    } 
    
    
    
}
