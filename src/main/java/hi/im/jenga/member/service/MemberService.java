package hi.im.jenga.member.service;

import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.dto.AuthMemberDTO;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MemberService {
    int addMemberInfo(MemberDTO memberDTO);

    void addEMember(EmailMemberDTO emailMemberDTO, String iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    void addAMember(AuthMemberDTO authMemberDTO) throws Exception;

    boolean isSMExist(String aes_sid);

    boolean isEMExist(String aes_eid);

    boolean isAMExist(String aes_eid);

    void findEPwd(String find_pwd) throws Exception;

    void findAPwd(String find_pwd) throws Exception;

    String checkEmail(EmailMemberDTO emailMemberDTO) throws Exception;

    String getToken(EmailMemberDTO emailMemberDTO);
}
