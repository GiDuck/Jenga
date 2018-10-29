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

    void addEMember(EmailMemberDTO emailMemberDTO, String iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    void addAMember(AuthMemberDTO authMemberDTO);

    boolean isSMExist(String aes_sid);

    String isEMExist(String aes_eid) throws Exception;

    boolean isAMExist(String aes_eid);

    void findEPwd(String find_pwd, String tempPwdKey);

    void findAPwd(String find_pwd, String tempPwdKey);

    void sendKey(EmailMemberDTO emailMemberDTO) throws Exception;

    void tempIns(String iuid);

    boolean authCheck(EmailMemberDTO emailMemberDTO);
}
