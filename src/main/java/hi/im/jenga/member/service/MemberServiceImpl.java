package hi.im.jenga.member.service;

import hi.im.jenga.member.dao.MemberDAO;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.dto.AuthMemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.cipher.SHA256Cipher;
import hi.im.jenga.member.util.login.Util;
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
import java.util.List;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDAO dao;
    @Autowired
    private AES256Cipher aes256Cipher;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SHA256Cipher sha256Cipher;

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);


    public void addMemberInfo(MemberDTO memberDTO) throws Exception {
        // String iuid = UUID.randomUUID().toString(); // iuid 생성
        // 이메일을 이용해서 조건에 넣을 iuid를 찾아야함
        // 암호화 iuid, 닉네임, 파일경로, level
        // 암호화된 iuid는 컨트롤러에서 넣음
        memberDTO.setMem_nick(aes256Cipher.AES_Encode(memberDTO.getMem_nick()));
        memberDTO.setMem_profile(aes256Cipher.AES_Encode(memberDTO.getMem_profile()));

        dao.addMemberInfo(memberDTO);
    }

    public void addEMember(String aes_iuid) { dao.addEMember(aes_iuid);
    }

    public void addSMember(SocialMemberDTO socialMemberDTO, String iuid) {
        dao.addSMember(socialMemberDTO, iuid);
    }

    public boolean isSMExist(String aes_sid) {
        return dao.isSMExist(aes_sid);
    }

    // 암호화해서 넘김
    public String isEMExist(String em_id) throws Exception {
        String aes_eid = aes256Cipher.AES_Encode(em_id);
        logger.info("오이잉 service " + aes_eid);
        return dao.isEMExist(aes_eid);
    }

    // 인증키 생성 후 해당 회원의 비밀번호를 인증키로 바꾸고   이 회원이 이메일, 바뀐 인증키비번으로 로그인 후 자율적으로 정보수정가서 비밀번호 변경하게
    public int findEPwd(String find_pwd) throws Exception {
        String result;
        logger.info(": : : findEPwd");
        String tempPwdKey = new TempKey().getKey(10, false);    // 인증키 생성
        logger.info(": 생성된 임시 키 " + tempPwdKey);
        // find_pwd의 비밀번호를 임시비밀번호로 바꿔야함
        // 바꾸고 메일보내기

        String aes_find_pwd = aes256Cipher.AES_Encode(find_pwd);       // 암호화 후 찾아야 하니까

        result = dao.isEMExist(aes_find_pwd);    // 일단 존재하는지 여부

//        인증 N 이고 가입한 메일이 없을때
        if(result.equals("notexist")){
            return 0;
        }

        String aes_key = sha256Cipher.getEncSHA256(tempPwdKey);           // 키를 암호화 후 넣음
        dao.findEPwd(aes_find_pwd, aes_key);

        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject("Jenga 임시 비밀번호입니다. ");
        sendMail.setText(new StringBuffer().append("<h1>임시 비밀번호</h1><br><br>").append("<h2>" + tempPwdKey + "</h2>").append(" 입니다.").toString());
        sendMail.setFrom("imjengamaster@gmail.com", "젠가관리자");
        sendMail.setTo(find_pwd);
        sendMail.send();

        return 1;
    }

    public String checkEmail(EmailMemberDTO emailMemberDTO) throws Exception {
        emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));
        emailMemberDTO.setEm_pwd(sha256Cipher.getEncSHA256(emailMemberDTO.getEm_pwd()));
        System.out.println(emailMemberDTO.getEm_id());
        System.out.println(emailMemberDTO.getEm_pwd());
        String idcheck = dao.checkEmail(emailMemberDTO);
        if(idcheck == null){
            return "iderror";
        }
        String pwdcheck = dao.checkPwd(emailMemberDTO);
        if(pwdcheck == null){
            return "pwderror";
        }
        return "success";
    }

    public MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO) {
        return dao.getMemInfo(emailMemberDTO);
    }



    public void loginEMCheck(EmailMemberDTO emailMemberDTO) throws Exception {
        //암호화하기 id -> aes, pwd -> sha
        // emember
        boolean result;
        String aes_id = aes256Cipher.AES_Encode(emailMemberDTO.getEm_id());
        String aes_pwd = aes256Cipher.AES_Encode(emailMemberDTO.getEm_id());

      /*  result = dao.loginEMcheck(emailMemberDTO);

        if(!result){
            result = dao.loginAMcheck()
        }*/

    }

    public void join(EmailMemberDTO emailMemberDTO) {

   /*     aes256Cipher.AES_Encode(emailMemberDTO.getEm_id());
        aes256Cipher.AES_Encode(emailMemberDTO.getEm_pwd());

        dao.join(emailMemberDTO);*/
    }

    // iuid는 DAOImpl에서 넣음
    public String sendKey(EmailMemberDTO emailMemberDTO) throws Exception {

        String key = new TempKey().getKey(10, false);        // 이메일 인증키
        String emailId = emailMemberDTO.getEm_id();
        // 인증여부가 N이면 메일전송 / 이메일, 비밀번호하고 인증키 UPDATE 해야함
            emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));        // 암호화 한 후 UPDATE
            emailMemberDTO.setEm_pwd(sha256Cipher.getEncSHA256(emailMemberDTO.getEm_pwd()));    // 암호화 한 후 UPDATE
            emailMemberDTO.setEm_akey(key);                                                     // 생성한 인증키를 넣음
            dao.sendKey(emailMemberDTO);
            sendTempKey(emailId, key);   // 이메일 보낼때는 암호화 안한 이메일과 인증키를 넘김
            return "sendAuthKey";
            /*

            emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));        // 암호화 한 후 UPDATE
            emailMemberDTO.setEm_pwd(sha256Cipher.getEncSHA256(emailMemberDTO.getEm_pwd()));    // 암호화 한 후 UPDATE
            emailMemberDTO.setEm_akey(key);                                                     // 생성한 인증키를 넣음
            dao.sendKey(emailMemberDTO, list);                                                  // 이땐 list는 안씀
            return "sendAuthKey";*/
//        }
//
//        //  인증안했으니 입력한 아이디, 비번, 인증키 UPDATE/ 아이디가 없으니 아이디, 비번, 인증키 INSERT 해야함 / List는 Y/N을 보기위해
//
//        emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));        // 암호화 한 후 INSERT / UPDATE
//        emailMemberDTO.setEm_pwd(sha256Cipher.getEncSHA256(emailMemberDTO.getEm_pwd()));    // 암호화 한 후 INSERT / UPDATE
//        emailMemberDTO.setEm_akey(key);                                                     // 생성한 인증키를 넣음
//
//        dao.sendKey(emailMemberDTO, list);
//
//        sendTempKey(emailId, key);                                                   // 이메일 보낼때는 암호화 안한 이메일과 인증키를 넘김
//
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

    //     이메일 인증번호 보내는 메소드
    private void sendTempKey(String emailId, String key) throws MessagingException, UnsupportedEncodingException {
        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject("Jenga 인증 번호 입니다.");
        sendMail.setText(new StringBuffer().append("<h1>이메일 인증</h1><br><br>").append("키는 ").append("<h2><b>"+key+"</b></h2>").append(" 입니다").toString());
        sendMail.setFrom("jengamaster@gmail.com","젠가관리자");
        sendMail.setTo(emailId);      // 암호화 안한 이메일
        sendMail.send();
    }

}
