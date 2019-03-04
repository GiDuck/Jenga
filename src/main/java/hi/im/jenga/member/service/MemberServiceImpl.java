package hi.im.jenga.member.service;

import hi.im.jenga.member.dao.MemberDAO;

import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.util.status_code.AuthStatusCode;
import hi.im.jenga.member.util.LoginType;
import hi.im.jenga.member.util.UserInfoType;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.cipher.SHA256Cipher;
import hi.im.jenga.member.util.mail.MailHandler;
import hi.im.jenga.member.util.mail.TempKey;
import hi.im.jenga.util.email.EmailFormFactory;
import hi.im.jenga.util.email.EmailFormType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

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
    private EmailFormFactory emailFormFactory;

    @Value("#{email['gm.address']}")
    private String mailAddress;

    @Value("#{email['email.master']}")
    private String masterName;

    @Value("#{email['findpwd.subject']}")
    private String findPasswordEmailSubject;

    @Value("#{email['authmail.subject']}")
    private String authEmailSubject;

    public MemberServiceImpl(MemberDAO dao, AES256Cipher aes256Cipher, JavaMailSender mailSender, SHA256Cipher sha256Cipher, EmailFormFactory emailFormFactory) {
        this.dao = dao;
        this.aes256Cipher = aes256Cipher;
        this.mailSender = mailSender;
        this.sha256Cipher = sha256Cipher;
        this.emailFormFactory = emailFormFactory;
    }

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Override
    public void addMemberInfo(String authId, MemberDTO memberDTO, String uploadName, LoginType type) {

        try {
            memberDTO.setMem_nick(aes256Cipher.AES_Encode(memberDTO.getMem_nick()));
            memberDTO.setMem_introduce(aes256Cipher.AES_Encode(memberDTO.getMem_introduce()));
            memberDTO.setMem_profile(uploadName);
            memberDTO.setMem_profile(aes256Cipher.AES_Encode(memberDTO.getMem_profile()));

            if (LoginType.EMAIL == type) {
                memberDTO.setMem_iuid(findMemUidByEmail(authId));
                dao.addEmailMemInfo(memberDTO);
            } else if (LoginType.SOCIAL == type) {
                dao.addSocialMemInfo(memberDTO);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void addEmailMember(String encodedAesUid) {
        dao.addEMember(encodedAesUid);
    }
    @Override
    public void addSocialMember(SocialMemberDTO socialMemberDTO, String socialMemberUid) {
        dao.addSocialMember(socialMemberDTO, socialMemberUid);
    }
    @Override
    public MemberDTO getExistMember(String encodedAesUid) {
        return dao.getExistMember(encodedAesUid);
    }
    @Override
    public AuthStatusCode isEmailMemberExists(String emailMemberUid) {
        try {
            String encodedEmailUid = aes256Cipher.AES_Encode(emailMemberUid);
            String statusToken = dao.isEmailMemberExists(encodedEmailUid);

            if(statusToken == null) return AuthStatusCode.AUTH_NOT_EXIST;

            statusToken = statusToken.toUpperCase();
            int whetherRegInfo = dao.selectWhetherRegInfo(dao.findMemUidByEmail(encodedEmailUid));

            if ("Y".equals(statusToken)) {
                return AuthStatusCode.AUTH_EXIST;
            }else if("N".equals(statusToken) || whetherRegInfo > 0){
                return AuthStatusCode.AUTH_NOT_VALID;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return AuthStatusCode.LOGIN_ERROR;
    }

    @Override
    public AuthStatusCode findEmailPwd(String userEmail) {
        String encodedAesEmail;
        String tempPwd;
        try {
            encodedAesEmail = aes256Cipher.AES_Encode(userEmail);

            if (dao.isEmailMemberExists(encodedAesEmail) == null) {
                return AuthStatusCode.LOGIN_ERROR;
            }

            tempPwd = new TempKey().getKey(10, false);
            dao.findEmailPwd(encodedAesEmail, sha256Cipher.getEncSHA256(tempPwd));
            Map<String, Object> param = new HashMap<>();
            param.put("pwd", tempPwd);
            String htmlContent = emailFormFactory.publish(EmailFormType.TEMP_PASSWORD_EMAIL, param);
            sendEmail(findPasswordEmailSubject, userEmail, htmlContent);


        } catch (Exception e) {
            e.printStackTrace();
            return AuthStatusCode.LOGIN_ERROR;
        }

        return AuthStatusCode.LOGIN_SUCCESS;
    }



    @Override
    public AuthStatusCode loginCheck(EmailMemberDTO emailMemberDTO) {

        try {
            emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));
            emailMemberDTO.setEm_pwd(sha256Cipher.getEncSHA256(emailMemberDTO.getEm_pwd()));

            if (dao.checkEmail(emailMemberDTO.getEm_id()) == 0) return AuthStatusCode.MISMATCHED_ID;
            if (dao.checkPwd(emailMemberDTO) == 0) return AuthStatusCode.MISMATCHED_PWD;
            if (dao.selectWhetherRegInfo(dao.findMemUidByEmail(emailMemberDTO.getEm_id())) > 0) return AuthStatusCode.REG_ALREADY_EXISTS;
            if ("N".equals(dao.getAuthToken(emailMemberDTO).toUpperCase())) return AuthStatusCode.AUTH_NOT_VALID;
            return AuthStatusCode.LOGIN_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return AuthStatusCode.LOGIN_FAIL;

    }

    public MemberDTO getUserInfo(String userUid) {
        return dao.getUserInfo(userUid);
    }


    @Override
    @Transactional
    public AuthStatusCode sendKey(EmailMemberDTO emailMemberDTO) {

        try {
            String key = new TempKey().getKey(10, false);
            String emailId = emailMemberDTO.getEm_id();

            emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));
            emailMemberDTO.setEm_pwd(sha256Cipher.getEncSHA256(emailMemberDTO.getEm_pwd()));
            emailMemberDTO.setEm_ref(aes256Cipher.AES_Encode(UUID.randomUUID().toString()));

            emailMemberDTO.setEm_akey(key);

            dao.sendKey(emailMemberDTO);

            Map<String, Object> param = new HashMap<>();
            param.put("pwd", key);

            String htmlContent = emailFormFactory.publish(EmailFormType.AUTH_EMAIL, param);
            sendEmail(authEmailSubject, emailId, htmlContent);

            return AuthStatusCode.AUTH_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return AuthStatusCode.AUTH_FAIL;
    }

    @Override
    public AuthStatusCode authCheck(EmailMemberDTO emailMemberDTO) {

        try {
            emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));
            if (dao.authCheck(emailMemberDTO) == 1) {
                dao.insertWhetherRegInfo(dao.findMemUidByEmail(emailMemberDTO.getEm_id()));
                return AuthStatusCode.AUTH_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AuthStatusCode.AUTH_FAIL;
    }
    @Override
    public String findMemUidByEmail(String userEmail) {
        String userUid = null;
        try {
            userUid = dao.findMemUidByEmail(aes256Cipher.AES_Encode(userEmail));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userUid;


    }
    @Override
    public void delMemInfo(String memUid) {
        dao.delMemInfo(memUid);
    }

    @Override
    public MemberDTO modMemberInfoGET(MemberDTO memberDTO) {

        try {
            memberDTO = dao.modMemberInfoGET(memberDTO.getMem_iuid());
            String nick = memberDTO.getMem_nick();
            String profile = memberDTO.getMem_profile();
            String introduce = memberDTO.getMem_introduce();

            memberDTO.setMem_nick(aes256Cipher.AES_Decode(nick));
            memberDTO.setMem_introduce(aes256Cipher.AES_Decode(introduce));
            memberDTO.setMem_profile(aes256Cipher.AES_Decode(profile));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return memberDTO;

    }
    @Override
    public MemberDTO modMemberInfoPOST(String memUid, String memNick, String mem_introduce, String uploadFilePath, String[] favor) throws Exception {

        MemberDTO memberDTO = new MemberDTO();

            memberDTO.setMem_nick(aes256Cipher.AES_Encode(memNick));
            memberDTO.setMem_introduce(aes256Cipher.AES_Encode(mem_introduce));

            if (uploadFilePath == null) {
                uploadFilePath = dao.getMemProfile(memUid);
                uploadFilePath = aes256Cipher.AES_Decode(uploadFilePath);
            }
            memberDTO.setMem_profile(aes256Cipher.AES_Encode(uploadFilePath));
            return dao.modMemberInfoPOST(memUid, memberDTO, favor);

    }

    @Override
    public void addMemberFavor(String encodedAesUid, String[] favor) {
        for (String fav : favor) {
            dao.addMemberFavor(encodedAesUid, fav);
        }
    }
    @Override
    public List<String> getMemFavor(String member) {
        return dao.getMemFavor(member);
    }
    @Override
    public List<Map<String, String>> getCategory() {
        return dao.getCategory();
    }


    @Override
    public Map<String, String> getUserInfo(String memUid, List<UserInfoType> types) {
        Map<String, String> map = new HashMap<>();
        checkUserInfoParam(dao.getUserInfo(memUid), map, types);
        return map;
    }
    public void checkUserInfoParam(MemberDTO memberDTO, Map<String, String> map, List<UserInfoType> types) {
        try {

            for (UserInfoType type : types) {

                if (type == UserInfoType.PROFILE) {
                    map.put("profile", aes256Cipher.AES_Decode(memberDTO.getMem_profile()));
                } else if (type == UserInfoType.NICK) {
                    map.put("nick", aes256Cipher.AES_Decode(memberDTO.getMem_nick()));
                } else if (type == UserInfoType.INTRODUCE) {
                    map.put("introduce", aes256Cipher.AES_Decode(memberDTO.getMem_introduce()));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public String getBookmarkUploadDate(String memUid) {
        return dao.getBookmarkUploadDate(memUid);
    }
    @Override
    public AuthStatusCode changePwd(String memUid, String pwd) {
        try {
             int count = dao.changePwd(memUid, sha256Cipher.getEncSHA256(pwd));
             if(count == 1)  {
                 return AuthStatusCode.MOD_SUCCESS;
             }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return AuthStatusCode.MOD_FAIL;

    }


    @Override
    public int deleteWhetherRegInfo(String memUid) {
        return dao.deleteWhetherRegInfo(memUid);
    }

    private void sendEmail(String subject, String userEmail, String htmlContent) {

        try {
            MailHandler sendMail = new MailHandler(mailSender);
            sendMail.setSubject(subject);
            sendMail.setText(htmlContent);
            sendMail.setFrom(mailAddress, masterName);
            sendMail.setTo(userEmail);
            sendMail.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
