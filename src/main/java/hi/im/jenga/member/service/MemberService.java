package hi.im.jenga.member.service;


import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public interface MemberService {
    void addMemberInfo(SocialMemberDTO socialMemberDTO, EmailMemberDTO emailMemberDTO, MemberDTO memberDTO, String uploadName, String key) throws Exception;

    void addEMember(String aes_iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String sMem_iuid);

    MemberDTO isSMExist(String aes_sid);

    String isEMExist(String em_id) throws Exception;

    int findEPwd(String find_pwd) throws Exception;

    String checkEmail(EmailMemberDTO emailMemberDTO) throws Exception; //이메일, 패스워드 체크

    MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO); //체크 후 그 아이디 토큰 얻어옴(iuid) 이메일 회원가입용

    String sendKey(EmailMemberDTO emailMemberDTO) throws Exception;

    boolean authCheck(EmailMemberDTO emailMemberDTO) throws Exception;

    String findIuid(EmailMemberDTO emailMemberDTO) throws Exception;

    void delMemInfo(String session_mem_iuid) throws Exception;

    MemberDTO modMemberInfoGET(MemberDTO memberDTO) throws Exception;

    MemberDTO modMemberInfoPOST(String s_iuid, String mem_nick, String uploadName, String em_pwd, String[] favor) throws Exception;

    void updMemInfo(MemberDTO memberDTO);

    void addMemberFavor(String aes_iuid, String[] favor);

    List<String> getMemFavor(String member);

    MemberDTO testParam();

    List<Map<String,String>> getCategory();

    Map<String, String> getUserInfo(String mem_iuid, String check_profile, String check_nick, String check_introduce) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    Map<String, String> getBmksUploadDate(String session_iuid);

    void changePwd(String mem_iuid, String pwd) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    int countFollowerMember(String session_iuid, String search) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    List<BoardDTO> getFollowerMember(String session_iuid, String search, int startrow, int endrow) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    int countFollowingMember(String session_iuid, String search) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    List<BoardDTO> getFollowingMember(String session_iuid, String search, int startrow, int endrow) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;

    List<Map<String, String>> getRecentBlock(String mem_iuid) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException;
}

