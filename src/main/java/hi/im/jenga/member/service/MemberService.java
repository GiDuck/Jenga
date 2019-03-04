package hi.im.jenga.member.service;


import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.util.status_code.AuthStatusCode;
import hi.im.jenga.member.util.LoginType;
import hi.im.jenga.member.util.UserInfoType;

import java.util.List;
import java.util.Map;

public interface MemberService {
    void addMemberInfo(String authId, MemberDTO memberDTO, String uploadName, LoginType key);

    void addEmailMember(String aes_iuid);

    void addSocialMember(SocialMemberDTO socialMemberDTO, String sMem_iuid);

    MemberDTO getExistMember(String aes_sid);

    AuthStatusCode isEmailMemberExists(String emailMemUid);

    AuthStatusCode findEmailPwd(String findPwd);

    AuthStatusCode loginCheck(EmailMemberDTO emailMemberDTO);

    MemberDTO getUserInfo(String userUid);

    AuthStatusCode sendKey(EmailMemberDTO emailMemberDTO);

    AuthStatusCode authCheck(EmailMemberDTO emailMemberDTO);

    String findMemUidByEmail(String userEmail) ;

    void delMemInfo(String memUid);

    MemberDTO modMemberInfoGET(MemberDTO memberDTO);

    MemberDTO modMemberInfoPOST(String memUid, String memNick, String uploadFilePath, String memPwd, String[] favor) throws Exception;

    void addMemberFavor(String encodedAesUid, String[] favor);

    List<String> getMemFavor(String member);

    List<Map<String,String>> getCategory();

    Map<String, String> getUserInfo(String mem_iuid, List<UserInfoType> typeList);

    String getBookmarkUploadDate(String memUid);

    AuthStatusCode changePwd(String memUid, String pwd);

    int deleteWhetherRegInfo(String memUid);

}

