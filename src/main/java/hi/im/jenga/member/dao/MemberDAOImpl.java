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
//        logger.info("11111 "+aes_eid);
//        aes_eid = "WrfT15MiBQqfHDhHLR5AcA==";
//        String aes_decode = aes256Cipher.AES_Decode(aes_eid);
//        logger.info("232323     "+aes_decode);
//        String result = sqlSession.selectList("member.isEMExist", aes_eid);
//        logger.info("2222 "+result);
        String result = sqlSession.selectOne("member.isEMExist", aes_eid);
        /*if(result != null){
            if(result.equals("Y")){

            }else if(result.equals("N")){

            }

        }else if(result == null)*/
        logger.info("빠져나와라 "+result);
        return result != null ? (result == "Y" ? "Y" : "N") : "notexist";
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
        logger.info("findIuid IN DaiImpl");
        return sqlSession.selectOne("member.findIuid", emailMemberDTO);
    }

    public void sendKey(EmailMemberDTO emailMemberDTO) throws Exception {

        logger.info(": : : sendKey 1 :");
//        logger.info(": : : "+list.get(0).getEm_acheck()+"입니다.");
        // INSERT 아예 없을 경우
        if(emailMemberDTO.getEm_acheck() == null){
            String aes_iuid  = aes256Cipher.AES_Encode(UUID.randomUUID().toString());     // Memberinfo에 넣어줄 iuid. 나머지는 0으로 지정
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

    public void addAMember(AuthMemberDTO authMemberDTO) {
        sqlSession.insert("member.addAMember", authMemberDTO);
    }
}
