package hi.im.jenga.member.dao;


import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

@Repository
public class MemberDAOImpl implements MemberDAO{

    private static final Logger logger = LoggerFactory.getLogger(MemberDAOImpl.class);
    private SqlSession sqlSession;
    private AES256Cipher aes256Cipher;

    @Autowired
    public MemberDAOImpl(SqlSession sqlSession, AES256Cipher aes256Cipher) {
        this.sqlSession = sqlSession;
        this.aes256Cipher = aes256Cipher;
    }
    @Override
    public int addEmailMemInfo(MemberDTO memberDTO) {
        return sqlSession.update("member.addEmailMemInfo", memberDTO);
    }
    @Override
    public void addSocialMemInfo(MemberDTO memberDTO) {
        sqlSession.insert("member.addSocialMemInfo", memberDTO);
    }
    @Override
    public String getBookmarkUploadDate(String memUid) {
        Date uploadedDate = sqlSession.selectOne("member.getBookmarkUploadDate", memUid);
        if(uploadedDate != null){
            return String.valueOf(uploadedDate.getTime());
        }
        return null;
    }
    @Override
    @Transactional
    public int changePwd(String memUid, String encodedAesPwd) {
        Map<String, String> map = new HashMap();
        map.put("memUid", memUid);
        map.put("memPwd", encodedAesPwd);

        System.out.println(map);
       return sqlSession.update("member.changePwd", map);
    }
    @Override
    public void addEMember(String encodedAesUid) { sqlSession.update("member.addEmailMember", encodedAesUid); }
    @Override
    public void addSocialMember(SocialMemberDTO socialMemberDTO, String memberUid) {
        HashMap<String, Object> map = new HashMap();
        map.put("socialMemberDTO", socialMemberDTO);
        map.put("memberUid", memberUid);

        sqlSession.insert("member.addSocialMember", map);
    }
    @Override
    public MemberDTO getExistMember(String memUid) {
        return sqlSession.selectOne("member.getExistMember", memUid);
    }
    @Override
    public String isEmailMemberExists(String encodedAesUid) {
        return sqlSession.selectOne("member.isEMExist", encodedAesUid);
    }
    @Override
    public void findEmailPwd(String encodedAesPwd, String shaKey) throws SQLException {
        Map <String, String> map = new HashMap<>();
        map.put("aes_find_pwd", encodedAesPwd);
        map.put("sha_key", shaKey);
        int updatedRow = sqlSession.update("member.findEPwd",map);
        if(updatedRow > 1){
            throw new SQLException();
        }
    }
    @Override
    public void tempIns(String memUid) {

        sqlSession.insert("member.tempIns", memUid);
    }
    @Override
    public int authCheck(EmailMemberDTO emailMemberDTO) {
        return sqlSession.update("member.authTokenUpdate", emailMemberDTO);

    }
    @Override
    public String findMemUidByEmail(String email) {
        return sqlSession.selectOne("member.findMemUidByEmail", email);
    }
    @Override
    public void delMemInfo(String memUid) {
        sqlSession.delete("member.delMemInfo", memUid);

    }
    @Override
    public void addMemberFavor(String encodedAesUid, String favor) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("memUid", encodedAesUid);
        map.put("favor", favor);
        sqlSession.insert("member.addMemberFavor", map);
    }

    @Transactional
    public void sendKey(EmailMemberDTO emailMemberDTO) {

           sqlSession.insert("member.setTempMemInfo", emailMemberDTO);
           sqlSession.insert("member.sendKey", emailMemberDTO);

    }

    @Override
    public int checkEmail(String userUid) {
        return sqlSession.selectOne("member.checkid", userUid);
    }
    @Override
    public int checkPwd(EmailMemberDTO emailMemberDTO){
        return sqlSession.selectOne("member.checkpass",emailMemberDTO);
    }
    @Override
    public String getAuthToken(EmailMemberDTO emailMemberDTO) {

        return sqlSession.selectOne("member.checkauth",emailMemberDTO);
    }
    @Override
    public List<String> getMemFavor(String memberUid) { return sqlSession.selectList("member.getMemFavor", memberUid); }
    @Override
    public MemberDTO modMemberInfoGET(String encodedAesUid) {  return sqlSession.selectOne("member.modMemberInfoGET", encodedAesUid); }

    @Transactional
    public MemberDTO modMemberInfoPOST(String memUid, MemberDTO memberDTO, String[] favor){
        Map<String, Object> map = new HashMap();
        memberDTO.setMem_iuid(memUid);
        map.put("memberDTO", memberDTO);
        map.put("memUid", memUid);

        sqlSession.update("member.modMemberInfoPOST_MemInfo", map);
        sqlSession.delete("member.delMemberFavor", memUid);

        for(String item : favor) {
            Map<String, Object> param = new HashMap<>();
            param.put("memUid", memUid);
            param.put("favor",item);
                sqlSession.insert("member.addMemberFavor", param);
        }

        return sqlSession.selectOne("member.getMemInfoSession", memUid);

    }

    @Override
    public String getMemProfile(String memUid) {
        return sqlSession.selectOne("member.getMemProfile", memUid);
    }

    @Override
    public List<Map<String,String>> getCategory() {
       return sqlSession.selectList("member.getCategory");
    }

    @Override
    public MemberDTO getUserInfo(String userUid) {
        return sqlSession.selectOne("member.getUserInfo", userUid);
    }

    @Override
    public void insertWhetherRegInfo(String memUid) {

        sqlSession.insert("member.insertWhetherRegInfo", memUid);

    }

    @Override
    public int deleteWhetherRegInfo(String memUid) {
        return sqlSession.delete("member.deleteWhetherRegInfo", memUid);
    }
    @Override
    public int selectWhetherRegInfo(String memUid){

        return sqlSession.selectOne("member.getWhetherRegInfo", memUid);
    }
}
