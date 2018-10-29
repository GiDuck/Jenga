package hi.im.jenga.member.service;

import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.dto.AuthMemberDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MemberService {
    void addMemberInfo(MemberDTO memberDTO) throws Exception;

    void addEMember(String aes_iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    void addAMember(AuthMemberDTO authMemberDTO) throws Exception;

    boolean isSMExist(String aes_sid);

    String isEMExist(String em_id) throws Exception;

    boolean isAMExist(String aes_eid);

    void findEPwd(String find_pwd) throws Exception;

    void findAPwd(String find_pwd) throws Exception;

    String checkEmail(EmailMemberDTO emailMemberDTO) throws Exception; //이메일, 패스워드 체크

    MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO); //체크 후 그 아이디 토큰 얻어옴(iuid) 이메일 회원가입용

    void loginEMCheck(EmailMemberDTO emailMemberDTO) throws Exception;

    void join(EmailMemberDTO emailMemberDTO);

    String sendKey(EmailMemberDTO emailMemberDTO) throws Exception;

    boolean authCheck(EmailMemberDTO emailMemberDTO) throws Exception;

    String findIuid(EmailMemberDTO emailMemberDTO) throws Exception;

}
