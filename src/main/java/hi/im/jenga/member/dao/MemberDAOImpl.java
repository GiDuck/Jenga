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

    public List<EmailMemberDTO> isEMExist(String aes_eid) throws Exception {
    logger.info("오이잉 daoimpl "+aes_eid);
//        logger.info("11111 "+aes_eid);
//        aes_eid = "WrfT15MiBQqfHDhHLR5AcA==";
//        String aes_decode = aes256Cipher.AES_Decode(aes_eid);
//        logger.info("232323     "+aes_decode);
//        String result = sqlSession.selectList("member.isEMExist", aes_eid);
//        logger.info("2222 "+result);
        return sqlSession.selectList("member.isEMExist", aes_eid);
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

    public void sendKey(EmailMemberDTO emailMemberDTO, List<EmailMemberDTO> list) throws Exception {

        logger.info(": : : sendKey 1 :");
        logger.info(": : : "+list.get(0).getEm_acheck()+"입니다.");
        // 이메일은 있지만 인증여부가 N일 경우 INSERT
        if(list.get(0).getEm_acheck().equals("N")){
            String aes_iuid  = aes256Cipher.AES_Encode(UUID.randomUUID().toString());     // Memberinfo에 넣어줄 iuid. 나머지는 0으로 지정
            sqlSession.insert("member.tempIns", aes_iuid);
            emailMemberDTO.setEm_ref(aes_iuid);                             // tbl_memInfo 의 iuid(PK)를 ref에 넣어줌
            logger.info(": : : sendKey 2 :");
            sqlSession.insert("member.sendKeyInsert", emailMemberDTO);
            return;
        }
        logger.info(": : : sendKey 3 :");
        sqlSession.update("member.sendKeyUpdate", emailMemberDTO);

        logger.info(": : : sendKey 4 :");
    }

    public void addAMember(AuthMemberDTO authMemberDTO) {
        sqlSession.insert("member.addAMember", authMemberDTO);
    }
}
