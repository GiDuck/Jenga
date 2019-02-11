package hi.im.jenga.member.dao;


import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemberDAOImpl implements MemberDAO{

    private static final Logger logger = LoggerFactory.getLogger(MemberDAOImpl.class);
    @Autowired
    SqlSession sqlSession;
    @Autowired
    AES256Cipher aes256Cipher;

    public int addEMemberInfo(MemberDTO memberDTO) {
        /*HashMap<String,Object> map = new HashMap();
        map.put("memberDTO", memberDTO);
        map.put("uploadPath", uploadPath);*/

        return sqlSession.update("member.addEMemberInfo", memberDTO);
    }

    public void addSMemberInfo(MemberDTO memberDTO) {
        sqlSession.insert("member.addSMemberInfo", memberDTO);
    }

    public MemberDTO getUserInfo(String mem_iuid) { return sqlSession.selectOne("member.getUserInfo", mem_iuid); }

    public Map<String, String> getBmksUploadDate(String session_iuid) {
        Map<String, Date> map = sqlSession.selectOne("member.getBmksUploadDate", session_iuid);

        Map<String, String> map_string = new HashMap();

        if(map.get("chrome_timestamp") != null) {
            map_string.put("chrome_timestamp", String.valueOf(map.get("chrome_timestamp").getTime()));
        }
        if(map.get("explorer_timestamp") != null) {
            map_string.put("explorer_timestamp", String.valueOf(map.get("explorer_timestamp").getTime()));
        }

        return map_string;
    }

    public void changePwd(String mem_iuid, String aes_pwd) {
        Map<String, String> map = new HashMap();
        map.put("mem_iuid", mem_iuid);
        map.put("aes_pwd", aes_pwd);
        sqlSession.update("member.changePwd", map);
    }

    public void addEMember(String aes_iuid) { sqlSession.update("member.addEMember",aes_iuid); }

    public void addSMember(SocialMemberDTO socialMemberDTO, String iuid) {
        HashMap<String, Object> map = new HashMap();
        map.put("socialMemberDTO", socialMemberDTO);
        map.put("iuid", iuid);

        sqlSession.insert("member.addSMember", map);
    }

    public MemberDTO isSMExist(String aes_sid) {
        return sqlSession.selectOne("member.isSMExist", aes_sid);
    }

    public String isEMExist(String aes_eid) throws Exception {
        String result= sqlSession.selectOne("member.isEMExist", aes_eid);
        return result != null ? (result.equals("Y") ? "Y" : "N") : "notexist";
    }

    public void findEPwd(String aes_find_pwd, String sha_key) {
        logger.info(": : : findEPwd 들어옴 ");
        HashMap <String, String> map = new HashMap();
        map.put("aes_find_pwd", aes_find_pwd);
        map.put("sha_key", sha_key);

        int n = sqlSession.update("member.findEPwd",map);
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
        logger.info("한번 찾아봅니다"+sqlSession.selectOne("member.findIuid", emailMemberDTO));
        return sqlSession.selectOne("member.findIuid", emailMemberDTO);
    }

    public void delMemInfo(String session_mem_iuid) {
        logger.info("DaoImpl  |  delMemInfo 에 들어옴");
        sqlSession.delete("member.delMemInfo", session_mem_iuid);
        logger.info("나가라");
        logger.info("나가라 했다");
//        logger.info("n은 "+n);
    }

    public void updMemInfo(MemberDTO memberDTO) {
        sqlSession.update("member.updMemInfo", memberDTO);
    }

//    public MemberDTO modMemberInfo(String aes_iuid) { return sqlSession.selectOne("member.modMemberInfo", aes_iuid); }
    public void addMemberFavor(String aes_iuid, String fav) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("aes_iuid",aes_iuid);
        map.put("fav",fav);
        sqlSession.insert("member.addMemberFavor", map);
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
        logger.info("새로 뽑아서 넣어야지 / 후"+ emailMemberDTO.getEm_akey());
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

    public String checkAuth(EmailMemberDTO emailMemberDTO) {
        String checkAuth = sqlSession.selectOne("member.checkauth",emailMemberDTO);

        return checkAuth;
    }

    public List<String> getMemFavor(String member) { return sqlSession.selectList("member.getMemFavor",member); }

    public MemberDTO modMemberInfoGET(String aes_iuid) {  return sqlSession.selectOne("member.modMemberInfoGET", aes_iuid); }

    public MemberDTO modMemberInfoPOST(String s_iuid, MemberDTO memberDTO, String[] favor){
        Map<String, Object> map = new HashMap();

        map.put("memberDTO", memberDTO);
        map.put("s_iuid", s_iuid);

        sqlSession.update("member.modMemberInfoPOST_MemInfo", map);

       /* 비번 처리
       logger.info("비번 공백이면 뒤에 음따 "+aes_em_pwd+"음제");
        if(!aes_em_pwd.equals("")) {
            map.put("aes_em_pwd", aes_em_pwd);
            sqlSession.update("member.modMemberInfoPOST_EMember", map);
        }*/

//        sqlSession.delete("member.delMemberFavor", s_iuid);
        sqlSession.delete("member.delMemberFavor", s_iuid);
        logger.info("delete이다");
        for(String fav : favor) {
            map.put("fav",fav);
            try {

                sqlSession.insert("member.addMemberFavor", map);
                logger.info("태그넣는ㄷ이다");
            }catch (Exception e){
                // 무결성 제약 조건에 위배됩니다.
            }
        }

//        세션에 있는 아이디로 memInfo를 뽑아오기 위해
        return sqlSession.selectOne("member.getMemInfoSession",s_iuid);

    }

    // 프사를 그대로 할 시 다시 뽑아오기
    public String getMemProfile(String s_iuid) {
        return sqlSession.selectOne("member.getMemProfile",s_iuid);
    }

    public MemberDTO testParam() {
        return null;
    }



    /***카테고리 뽑는중***/
    public List<Map<String,String>> getCategory() {
        /*System.out.println(sqlSession.selectList("member.getCategory"));*/
        List<Map<String,String>> list  = sqlSession.selectList("member.getCategory");
       /* for(HashMap<String,String> map :  list){
            System.out.println(map.get("MCTG_NAME"));
            System.out.println(map.get("MCTG_IMG"));
        }*/
       return list;
    }



    public MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO) {
        return sqlSession.selectOne("member.getMemInfo",emailMemberDTO);
    }

    public int countFollowingMember(String session_iuid, String search) {
        Map<String, String> map = new HashMap<String, String>();
        logger.info("search는 "+ search);
        if("".equals(search)){
            logger.info("search가 널입니다");
        }

        map.put("search", search);
        map.put("session_iuid", session_iuid);
        int result = sqlSession.selectOne("member.countFollowingMember", map);
        logger.info("반환할 갯수 "+result);

        return result;
    }

    public List<BoardDTO> getFollowingMember(String session_iuid, String search, int startrow, int endrow) {
        Map<String, Object> map = new HashMap();
        map.put("session_iuid", session_iuid);
        map.put("search", search);
        map.put("startrow",startrow);
        map.put("endrow", endrow);
        return sqlSession.selectList("member.getFollowingMember", map);
    }

    public int countFollowerMember(String session_iuid, String search) {
        Map<String, String> map = new HashMap<String, String>();
        logger.info("search는 "+ search);
        if("".equals(search)){
            logger.info("search가 널입니다");
        }

        map.put("search", search);
        map.put("session_iuid", session_iuid);
        int result = sqlSession.selectOne("member.countFollowerMember", map);
        logger.info("반환할 갯수 "+result);

        return result;
    }

    public List<BoardDTO> getFollowerMember(String session_iuid, String search, int startrow, int endrow) {
        Map<String, Object> map = new HashMap();
        map.put("session_iuid", session_iuid);
        map.put("search", search);
        map.put("startrow", startrow);
        map.put("endrow", endrow);
        return sqlSession.selectList("member.getFollowerMember", map);
    }

    public List<Map<String, String>> getRecentBlock(String mem_iuid) {
        return sqlSession.selectList("member.getRecentBlock", mem_iuid);
    }

}
