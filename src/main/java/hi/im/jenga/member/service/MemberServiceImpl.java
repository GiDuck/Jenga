package hi.im.jenga.member.service;

import hi.im.jenga.member.dao.MemberDAO;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDAO dao;

    public int addMemberInfo(MemberDTO memberDTO) { return dao.addMemberInfo(memberDTO); }

    public void addEMember(EmailMemberDTO emailMemberDTO, String iuid) { dao.addEMember(emailMemberDTO, iuid); }

    public void addSMember(SocialMemberDTO socialMemberDTO, String iuid) { dao.addSMember(socialMemberDTO, iuid); }

    public boolean isExist(String id) { return dao.isExist(id); }
}
