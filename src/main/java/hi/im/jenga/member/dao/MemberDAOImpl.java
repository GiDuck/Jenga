package hi.im.jenga.member.dao;


import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.dto.AuthMemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.cipher.SHA256Cipher;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class MemberDAOImpl implements MemberDAO{

    private static final Logger logger = LoggerFactory.getLogger(MemberDAOImpl.class);
    @Autowired
    SqlSession sqlSession;
    @Autowired
    AES256Cipher aes256Cipher;

    public int addMemberInfo(MemberDTO memberDTO) {
        /*HashMap<String,Object> map = new HashMap();
        map.put("memberDTO", memberDTO);
        map.put("uploadPath", uploadPath);*/

        return sqlSession.update("member.addMemberInfo", memberDTO);
    }

    public void addEMember(String aes_iuid) {

        sqlSession.update("member.addEMember",aes_iuid);
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

    public String isEMExist(String aes_eid) throws Exception {
    logger.info("오이잉 daoimpl "+aes_eid);
        String result= sqlSession.selectOne("member.isEMExist", aes_eid);
        logger.info("빠져나와라 "+result);

        /*if(result.equals("Y ")){
            logger.info(result+" 로 나왔다");
            return "Y";
        }else if(result.equals("N ")){
            logger.info(result+" 로 나왔다2");
            return "N";
        }
        // null 일 때
        logger.info(result+" 로 나왔다3");

        return "notexist";*/
        return result != null ? (result.equals("Y ") ? "Y" : "N") : "notexist";
    }

    public void findEPwd(String aes_find_pwd, String tempPwdKey) {
        logger.info(": : : findEPwd 들어옴 ");
        HashMap <String, String> map = new HashMap();
        map.put("aes_find_pwd", aes_find_pwd);
        map.put("tempPwdKey", tempPwdKey);

        int n = sqlSession.update("member.findEPwd",map);

        logger.info(": : : n은 "+n);
        logger.info(": : : findEPwd 나감 ");
    }

    public void findAPwd(String find_pwd, String tempPwdKey) {
        HashMap<String, String> map = new HashMap();
        map.put("find_pwd", find_pwd);
        map.put("tempPwdKey", tempPwdKey);

        sqlSession.update("member.findAPwd",map);

    }

    public void tempIns(String iuid) {

        sqlSession.insert("member.tempIns", iuid);
    }

    public boolean authCheck(EmailMemberDTO emailMemberDTO) {
        String result = sqlSession.selectOne("member.authCheck", emailMemberDTO);
        return result == null? false : true;
    }

    public String findIuid(EmailMemberDTO emailMemberDTO) {
        logger.info("findIuid IN DaoImpl");
        return sqlSession.selectOne("member.findIuid", emailMemberDTO);
    }

    public void delMemInfo(String session_mem_iuid) {
        logger.info("DaoImpl  |  delMemInfo 에 들어옴");
        sqlSession.delete("member.delMemInfo",session_mem_iuid);
        logger.info("나가라");
        logger.info("나가라 했다");
//        logger.info("n은 "+n);
    }

    public void sendKey(EmailMemberDTO emailMemberDTO) throws Exception {

        logger.info(": : : sendKey 1 :");
//        logger.info(": : : "+list.get(0).getEm_acheck()+"입니다.");

        // INSERT 아예 없을 경우
        if(emailMemberDTO.getEm_acheck() == null){
            logger.info("null임 if문 들어옴");
            String aes_iuid  = aes256Cipher.AES_Encode(UUID.randomUUID().toString());     // Memberinfo에 넣어줄 iuid. 나머지는 0으로 지정
            logger.info("aes_iuid는 "+aes_iuid);
            sqlSession.insert("member.tempIns", aes_iuid);                      // 임시로 memInfo 에 iuid, nick, profile, joindate 넣음
            emailMemberDTO.setEm_ref(aes_iuid);                                             // tbl_memInfo 의 iuid(PK)를 ref에 넣어줌
            logger.info(": : : sendKey 2 :");
            sqlSession.insert("member.sendKeyInsert", emailMemberDTO);
            return;
        }
        // 인증여부가 N 일때
        logger.info(": : : sendKey 3 :");
        sqlSession.update("member.sendKeyUpdate", emailMemberDTO);

        logger.info(": : : sendKey 4 :");
    }

    public String checkEmail(EmailMemberDTO emailMemberDTO) {
        String checkid = sqlSession.selectOne("member.checkid",emailMemberDTO);

        return checkid;
    }
    public String checkPwd(EmailMemberDTO emailMemberDTO){
        String checkpwd = sqlSession.selectOne("member.checkpass",emailMemberDTO);

        return checkpwd;
    }

    public MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO) {
        return sqlSession.selectOne("member.getMemInfo",emailMemberDTO);
    }

    public void addAMember(AuthMemberDTO authMemberDTO) {
        logger.info("인증멤버"+authMemberDTO.getAm_id());
        logger.info("인증멤버"+authMemberDTO.getAm_pwd());
        logger.info("인증멤버"+authMemberDTO.getAm_key());
        sqlSession.insert("member.addAMember", authMemberDTO);
    }
}
