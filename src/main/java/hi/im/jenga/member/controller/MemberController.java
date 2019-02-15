package hi.im.jenga.member.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import hi.im.jenga.board.util.FileIOUtil;
import hi.im.jenga.board.util.FileType;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.service.MemberService;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    @Autowired
    public MemberController(NaverLoginUtil naverLoginUtil, GoogleLoginUtil googleLoginUtil, FacebookLoginUtil facebookLoginUtil, KakaoLoginUtil kakaoLoginUtil, MemberService memberService, AES256Cipher aes256Cipher) {
        this.naverLoginUtil = naverLoginUtil;
        this.googleLoginUtil = googleLoginUtil;
        this.facebookLoginUtil = facebookLoginUtil;
        this.kakaoLoginUtil = kakaoLoginUtil;
        this.memberService = memberService;
        this.aes256Cipher = aes256Cipher;
    }

    @Value("#{data['image.profile_path']}")
    private String profile_path;

    @Value("#{data['image.profile_absolute_path']}")
    private String profile_absolute_path;

    @Value("#{data['image.absolute_path']}")
    private String image_absolute_path;

    private final LoginUtil[] utils = {naverLoginUtil, facebookLoginUtil, googleLoginUtil, kakaoLoginUtil};


    @RequestMapping(value = "/")
    public String welcome() {
        return "main/main";
    }

    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    public void logincheck(EmailMemberDTO emailMemberDTO, HttpServletRequest request) {
        String check = null;
        try{
            check = memberService.checkEmail(emailMemberDTO);
            request.setAttribute("check", check);
        }catch(Exception e){
            e.printStackTrace();
        }


        if ("success".equals(check) || "noauth".equals(check)) {
            request.setAttribute("Member", memberService.getMemInfo(emailMemberDTO));
            return;
        }

    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {


        for (int i = 0; i < utils.length; ++i) {
            model.addAttribute(extractClassHead(utils[i]), utils[i].getAuthorizationUrl(session));
        }

        return "member/login";
    }


    @RequestMapping(value = "/findPwd", method = RequestMethod.POST)
    public @ResponseBody
    void findPwdPOST(@RequestParam String find_pwd, HttpServletResponse response) throws Exception {
        String check = "success";
        int findPwdCheck = memberService.findEPwd(find_pwd);

        if (findPwdCheck == 0) {
            check = "error";
        }
        response.getWriter().println(check);
        return;
    }


    // 이메일 가입 / 인증번호 발송 버튼
    @RequestMapping(value = "/authCheck", method = RequestMethod.POST)
    public void authCheck(@ModelAttribute EmailMemberDTO emailMemberDTO, HttpServletResponse response) throws Exception {
        String check = "success";
        String result;
        result = memberService.isEMExist(emailMemberDTO.getEm_id());

        if ("Y".equals(result)) {
            check = "isExist";
            response.getWriter().println(check);
            return;
        } else if ("N".equals(result)) {
            emailMemberDTO.setEm_acheck("N");
        }
        check = memberService.sendKey(emailMemberDTO);

        if (check.equals("sendAuthKey")) {
            response.getWriter().println(check);
        }
    }


    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public @ResponseBody
    void joinPOST(@ModelAttribute EmailMemberDTO emailMemberDTO, HttpServletResponse response) throws Exception {
        String check = "success";
        boolean result = memberService.authCheck(emailMemberDTO);

        if (!result) {
            check = "error";
        }
        response.getWriter().println(check);

    }


    @RequestMapping(value = "/modMemInfo", method = RequestMethod.GET)
    public String modMemberInfoGET(HttpSession session, Model model) throws Exception {

        MemberDTO memberDTO = memberService.modMemberInfoGET((MemberDTO) session.getAttribute("Member"));
        List<String> favor = memberService.getMemFavor(((MemberDTO) session.getAttribute("Member")).getMem_iuid());

        model.addAttribute("DTO", memberDTO);
        model.addAttribute("favor", favor);

        return "member/modMemInfo";
    }


    @RequestMapping(value = "/modMemInfo", method = RequestMethod.POST)
    public String modMemberInfoPOST(@RequestParam("mem_nick") String mem_nick, @RequestParam(value = "mem_profile", required = false) MultipartFile uploadFile, @RequestParam("mem_introduce") String mem_introduce,
                                    @RequestParam String[] favor, HttpSession session, Model model) throws Exception {
        String memberUid = new MemberSession().getMemberUid(session);

        String uploadName;
        if (uploadFile != null) {
            uploadName = new FileIOUtil(FileType.IMAGE).fileUpload(uploadFile, profile_absolute_path).replace(image_absolute_path, "");
        } else {
            uploadName = "";
        }

        MemberDTO memberDTO = memberService.modMemberInfoPOST(memberUid, mem_nick, mem_introduce, uploadName, favor);
        session.setAttribute("Member", memberDTO);

        return "redirect:/";
    }


    @RequestMapping(value = "/setMemInfo", method = RequestMethod.POST)
    public String setMemberInfoPOST(@ModelAttribute EmailMemberDTO emailMemberDTO, @ModelAttribute SocialMemberDTO socialMemberDTO, Model model, HttpServletResponse response) throws IOException {
        return "member/setMemInfo";
    }


    @RequestMapping(value = "/regMemInfo", method = RequestMethod.POST)
    public String regMemberInfoPOST(@RequestParam("mem_nick") String mem_nick, @RequestParam("mem_introduce") String mem_introduce, EmailMemberDTO emailMemberDTO, String[] favor, SocialMemberDTO socialMemberDTO,
                                    @RequestParam("mem_profile") MultipartFile uploadFile, HttpSession session) throws Exception {

        MemberDTO memberDTO = new MemberDTO();

        String uploadName;
        if (uploadFile != null) {
            uploadName = new FileIOUtil(FileType.IMAGE).fileUpload(uploadFile, profile_absolute_path).replace(image_absolute_path, "");
        } else {
            uploadName = "";
        }

        memberDTO.setMem_nick(mem_nick);
        memberDTO.setMem_introduce(mem_introduce);


        if (emailMemberDTO.getEm_id().trim().length() > 0) {
            String aesId = memberService.findIuid(emailMemberDTO);
            memberService.addMemberInfo(socialMemberDTO, emailMemberDTO, memberDTO, uploadName, "email");
            memberService.addMemberFavor(aesId, favor);
            memberService.addEMember(aesId);


        }else{

            memberService.addMemberInfo(socialMemberDTO, emailMemberDTO, memberDTO, uploadName, "social");
            socialMemberDTO.setSm_id(socialMemberDTO.getSm_id());
            socialMemberDTO.setSm_type(socialMemberDTO.getSm_type());
            memberService.addSMember(socialMemberDTO, memberDTO.getMem_iuid());

        }

        return "redirect:/";
    }


    @RequestMapping(value = "/delMemInfo", method = RequestMethod.GET)
    public String delMemberInfoGET(HttpSession session) throws Exception {
        memberService.delMemInfo(new MemberSession().getMemberUid(session));
        session.invalidate();
        return "redirect:/";
    }


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> getUserInfo(@RequestParam(value = "uid") String mem_iuid, @RequestParam(value = "profile", required = false) String profile, @RequestParam(value = "nick", required = false) String nick,
                                    @RequestParam(value = "introduce", required = false) String introduce) throws Exception {
        String checkProfile = null;
        String checkNick  = null;
        String checkIntroduce  = null;
        if (profile != null) {
            checkProfile = "profile";
        }
        if (nick != null) {
            checkNick = "nick";
        }
        if (introduce != null) {
            checkIntroduce = "introduce";
        }

        return memberService.getUserInfo(mem_iuid, checkProfile, checkNick, checkIntroduce);

    }

    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    public void changePwd(HttpSession session, @RequestParam("pwd") String pwd) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        String memUid = new MemberSession().getMemberUid(session);
        memberService.changePwd(memUid, pwd);
    }


    @RequestMapping("logout")
    @ResponseBody
    public boolean logOut(HttpSession session) {

        try {
            if (SessionValidate.isSessionEmpty(session, "Member")) {
                session.invalidate();
                return true;
            }

            String getSes = (String) session.getAttribute("access_token");
            String[] check = getSes.split("%&");


            for (int i = 0; i < utils.length; ++i) {
                if(extractClassHead(utils[i]).equals(check[0])){
                    utils[i].logOut(check[1]);
                    break;
                }
            }
            session.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;


    }

    public static String extractClassHead(Object obj){
        return obj.getClass().getName().replace("LoginUtil", "").toLowerCase();
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
    public String facebookcallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {

        return responsePathResolver(model, getMemberFromSocialCallback(session, model, code, state, facebookLoginUtil));
    }



    @RequestMapping(value = "/navercallback", method = RequestMethod.GET)
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session, SocialMemberDTO socialMemberDTO) throws Exception {

        return responsePathResolver(model, getMemberFromSocialCallback(session, model, code, state, facebookLoginUtil));

    }

    @RequestMapping(value = "/googlecallback", method = RequestMethod.GET)
    public String googlecallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {

        return responsePathResolver(model, getMemberFromSocialCallback(session, model, code, state, facebookLoginUtil));


    }

    @RequestMapping(value = "/kakaocallback", method = RequestMethod.GET)
    public String kakaocallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {

        return responsePathResolver(model, getMemberFromSocialCallback(session, model, code, state, facebookLoginUtil));

    }



    // 취향 선택 IN setMemberInfo
    @ResponseBody
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    public ResponseEntity<String> getCategory() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<Map<String, String>> params = new ArrayList<Map<String, String>>();

        params = memberService.getCategory();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String json = new ObjectMapper().writeValueAsString(params);


        return new ResponseEntity<String>(json, httpHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "/getBmksUploadDate", method = RequestMethod.GET)
    public @ResponseBody
    String getBmksUploadDate(HttpSession session) {

        return memberService.getBmksUploadDate(new MemberSession().getMemberUid(session));
    }


    @RequestMapping(value = "/getFollowerListPage")
    public String getFollowerListPage(Model model) {
        return "following/followerList";
    }


    //팔로워 별로 글을 확인할 수 있는 페이지
    @RequestMapping(value = "/getFollowerPage")
    public String getFollowerPage(Model model) {
        return "following/followerPage";
    }

}
