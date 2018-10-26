package hi.im.jenga.member.service;

import hi.im.jenga.member.dao.MemberDAO;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.dto.AuthMemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.cipher.SHA256Cipher;
import hi.im.jenga.member.util.mail.MailHandler;
import hi.im.jenga.member.util.mail.TempKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

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


    public int addMemberInfo(MemberDTO memberDTO) { return dao.addMemberInfo(memberDTO); }

    public void addEMember(EmailMemberDTO emailMemberDTO, String iuid) { dao.addEMember(emailMemberDTO, iuid); }

    public void addSMember(SocialMemberDTO socialMemberDTO, String iuid) { dao.addSMember(socialMemberDTO, iuid); }

    public void addAMember(AuthMemberDTO authMemberDTO) throws Exception {

        String key = new TempKey().getKey(10,false);    // 인증키 생성
        logger.info(": : : MemberServiceImpl  생성된 인증키 : "+key);


        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject("Jenga 인증 번호 입니다.");
        sendMail.setText(new StringBuffer().append("<h1>메일 인증</h1><br><br>").append("키는 ").append("<h2><b>"+key+"</b></h2>").append(" 입니다").toString());
        sendMail.setFrom("jengamaster@gmail.com","젠가관리자");
        sendMail.setTo(authMemberDTO.getAm_id());
        sendMail.send();
        // 이메일, 비번(SHA256), 키 암호화해서 authMemberDTO에 넣음
        authMemberDTO.setAm_id(aes256Cipher.AES_Encode(authMemberDTO.getAm_id()));
        authMemberDTO.setAm_pwd(sha256Cipher.getEncSHA256(authMemberDTO.getAm_pwd()));
        authMemberDTO.setAm_key(key);


        dao.addAMember(authMemberDTO);

    }

    public boolean isSMExist(String aes_sid) { return dao.isSMExist(aes_sid); }

    public boolean isEMExist(String aes_eid) { return dao.isEMExist(aes_eid); }

    public boolean isAMExist(String aes_eid) { return dao.isAMExist(aes_eid); }

    // 인증키 생성 후 해당 회원의 비밀번호를 인증키로 바꾸고   이 회원이 이메일, 바뀐 인증키비번으로 로그인 후 자율적으로 정보수정가서 비밀번호 변경하게
    public void findEPwd(String find_pwd) throws Exception {
        logger.info(": : : findEPwd");
        String tempPwdKey = new TempKey().getKey(10,false);    // 인증키 생성
        logger.info(": 생성된 임시 키 "+tempPwdKey);
        // find_pwd의 비밀번호를 임시비밀번호로 바꿔야함
        // 바꾸고 메일보내기

        find_pwd = aes256Cipher.AES_Encode(find_pwd);       // 암호화 후 찾아야 하니까
        dao.findEPwd(find_pwd, tempPwdKey);

        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject("Jenga 임시 비밀번호입니다. ");
        sendMail.setText(new StringBuffer().append("<h1>임시 비밀번호</h1><br><br>").append("<h2>"+tempPwdKey+"</h2>").append(" 입니다.").toString());
        sendMail.setFrom("jengamaster@gmail.com", "젠가관리자");
        sendMail.setTo(find_pwd);
        sendMail.send();


    }

    public void findAPwd(String find_pwd) throws Exception{
        logger.info(": : : findAPwd");
        String tempPwdKey = new TempKey().getKey(10,false);  // 인증키 생성
        String find_pwd_original = find_pwd;
        logger.info(find_pwd);
        logger.info(": 생성된 임시 키 "+tempPwdKey);
        // find_pwd의 비밀번호를 임시비밀번호로 바꿔야함
        // 바꾸고 메일보내기

        find_pwd= aes256Cipher.AES_Encode(find_pwd);       // 암호화 후 찾아야 하니까
        logger.info(find_pwd);
        dao.findAPwd(find_pwd, tempPwdKey);

        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject("Jenga 임시 비밀번호입니다. ");
        sendMail.setText(new StringBuffer().append("<h1>임시 비밀번호</h1><br><br>").append("<h2>"+tempPwdKey+"</h2>").append(" 입니다.").toString());
        sendMail.setFrom("jengamaster@gmail.com", "젠가관리자");
        sendMail.setTo(find_pwd_original);
        sendMail.send();


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

    public String getToken(EmailMemberDTO emailMemberDTO) {
        return dao.getToken(emailMemberDTO);
    }


}
