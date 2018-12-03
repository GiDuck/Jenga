package hi.im.jenga.member.dao;


import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import java.util.List;

public interface MemberDAO {
    int addMemberInfo(MemberDTO memberDTO);

    void addEMember(String aes_iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    boolean isSMExist(String aes_sid);

    String isEMExist(String aes_eid) throws Exception;

    void findEPwd(String aes_find_pwd, String sha_key);

    String checkEmail(EmailMemberDTO emailMemberDTO);

    String checkPwd(EmailMemberDTO emailMemberDTO);

    MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO);

    void sendKey(EmailMemberDTO emailMemberDTO) throws Exception;

    void tempIns(String iuid);

    boolean authCheck(EmailMemberDTO emailMemberDTO);

    String findIuid(EmailMemberDTO emailMemberDTO);

    void delMemInfo(String aes_iuid);

    void updMemInfo(MemberDTO memberDTO);

//    MemberDTO modMemberInfo(String aes_iuid);

    void addMemberFavor(String aes_iuid, String fav);

    String checkAuth(EmailMemberDTO emailMemberDTO);

    List<String> getMemFavor(String member);

    MemberDTO modMemberInfoGET(String aes_iuid);

    MemberDTO modMemberInfoPOST(String s_iuid, MemberDTO memberDTO, String aes_em_pwd, String[] favor) throws Exception;

    String getMemProfile(String s_iuid);

    MemberDTO testParam();
}
