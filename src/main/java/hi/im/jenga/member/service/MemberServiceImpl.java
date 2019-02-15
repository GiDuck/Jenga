package hi.im.jenga.member.service;

import hi.im.jenga.member.dao.MemberDAO;

import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.cipher.SHA256Cipher;
import hi.im.jenga.member.util.mail.MailHandler;
import hi.im.jenga.member.util.mail.TempKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberDAO dao;
    private AES256Cipher aes256Cipher;
    private JavaMailSender mailSender;
    private SHA256Cipher sha256Cipher;

    @Autowired
    public MemberServiceImpl(MemberDAO dao, AES256Cipher aes256Cipher, JavaMailSender mailSender, SHA256Cipher sha256Cipher) {
        this.dao = dao;
        this.aes256Cipher = aes256Cipher;
        this.mailSender = mailSender;
        this.sha256Cipher = sha256Cipher;
    }

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);


    public void addMemberInfo(SocialMemberDTO socialMemberDTO, EmailMemberDTO emailMemberDTO, MemberDTO memberDTO, String uploadName, String key) throws Exception {
        // String iuid = UUID.randomUUID().toString(); // iuid 생성
        // 이메일을 이용해서 조건에 넣을 iuid를 찾아야함
        // 암호화 iuid, 닉네임, 파일경로, level
        // 암호화된 iuid는 컨트롤러에서 넣음
        String iuid = "";

        memberDTO.setMem_nick(aes256Cipher.AES_Encode(memberDTO.getMem_nick()));
        memberDTO.setMem_introduce(aes256Cipher.AES_Encode(memberDTO.getMem_introduce()));

        if (uploadName.equals("")) {
            memberDTO.setMem_profile("D:\\jengaResource\\img\\profiles\\jenga_profile_default.jpg");
        } else {
            memberDTO.setMem_profile(uploadName);
        }

        memberDTO.setMem_profile(aes256Cipher.AES_Encode(memberDTO.getMem_profile()));
        if (key.equals("email")) {
            logger.info("addEMemberInfo 이메일 입니다");
            iuid = findIuid(emailMemberDTO);   // 암호화 한 임시 meminfo uid를 찾아옴
            memberDTO.setMem_iuid(iuid);

            dao.addEMemberInfo(memberDTO);
        } else if (key.equals("social")) {
            logger.info("addSMemberInfo 소셜 입니다");
            iuid = aes256Cipher.AES_Encode(UUID.randomUUID().toString());
            logger.info(iuid);
            memberDTO.setMem_iuid(iuid);

            dao.addSMemberInfo(memberDTO);
        }


    }

    public void addEMember(String aes_iuid) {
        dao.addEMember(aes_iuid);
    }

    public void addSMember(SocialMemberDTO socialMemberDTO, String sMem_iuid) {
        dao.addSMember(socialMemberDTO, sMem_iuid);
    }

    public MemberDTO getExistMember(String aes_sid) {
        return dao.getExistMember(aes_sid);
    }

    public String isEMExist(String em_id) throws Exception {
        String aes_eid = aes256Cipher.AES_Encode(em_id);
        logger.info("오이잉 service " + aes_eid);
        return dao.isEMExist(aes_eid);
    }

    public int findEPwd(String userEmail) {
        logger.info(": : : findEPwd");
        String encryptedUserEmail;
        String tempPwd;
        try {
            encryptedUserEmail = aes256Cipher.AES_Encode(userEmail);
            if ("notexist".equals(dao.isEMExist(encryptedUserEmail))) {
                return 0;
            }
            tempPwd = new TempKey().getKey(10, false);
            dao.findEPwd(encryptedUserEmail, sha256Cipher.getEncSHA256(tempPwd));


            MailHandler sendMail = new MailHandler(mailSender);
            sendMail.setSubject("Jenga 임시 비밀번호입니다. ");
            sendMail.setText(new StringBuffer().append("<h1>임시 비밀번호</h1><br><br>").append("<h2>" + tempPwd + "</h2>").append(" 입니다.").toString());
            sendMail.setFrom("imjengamaster@gmail.com", "젠가관리자");
            sendMail.setTo(userEmail);
            sendMail.send();


        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }

    public String checkEmail(EmailMemberDTO emailMemberDTO) {

        try {
            emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));
            emailMemberDTO.setEm_pwd(sha256Cipher.getEncSHA256(emailMemberDTO.getEm_pwd()));

            logger.info("암호화 된 ID PW..  " + emailMemberDTO.getEm_id() + " " + emailMemberDTO.getEm_pwd());
            if (dao.checkEmail(emailMemberDTO) == null) return "iderror";
            if (dao.checkPwd(emailMemberDTO) == null) return "pwderror";
            if (dao.checkAuth(emailMemberDTO).equals("N")) return "noauth";

            return "success";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO) {
        return dao.getMemInfo(emailMemberDTO);
    }

    // iuid는 DAOImpl에서 넣음
    public String sendKey(EmailMemberDTO emailMemberDTO) throws Exception {

        String key = new TempKey().getKey(10, false);        // 이메일 인증키
        String emailId = emailMemberDTO.getEm_id();
        // 인증여부가 N이면 메일전송 / 이메일, 비밀번호하고 인증키 UPDATE 해야함
        emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));        // 암호화 한 후 UPDATE
        emailMemberDTO.setEm_pwd(sha256Cipher.getEncSHA256(emailMemberDTO.getEm_pwd()));    // 암호화 한 후 UPDATE
        emailMemberDTO.setEm_akey(key);                                                     // 생성한 인증키를 넣음
        logger.info("새로 뽑아서 넣어야지 / 넣기전" + key);
        logger.info("새로 뽑아서 넣어야지 / 후 " + emailMemberDTO.getEm_akey());
        dao.sendKey(emailMemberDTO);
        sendTempKey(emailId, key);   // 이메일 보낼때는 암호화 안한 이메일과 인증키를 넘김
        return "sendAuthKey";
    }

    public boolean authCheck(EmailMemberDTO emailMemberDTO) throws Exception {

        emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));
        return dao.authCheck(emailMemberDTO);
    }

    public String findIuid(EmailMemberDTO emailMemberDTO) throws Exception {
        logger.info("findIuid IN ServiceImpl");
        //        이메일을 암호화시켜서 비교하기 위해
        String aes_id = aes256Cipher.AES_Encode(emailMemberDTO.getEm_id());
        emailMemberDTO.setEm_id(aes_id);
        return dao.findIuid(emailMemberDTO);
    }

    public void delMemInfo(String session_mem_iuid) {
        dao.delMemInfo(session_mem_iuid);
    }

    public void updMemInfo(MemberDTO memberDTO) {
        memberDTO.getMem_nick();
        memberDTO.getMem_profile();
        dao.updMemInfo(memberDTO);
    }


    public MemberDTO modMemberInfoGET(MemberDTO memberDTO) throws Exception {


        memberDTO = dao.modMemberInfoGET(memberDTO.getMem_iuid());
        memberDTO.setMem_nick(aes256Cipher.AES_Decode(memberDTO.getMem_nick()));
        memberDTO.setMem_profile(aes256Cipher.AES_Decode(memberDTO.getMem_profile()));
        memberDTO.setMem_introduce(aes256Cipher.AES_Decode(memberDTO.getMem_introduce()));

        return memberDTO;

    }

    public MemberDTO modMemberInfoPOST(String s_iuid, String mem_nick, String mem_introduce, String uploadName, String[] favor) throws Exception {

        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setMem_nick(aes256Cipher.AES_Encode(mem_nick));
        memberDTO.setMem_introduce(aes256Cipher.AES_Encode(mem_introduce));


        if (uploadName.equals("")) {
            uploadName = dao.getMemProfile(s_iuid);
            uploadName = aes256Cipher.AES_Decode(uploadName);
        }
        memberDTO.setMem_profile(aes256Cipher.AES_Encode(uploadName));

        return dao.modMemberInfoPOST(s_iuid, memberDTO, favor);

    }


    public void addMemberFavor(String aes_iuid, String[] favor) {
        logger.info("addMemberFavor iuid는 " + aes_iuid);
        for (String fav : favor) {
            dao.addMemberFavor(aes_iuid, fav);
        }
    }

    public List<String> getMemFavor(String member) {
        return dao.getMemFavor(member);
    }

    public MemberDTO testParam() {
        return dao.testParam();
    }

    public List<Map<String, String>> getCategory() {
        return dao.getCategory();
    }

    public Map<String, String> getUserInfo(String mem_iuid, String check_profile, String check_nick, String check_introduce) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Map<String, String> map = new HashMap();
        MemberDTO memberDTO = dao.getUserInfo(mem_iuid);
        if (check_profile.equals("profile")) {
            map.put("profile", aes256Cipher.AES_Decode(memberDTO.getMem_profile()));
        }
        if (check_nick.equals("nick")) {
            map.put("nick", aes256Cipher.AES_Decode(memberDTO.getMem_nick()));
        }
        if (check_introduce.equals("introduce")) {
            map.put("introduce", aes256Cipher.AES_Decode(memberDTO.getMem_introduce()));
        }
        return map;
    }

    public String getBmksUploadDate(String session_iuid) {
        return dao.getBmksUploadDate(session_iuid);
    }

    public void changePwd(String mem_iuid, String pwd) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        dao.changePwd(mem_iuid, aes256Cipher.AES_Encode(pwd));

    }


    //     이메일 인증번호 보내는 메소드
    private void sendTempKey(String emailId, String key) throws MessagingException, UnsupportedEncodingException {
        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject("Jenga 인증 번호 입니다.");
        sendMail.setText(new StringBuffer().append("<h1>이메일 인증</h1><br><br>").append("키는 ").append("<h2><b>" + key + "</b></h2>").append(" 입니다").toString());
        sendMail.setFrom("jengamaster@gmail.com", "젠가관리자");
        sendMail.setTo(emailId);      // 암호화 안한 이메일
        sendMail.send();
    }

}
