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
    int addMemberInfo(MemberDTO memberDTO) throws Exception;

    void addEMember(EmailMemberDTO emailMemberDTO, String iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    void addAMember(AuthMemberDTO authMemberDTO) throws Exception;

    boolean isSMExist(String aes_sid);

    String isEMExist(String em_id) throws Exception;

    boolean isAMExist(String aes_eid);

    void findEPwd(String find_pwd) throws Exception;

    void findAPwd(String find_pwd) throws Exception;

    void loginEMCheck(EmailMemberDTO emailMemberDTO) throws Exception;

    void join(EmailMemberDTO emailMemberDTO);

    String sendKey(EmailMemberDTO emailMemberDTO) throws Exception;

    boolean authCheck(EmailMemberDTO emailMemberDTO) throws Exception;
}
