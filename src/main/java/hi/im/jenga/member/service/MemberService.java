package hi.im.jenga.member.service;

import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;

public interface MemberService {
    int addMemberInfo(MemberDTO memberDTO);

    void addEMember(EmailMemberDTO emailMemberDTO, String iuid);

    void addSMember(SocialMemberDTO socialMemberDTO, String iuid);

    boolean isExist(String id);
}
