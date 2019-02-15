package hi.im.jenga.util.session;

import hi.im.jenga.member.dto.MemberDTO;

import javax.servlet.http.HttpSession;

public class MemberSession implements SessionAction {

    public boolean equalsToSomething(HttpSession session, String compareKey) {

        MemberDTO member = (MemberDTO) SessionValidate.getValidSessionObj(session, "Member");
        if (member.getMem_iuid().equals(compareKey)) return true;
        return false;
    }

    public String getMemberUid(HttpSession session){

        MemberDTO member = (MemberDTO) SessionValidate.getValidSessionObj(session, "Member");
        return member.getMem_iuid();


    }

}
