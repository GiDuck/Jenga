package hi.im.jenga.member.dao;

import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;

public interface MemberDAO {
    int addMemberInfo(MemberDTO memberDTO);

    void addEMember(EmailMemberDTO emailMemberDTO, String iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    boolean isExist(String id);
}
