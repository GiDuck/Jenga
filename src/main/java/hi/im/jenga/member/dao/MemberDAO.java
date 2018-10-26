package hi.im.jenga.member.dao;

import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.dto.AuthMemberDTO;

public interface MemberDAO {
    int addMemberInfo(MemberDTO memberDTO);

    void addEMember(EmailMemberDTO emailMemberDTO, String iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    void addAMember(AuthMemberDTO authMemberDTO);

    boolean isSMExist(String aes_sid);

    boolean isEMExist(String aes_eid);

    boolean isAMExist(String aes_eid);

    void findEPwd(String find_pwd, String tempPwdKey);

    void findAPwd(String find_pwd, String tempPwdKey);

    String checkEmail(EmailMemberDTO emailMemberDTO);

    String checkPwd(EmailMemberDTO emailMemberDTO);

    String getToken(EmailMemberDTO emailMemberDTO);
}
