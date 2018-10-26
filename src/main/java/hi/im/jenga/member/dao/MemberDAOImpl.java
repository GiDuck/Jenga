package hi.im.jenga.member.dao;


import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.dto.AuthMemberDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberDAOImpl implements MemberDAO{

    private static final Logger logger = LoggerFactory.getLogger(MemberDAOImpl.class);
    @Autowired
    SqlSession sqlSession;

    public int addMemberInfo(MemberDTO memberDTO) {
        /*HashMap<String,Object> map = new HashMap();
        map.put("memberDTO", memberDTO);
        map.put("uploadPath", uploadPath);*/

        return sqlSession.insert("member.addMemberInfo", memberDTO);
    }

    public void addEMember(EmailMemberDTO emailMemberDTO, String iuid) {
        HashMap<String, Object> map = new HashMap();
        map.put("emailMemberDTO", emailMemberDTO);
        map.put("iuid", iuid);

        sqlSession.insert("member.addEMember", map);
    }

    public void addSMember(SocialMemberDTO socialMemberDTO, String iuid) {
        HashMap<String, Object> map = new HashMap();
        map.put("socialMemberDTO", socialMemberDTO);
        map.put("iuid", iuid);

        sqlSession.insert("member.addSMember", map);
    }

    public boolean isSMExist(String aes_sid) {
        String result = sqlSession.selectOne("member.isSMExist", aes_sid);
        return result == null? false : true;
    }

    public boolean isEMExist(String aes_eid) {
        String result = sqlSession.selectOne("member.isEMExist", aes_eid);
        return result == null? false : true;
    }

    public boolean isAMExist(String aes_eid) {
        String result = sqlSession.selectOne("member.isAMExist", aes_eid);
        return result == null? false : true;
    }

    public void findEPwd(String find_pwd, String tempPwdKey) {
        logger.info(": : : findEPwd 들어옴 ");
        HashMap <String, String> map = new HashMap();
        map.put("find_pwd", find_pwd);
        map.put("tempPwdKey", tempPwdKey);

        sqlSession.update("member.findEPwd",map);
        logger.info(": : : findEPwd 나감 ");
    }

    public void findAPwd(String find_pwd, String tempPwdKey) {
        logger.info(": : : findAPwd 들어옴 ");
        logger.info(": : :" + find_pwd);
        logger.info(": : :" + tempPwdKey);

//        String s = "KpDuk+2/4UWsdYMDzk4v7uLU03pdQ68xhEVF2DnV/rs=";
        HashMap<String, String> map = new HashMap();
        map.put("find_pwd", find_pwd);
        map.put("tempPwdKey", tempPwdKey);
        System.out.println(map.get("find_pwd"));
        System.out.println(map.get("tempPwdKey"));

        sqlSession.update("member.findAPwd",map);

        logger.info(": : : findAPwd 나감 ");
    }

    public String checkEmail(EmailMemberDTO emailMemberDTO) {
        String checkid = sqlSession.selectOne("member.checkid",emailMemberDTO);

        return checkid;
    }
    public String checkPwd(EmailMemberDTO emailMemberDTO){
        String checkpwd = sqlSession.selectOne("member.checkpass",emailMemberDTO);

        return checkpwd;
    }

    public String getToken(EmailMemberDTO emailMemberDTO) {
        return sqlSession.selectOne("member.getToken",emailMemberDTO);
    }

    public void addAMember(AuthMemberDTO authMemberDTO) {
        sqlSession.insert("member.addAMember", authMemberDTO);
    }
}
