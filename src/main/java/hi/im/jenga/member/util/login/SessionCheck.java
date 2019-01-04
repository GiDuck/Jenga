package hi.im.jenga.member.util.login;

import hi.im.jenga.member.dto.MemberDTO;

import javax.servlet.http.HttpSession;

public class SessionCheck {

    private static SessionCheck sessionCheck;

    private SessionCheck() {
    }

    public static SessionCheck getInstance(){
        if (sessionCheck == null){
            sessionCheck = new SessionCheck();
        }
        return sessionCheck;
    }

    public boolean myGetSession(HttpSession session) {
        if (session.getAttribute("Member") != null) {
            return true;
        }
        else{
            return false;
        }
    }

    public void mySetSession(HttpSession session, MemberDTO memberDTO) {
        session.setAttribute("Member",memberDTO);
    }

    public String myGetSessionIuid(HttpSession session){
        if (session.getAttribute("Member") != null) {
            return ((MemberDTO)session.getAttribute("Member")).getMem_iuid();
        }else{
            return "error";
        }
    }


}
