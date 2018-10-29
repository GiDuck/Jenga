package hi.im.jenga.member.dao;

import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.dto.AuthMemberDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MemberDAO {
    int addMemberInfo(MemberDTO memberDTO);

    void addEMember(String aes_iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    boolean isSMExist(String aes_sid);

    String isEMExist(String aes_eid) throws Exception;

    void findEPwd(String aes_find_pwd, String tempPwdKey);

    String checkEmail(EmailMemberDTO emailMemberDTO);

    String checkPwd(EmailMemberDTO emailMemberDTO);

    MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO);

    void sendKey(EmailMemberDTO emailMemberDTO) throws Exception;

    void tempIns(String iuid);

    boolean authCheck(EmailMemberDTO emailMemberDTO);

    String findIuid(EmailMemberDTO emailMemberDTO);

    void delMemInfo(String aes_iuid);
}
