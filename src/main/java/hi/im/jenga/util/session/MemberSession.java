package hi.im.jenga.util.session;

import hi.im.jenga.member.dto.MemberDTO;

import javax.servlet.http.HttpSession;

public class MemberSession implements SessionAction {


    public static final String MEMBER_SESSION_KEY = "Member";
    @Override
    public boolean equalsToSomething(HttpSession session, String compareKey) {

        MemberDTO member = (MemberDTO) SessionValidate.getValidSessionObj(session, MemberSession.MEMBER_SESSION_KEY);
        if (member.getMem_iuid().equals(compareKey)) return true;
        return false;
    }



    public static boolean equalsToMemberUid(HttpSession session, String expectedUid){
        MemberDTO  member = (MemberDTO) SessionValidate.getValidSessionObj(session, MemberSession.MEMBER_SESSION_KEY);
        return member.getMem_iuid().equals(expectedUid);

    }


    public static String getMemberUid(HttpSession session){

        MemberDTO member = (MemberDTO) SessionValidate.getValidSessionObj(session, MemberSession.MEMBER_SESSION_KEY);
        return member.getMem_iuid();

    }

    public static boolean setMemberSession(HttpSession session, MemberDTO dto){

        if(session!=null && dto != null) {
            session.setAttribute(MemberSession.MEMBER_SESSION_KEY, dto);
            return true;
        }

        return false;

    }

    public static boolean removeMemberSession(HttpSession session){

        if(session!=null) {
            session.removeAttribute(MemberSession.MEMBER_SESSION_KEY);
            return true;
        }

        return false;

    }


}
