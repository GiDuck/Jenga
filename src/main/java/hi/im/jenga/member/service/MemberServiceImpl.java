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

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
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


    public void addMemberInfo(SocialMemberDTO socialMemberDTO, EmailMemberDTO emailMemberDTO, MemberDTO memberDTO, String uploadName, String key) throws Exception {
        // String iuid = UUID.randomUUID().toString(); // iuid 생성
        // 이메일을 이용해서 조건에 넣을 iuid를 찾아야함
        // 암호화 iuid, 닉네임, 파일경로, level
        // 암호화된 iuid는 컨트롤러에서 넣음
        logger.info("addEMemberInfo 서비스");
        String iuid = "";

        memberDTO.setMem_nick(aes256Cipher.AES_Encode(memberDTO.getMem_nick()));
        memberDTO.setMem_introduce(aes256Cipher.AES_Encode(memberDTO.getMem_introduce()));

        if(uploadName.equals("")){
            logger.info("addEMemberInfo 서비스 디폴트이미지로 변경");
            memberDTO.setMem_profile("Y:\\go\\Jenga\\profiles\\jenga_profile_default.jpg");
        }else {
            logger.info("프로필사진 있고 uploadName은 " + uploadName);
            memberDTO.setMem_profile(uploadName);
        }

        memberDTO.setMem_profile(aes256Cipher.AES_Encode(memberDTO.getMem_profile()));
        if(key.equals("email")){
            logger.info("addEMemberInfo 이메일 입니다");
            iuid = findIuid(emailMemberDTO);   // 암호화 한 임시 meminfo uid를 찾아옴
            memberDTO.setMem_iuid(iuid);

            dao.addEMemberInfo(memberDTO);
        }else if(key.equals("social")){
            logger.info("addSMemberInfo 소셜 입니다");
            iuid = aes256Cipher.AES_Encode(UUID.randomUUID().toString());
            logger.info(iuid);
            memberDTO.setMem_iuid(iuid);

            dao.addSMemberInfo(memberDTO);
        }



    }

    public void addEMember(String aes_iuid) { dao.addEMember(aes_iuid); }

    public void addSMember(SocialMemberDTO socialMemberDTO, String sMem_iuid) {
        dao.addSMember(socialMemberDTO, sMem_iuid);
    }

    public MemberDTO isSMExist(String aes_sid) {
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

        String aes_find_pwd = aes256Cipher.AES_Encode(find_pwd);       // 암호화 후 찾아야 하니까

        result = dao.isEMExist(aes_find_pwd);    // 일단 존재하는지 여부

//        인증 N 이고 가입한 메일이 없을때
        if(result.equals("notexist")){
            return 0;
        }
//        ////////////
        String tempPwdKey = new TempKey().getKey(10, false);    // 인증키 생성
        logger.info(": 생성된 임시 키 " + tempPwdKey);
        // find_pwd의 비밀번호를 임시비밀번호로 바꿔야함
        // 바꾸고 메일보내기

        String sha_key = sha256Cipher.getEncSHA256(tempPwdKey);           // 키를 암호화 후 넣음
        dao.findEPwd(aes_find_pwd, sha_key);

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
        String Acheck = dao.checkAuth(emailMemberDTO);
        logger.info("||||||||||auth 체크"+Acheck);
        if(Acheck.equals("N")){
            return "noauth";
        }
        return "success";
    }

    public MemberDTO getMemInfo(EmailMemberDTO emailMemberDTO) {
        return dao.getMemInfo(emailMemberDTO);
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
            logger.info("새로 뽑아서 넣어야지 / 넣기전"+ key);
            logger.info("새로 뽑아서 넣어야지 / 후 "+ emailMemberDTO.getEm_akey());
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

    // 회원정보 수정
    // 세션에 있는 회원정보를 조건으로 출력  session memberDTO
    public MemberDTO modMemberInfoGET(MemberDTO memberDTO) throws Exception{
        // 복호화 한 후 비교 후 현재 세션에 있는 사용자의 정보를 받아옴
        logger.info(": : : ServiceImpl에 modMemberInfo 들어옴");
        logger.info("세션에 있는 iuid는 "+memberDTO.getMem_iuid());
        /*String  notAes_iuid = aes256Cipher.AES_Decode(memberDTO.getMem_iuid());
        logger.info("복호화한 있는 iuid는 "+notAes_iuid);*/

        memberDTO = dao.modMemberInfoGET(memberDTO.getMem_iuid());
        logger.info("DAO에서 받은 member dto.. " + memberDTO.toString());

        logger.info("암호화 결과... 닉네임 " + aes256Cipher.AES_Decode(memberDTO.getMem_nick()));
        logger.info("암호화 결과... 경로 " + aes256Cipher.AES_Decode(memberDTO.getMem_profile()));
        logger.info("암호화 결과... 소개 " + aes256Cipher.AES_Decode(memberDTO.getMem_introduce()));

        // 세션에 있는 사용자의 정보를 받아온 후 닉네임, 파일경로 복호화 후 memberDTO에 담음
        memberDTO.setMem_nick(aes256Cipher.AES_Decode(memberDTO.getMem_nick()));
        memberDTO.setMem_profile(aes256Cipher.AES_Decode(memberDTO.getMem_profile()));
        memberDTO.setMem_introduce(aes256Cipher.AES_Decode(memberDTO.getMem_introduce()));

        logger.info("ServiceImpl에 modMemberInfo    복호화 한 "+memberDTO.getMem_nick());
        logger.info("ServiceImpl에 modMemberInfo    복호화 한 "+memberDTO.getMem_profile());
        logger.info("ServiceImpl에 modMemberInfo    복호화 한 "+memberDTO.getMem_introduce());
        logger.info(": : : ServiceImpl에 modMemberInfo 나가자");

        return memberDTO;

    }

    public MemberDTO modMemberInfoPOST(String s_iuid, String mem_nick, String uploadName, String em_pwd, String[] favor) throws Exception {
        logger.info("MemberServiceImpl 1 "+s_iuid);
        logger.info("MemberServiceImpl 2 "+mem_nick);
        logger.info("MemberServiceImpl 3 "+uploadName);
        logger.info("MemberServiceImpl 4 "+em_pwd);
        for(String s:favor){
            logger.info("MemberServiceImpl 5 "+s);
        }
        // 공백으로 넘어오면 암호화안하고 daoImpl로 ""로 넘어감
        String aes_em_pwd = "";

        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setMem_nick(aes256Cipher.AES_Encode(mem_nick));       // 닉네임 암호화 후 DTO에 넣음


        if(uploadName.equals("")){
            logger.info("빈파일이면 뒤에 안뜸 "+uploadName);
            uploadName = dao.getMemProfile(s_iuid); // 세션id로 원래 파일이름 가져옴
            uploadName = aes256Cipher.AES_Decode(uploadName);   // 암호화 된채로 왔으니 복호화하고 밑에서 다시 암호화
        }
        memberDTO.setMem_profile(aes256Cipher.AES_Encode(uploadName));  // 파일이름 암호화 후 DTO에 넣음


        if(!em_pwd.equals("")) {
            logger.info("MemberServiceImpl 비밀번호 공백아니고 "+ em_pwd);
            aes_em_pwd = sha256Cipher.getEncSHA256(em_pwd);
        }

        return dao.modMemberInfoPOST(s_iuid, memberDTO, aes_em_pwd, favor);

    }


    public void addMemberFavor(String aes_iuid, String[] favor) {
        for(String fav : favor){
            dao.addMemberFavor(aes_iuid,fav);
        }
    }

    public List<String> getMemFavor(String member) { return dao.getMemFavor(member); }

    public MemberDTO testParam() {
        return dao.testParam();
    }

    public List<Map<String,String>> getCategory() {
        return dao.getCategory();
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
