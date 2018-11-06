package hi.im.jenga.member.service;

import hi.im.jenga.member.dto.*;

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

    boolean isSMExist(String aes_sid);

    String isEMExist(String em_id) throws Exception;

    int findEPwd(String find_pwd) throws Exception;

    String checkEmail(EmailMemberDTO emailMemberDTO) throws Exception; //이메일, 패스워드 체크

    MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO); //체크 후 그 아이디 토큰 얻어옴(iuid) 이메일 회원가입용

    void join(EmailMemberDTO emailMemberDTO);

    String sendKey(EmailMemberDTO emailMemberDTO) throws Exception;

    boolean authCheck(EmailMemberDTO emailMemberDTO) throws Exception;

    String findIuid(EmailMemberDTO emailMemberDTO) throws Exception;

    void delMemInfo(String session_mem_iuid) throws Exception;

    void updMemInfo(MemberDTO memberDTO);

    void addMemberFavor(String aes_iuid, String[] favor);

    MemberDTO modMemberInfoGET(MemberDTO memberDTO) throws Exception;

    List<String> getMemFavor(String member);
}