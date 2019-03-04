package hi.im.jenga.member.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import hi.im.jenga.board.util.FileIOUtil;
import hi.im.jenga.util.AuthUser;
import hi.im.jenga.util.FileType;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.service.MemberService;
import hi.im.jenga.util.status_code.AuthStatusCode;
import hi.im.jenga.member.util.LoginType;
import hi.im.jenga.member.util.UserInfoType;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.login.*;
import hi.im.jenga.util.session.MemberSession;
import hi.im.jenga.util.session.SessionValidate;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private NaverLoginUtil naverLoginUtil;
    private GoogleLoginUtil googleLoginUtil;
    private FacebookLoginUtil facebookLoginUtil;
    private KakaoLoginUtil kakaoLoginUtil;
    private MemberService memberService;
    private AES256Cipher aes256Cipher;
    private LoginUtil[] utils;


    @Autowired
    public MemberController(NaverLoginUtil naverLoginUtil, GoogleLoginUtil googleLoginUtil, FacebookLoginUtil facebookLoginUtil,
                            KakaoLoginUtil kakaoLoginUtil, MemberService memberService, AES256Cipher aes256Cipher) {
        this.naverLoginUtil = naverLoginUtil;
        this.googleLoginUtil = googleLoginUtil;
        this.facebookLoginUtil = facebookLoginUtil;
        this.kakaoLoginUtil = kakaoLoginUtil;
        this.memberService = memberService;
        this.aes256Cipher = aes256Cipher;
        initLoginUtilArr();

    }

    @Value("#{data['image.profile_path']}")
    private String profile_absolute_path;

    @Value("#{data['image.root_path']}")
    private String image_root_path;



    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome(@AuthUser MemberDTO authUser) {

        System.out.println("member...");
        if(authUser!=null)
        System.out.println(authUser.toString());

        return "main/main";
    }


    @RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
    @ResponseBody
    public int loginCheck(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        EmailMemberDTO emailMemberDTO = new EmailMemberDTO();
        emailMemberDTO.setEm_id(email);
        emailMemberDTO.setEm_pwd(password);
        AuthStatusCode status = memberService.loginCheck(emailMemberDTO);
        if(status == AuthStatusCode.LOGIN_SUCCESS) {
            String userUid = memberService.findMemUidByEmail(email);
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMem_iuid(userUid);
            MemberSession.setMemberSession(session , memberDTO);
        }
        return status.getCode();

    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session, Model model) {
        for (int i = 0; i < utils.length; ++i) {
            model.addAttribute(extractClassHead(utils[i]), utils[i].getAuthorizationUrl(session));
        }
        return "member/login";
    }


    @RequestMapping(value = "/findPwd", method = RequestMethod.POST)
    public @ResponseBody
    String findPwdPOST(@RequestParam String find_pwd) {
        String check = "success";
        AuthStatusCode findPwdCheck = memberService.findEmailPwd(find_pwd);

        if (findPwdCheck == AuthStatusCode.LOGIN_ERROR) {
            check = "error";
        }

        return check;
    }


    @RequestMapping(value = "/authCheck", method = RequestMethod.POST)
    @ResponseBody
    public int authCheck(@RequestParam("email") String email, @RequestParam("password") String pwd){
        AuthStatusCode result= memberService.isEmailMemberExists(email);
        try {
                EmailMemberDTO emailMemberDTO = new EmailMemberDTO();
                emailMemberDTO.setEm_id(email);
                emailMemberDTO.setEm_pwd(pwd);

                    if (result == AuthStatusCode.AUTH_NOT_EXIST || result == AuthStatusCode.AUTH_NOT_VALID) {
                        return memberService.sendKey(emailMemberDTO).getCode();
                    }

            return result.getCode();

        }catch(Exception e){
            e.printStackTrace();
        }
        return AuthStatusCode.AUTH_FAIL.getCode();
    }



    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public @ResponseBody
    int joinPOST(@RequestParam("email") String email, @RequestParam("authKey") String authKey) {
        EmailMemberDTO emailMemberDTO = new EmailMemberDTO();
        emailMemberDTO.setEm_id(email);
        emailMemberDTO.setEm_akey(authKey);
        AuthStatusCode status = memberService.authCheck(emailMemberDTO);
        return status.getCode();

    }


    @RequestMapping(value = "/modMemInfo", method = RequestMethod.GET)
    public String modMemberInfoGET(HttpSession session, Model model) {


       MemberDTO memberDTO =(MemberDTO)SessionValidate.getValidSessionObj(session, MemberSession.MEMBER_SESSION_KEY);
       String userUid = memberDTO.getMem_iuid();
       memberDTO = memberService.modMemberInfoGET(memberDTO);
       memberDTO.setMem_iuid(userUid);
        model.addAttribute("DTO", memberDTO);
        model.addAttribute("favor", memberService.getMemFavor(userUid));

        return "member/modMemInfo";
    }


    @RequestMapping(value = "/modMemInfo", method = RequestMethod.POST)
    @ResponseBody
    public int modMemberInfoPOST(@RequestParam("mem_nick") String mem_nick, @RequestPart(value = "mem_profile", required = false) MultipartFile uploadFile, @RequestParam("mem_introduce") String mem_introduce,
                                    @RequestParam ("favor") String[] favor, HttpSession session) {

        try {
            String memberUid = MemberSession.getMemberUid(session);
            logger.info(profile_absolute_path);
            logger.info(image_root_path);

            String uploadName = FileIOUtil.getUploadedFilePath(uploadFile, profile_absolute_path, image_root_path, FileType.IMAGE);
            logger.info(uploadName);
            MemberSession.setMemberSession(session, memberService.modMemberInfoPOST(memberUid, mem_nick, mem_introduce, uploadName, favor));
            return AuthStatusCode.MOD_SUCCESS.getCode();

        }catch (Exception e){
            e.printStackTrace();
        }
        return AuthStatusCode.MOD_FAIL.getCode();
    }


    @RequestMapping(value = "/setMemInfo", method = RequestMethod.POST)
    public String setMemberInfoPOST(@ModelAttribute EmailMemberDTO emailMemberDTO, @ModelAttribute SocialMemberDTO socialMemberDTO, Model model) {

        model.addAttribute("email", emailMemberDTO);
        model.addAttribute("social", socialMemberDTO);

        return "member/setMemInfo";
    }


    @RequestMapping(value = "/regMemInfo", method = RequestMethod.POST)
    @ResponseBody
    public int regMemberInfoPOST(HttpSession session, @RequestParam("mem_nick") String mem_nick, @RequestParam("mem_introduce") String mem_introduce,
                                    @RequestParam(value = "mem_profile", required = false) MultipartFile uploadFile, @RequestParam("favor") String[] favor,
                                    @RequestParam("em_id") String em_id,  @RequestParam("em_pwd") String em_pwd, @RequestParam("sm_id") String sm_id,
                                    @RequestParam("sm_type") String sm_type, @RequestParam("loginType") int loginType) {

        MemberDTO memberDTO = new MemberDTO();
        String uploadName = FileIOUtil.getUploadedFilePath(uploadFile, profile_absolute_path, image_root_path, FileType.IMAGE);
        memberDTO.setMem_nick(mem_nick);
        memberDTO.setMem_introduce(mem_introduce);

        if (loginType == LoginType.EMAIL.getCode()) {

            EmailMemberDTO emailMemberDTO = new EmailMemberDTO();
            emailMemberDTO.setEm_id(em_id);
            emailMemberDTO.setEm_pwd(em_pwd);
            String encodedAesId = memberService.findMemUidByEmail(em_id);
            memberService.addMemberInfo(em_id, memberDTO, uploadName, LoginType.EMAIL);
            memberService.addMemberFavor(encodedAesId, favor);
            memberService.addEmailMember(encodedAesId);

            if(memberService.deleteWhetherRegInfo(encodedAesId) < 1){

                return AuthStatusCode.REG_ALREADY_EXISTS.getCode();

            }


        }else if (loginType == LoginType.SOCIAL.getCode()){
            SocialMemberDTO socialMemberDTO = new SocialMemberDTO();
            socialMemberDTO.setSm_id(sm_id);
            socialMemberDTO.setSm_type(sm_type);
            memberService.addMemberInfo(null, memberDTO, uploadName, LoginType.SOCIAL);
            socialMemberDTO.setSm_id(socialMemberDTO.getSm_id());
            socialMemberDTO.setSm_type(socialMemberDTO.getSm_type());
            memberService.addSocialMember(socialMemberDTO, memberDTO.getMem_iuid());
        }

        session.setAttribute(MemberSession.MEMBER_SESSION_KEY, memberDTO);
        return AuthStatusCode.REG_SUCCESS.getCode();
    }


    @RequestMapping(value = "/delMemInfo", method = RequestMethod.GET)
    public String delMemberInfoGET(HttpSession session) {
        memberService.delMemInfo(MemberSession.getMemberUid(session));
        session.invalidate();
        return "redirect:/";
    }


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> getUserInfo(@RequestParam(value = "uid") String memUid, @RequestParam(value = "profile", required = false) String profile, @RequestParam(value = "nick", required = false) String nick,
                                    @RequestParam(value = "introduce", required = false) String introduce) {

        List<UserInfoType> typeList = new ArrayList<>();

        if (Boolean.valueOf(profile)) {
            typeList.add(UserInfoType.PROFILE);
        }
        if (Boolean.valueOf(nick)) {
            typeList.add(UserInfoType.NICK);
        }
        if (Boolean.valueOf(introduce)) {
            typeList.add(UserInfoType.INTRODUCE);
        }

        return memberService.getUserInfo(memUid, typeList);

    }

    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ResponseBody
    public int changePwd(HttpSession session, @RequestParam("password") String password) {
        AuthStatusCode code = memberService.changePwd(MemberSession.getMemberUid(session), password);
        return code.getCode();
    }


    @RequestMapping("/logout")
    @ResponseBody
    public int logOut(HttpSession session) {

        try {
            MemberSession.removeMemberSession(session);
            socialLogout(session);
            return AuthStatusCode.LOGOUT_SUCCESS.getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AuthStatusCode.LOGOUT_FAIL.getCode();
    }




    public static String extractClassHead(Object obj){
        return obj.getClass().getName().replace("LoginUtil", "").toLowerCase();
    }

    private void initLoginUtilArr(){

        utils = new LoginUtil[4];
        utils[0] = naverLoginUtil;
        utils[1] = facebookLoginUtil;
        utils[2] = googleLoginUtil;
        utils[3] = kakaoLoginUtil;

    }


    private void socialLogout(HttpSession session){

        String getSes = (String)SessionValidate.getValidSessionObj(session, "access_token");
        String[] check = getSes.split("%&");


        for (int i = 0; i < utils.length; ++i) {
            if(extractClassHead(utils[i]).equals(check[0])){
                utils[i].logOut(check[1]);
                break;
            }
        }

    }
    /******************************** 소셜별 콜백 매핑 *********************************/

    private SocialMemberDTO getMemberFromSocialCallback(final HttpSession session, Model model, final String code, final String state, final LoginUtil util){

        OAuth2AccessToken oauthToken;
        SocialMemberDTO socialMemberDTO = new SocialMemberDTO();
        JSONParser jsonParser = new JSONParser();
        String apiResult = null;
        String accessToken = null;
        String id = null;
        try {
            oauthToken = util.getAccessToken(session, code, state);
            apiResult = util.getUserProfile(oauthToken);
            JSONObject resultJsonObj = (JSONObject) jsonParser.parse(apiResult);
            JSONObject responseJsonObj = null;
            id = String.valueOf(resultJsonObj.get("id"));

            accessToken = String.valueOf(resultJsonObj.get("access_token"));

            if(accessToken != null){
                session.setAttribute("access_token", extractClassHead(util) + "%&" + accessToken);
            }
            responseJsonObj = (JSONObject)resultJsonObj.get("response");

            if(responseJsonObj != null){

                id =  String.valueOf(responseJsonObj.get("id"));
            }

            String aesId = aes256Cipher.AES_Encode(id);
            MemberDTO memberDTO = memberService.getExistMember(aesId);

            if(memberDTO != null){
                session.setAttribute("Member", memberDTO);
                model.addAttribute("M_Type", "social");
                return null;
            }

            socialMemberDTO.setSm_id(aesId);
            socialMemberDTO.setSm_type(aes256Cipher.AES_Encode(extractClassHead(util)));

        }catch (Exception e){
            e.printStackTrace();
        }

        return socialMemberDTO;


    }

    private String responsePathResolver(Model model, SocialMemberDTO socialDTO){

      if(socialDTO == null) return "redirect:/";
        model.addAttribute("socialMemberDTO", socialDTO);

        return "member/setMemInfo";


    }



    @RequestMapping(value = "/facebookcallback", method = RequestMethod.GET)
    public String facebookcallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session){

        return responsePathResolver(model, getMemberFromSocialCallback(session, model, code, state, facebookLoginUtil));
    }



    @RequestMapping(value = "/navercallback", method = RequestMethod.GET)
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) {

        return responsePathResolver(model, getMemberFromSocialCallback(session, model, code, state, facebookLoginUtil));

    }

    @RequestMapping(value = "/googlecallback", method = RequestMethod.GET)
    public String googlecallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) {

        return responsePathResolver(model, getMemberFromSocialCallback(session, model, code, state, facebookLoginUtil));

    }

    @RequestMapping(value = "/kakaocallback", method = RequestMethod.GET)
    public String kakaocallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session){

        return responsePathResolver(model, getMemberFromSocialCallback(session, model, code, state, facebookLoginUtil));

    }

    @ResponseBody
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    public ResponseEntity<String> getCategory() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<Map<String, String>> params = new ArrayList<>();

        params = memberService.getCategory();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String json = new ObjectMapper().writeValueAsString(params);


        return new ResponseEntity<String>(json, httpHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "/getBookmarkUploadDate", method = RequestMethod.GET)
    public @ResponseBody
    String getBookmarkUploadDate(HttpSession session) {
        return memberService.getBookmarkUploadDate(MemberSession.getMemberUid(session));
    }


    @RequestMapping(value = "/getFollowerListPage")
    public String getFollowerListPage() {
        return "following/followerList";
    }


    @RequestMapping(value = "/getFollowerPage")
    public String getFollowerPage() {
        return "following/followerPage";
    }

}
