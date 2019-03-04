package hi.im.jenga.member.dao;


import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MemberDAO {
    int addEmailMemInfo(MemberDTO memberDTO);

    void addEMember(String encodedAesUid);

    void addSocialMember(SocialMemberDTO socialMemberDTO, String memberUid);

    MemberDTO getExistMember(String memUid);

    String isEmailMemberExists(String encodedAesUid);

    void findEmailPwd(String encodedAesPwd, String shaKey) throws SQLException;

    int checkEmail(String userUid);

    int checkPwd(EmailMemberDTO emailMemberDTO);

    MemberDTO getUserInfo(String userUid);

    void sendKey(EmailMemberDTO emailMemberDTO) throws Exception;

    void tempIns(String memUid);

    int authCheck(EmailMemberDTO emailMemberDTO);

    String findMemUidByEmail(String email);

    void delMemInfo(String memUid);

    void addMemberFavor(String encodedAesUid, String favor);

    String getAuthToken(EmailMemberDTO emailMemberDTO);

    List<String> getMemFavor(String memberUid);

    MemberDTO modMemberInfoGET(String encodedAesUid);

    MemberDTO modMemberInfoPOST(String memUid, MemberDTO memberDTO, String[] favor) throws Exception;

    String getMemProfile(String memUid);

    List<Map<String,String>> getCategory();

    void addSocialMemInfo(MemberDTO memberDTO);

    String getBookmarkUploadDate(String memUid);

    int changePwd(String memUid, String encodedAesPwd);

    void insertWhetherRegInfo(String memUid);

    int deleteWhetherRegInfo(String memUid);

    public int selectWhetherRegInfo(String memUid);
}
