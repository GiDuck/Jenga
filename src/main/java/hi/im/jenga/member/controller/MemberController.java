package hi.im.jenga.member.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.Gson;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.service.MemberService;
import hi.im.jenga.member.util.MemberUtilFile;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.login.*;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private NaverLoginUtil naverLoginUtil;
    @Autowired
    private GoogleLoginUtil googleLoginUtil;
    @Autowired
    private FacebookLoginUtil facebookLoginUtil;
    @Autowired
    private KakaoLoginUtil kakaoLoginUtil;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberUtilFile memberUtilFile;


    private String apiResult = null;

    @Autowired
    private AES256Cipher aes256Cipher;
    @Autowired
    private Util util;


    @RequestMapping(value = "/")
    public String hi(HttpSession session) {
        logger.info("이메일 세션은 "+(MemberDTO)session.getAttribute("Member"));
        logger.info("소셜 세션은 "+ (SocialMemberDTO)session.getAttribute("Social"));
//        logger.info("세션의 iuid는 "+((MemberDTO) session.getAttribute("Member")).getMem_iuid());
        return "main/main";
    }

    @RequestMapping(value = "/test")
    public String testParam(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO = memberService.testParam();

        logger.info(memberDTO.toString());
        return "/";
    }




//  interceptor처리
//    Model에 DTO를 넣어서 NULL 체크 후 interceptor에서 세션에 넣음
//    HttpServletSession 이어서 Logininterceptor에서 계속 getSession()이 안됐음 -> HttpSession으로 바꾸니 됨
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    public void logincheck(EmailMemberDTO emailMemberDTO, HttpSession session, HttpServletRequest request)throws Exception{
        /*logger.info("아이디"+emailMemberDTO.getEm_id());
        logger.info("비밀번호"+emailMemberDTO.getEm_pwd());*/

        String check = memberService.checkEmail(emailMemberDTO);

        logger.info("체크 "+check);

//        model.addAttribute("check", check);
        request.setAttribute("check", check);       //
        if(check.equals("success") || check.equals("noauth")){
            MemberDTO Member = memberService.getMemInfo(emailMemberDTO);
//            model.addAttribute("Member", Member);
            request.setAttribute("Member", Member);
            logger.info("if문 들어옴");
            logger.info(Member.getMem_iuid());
            return;
        }
        logger.info("if문 안들어감");

        // 내 글에서 수정하기 누르면 session iuid랑 글 번호를 가지고 prehandle에서 비교해야함 이 글이 이 사람것이 맞는지

    }
//    기존 로그인 POST
/*
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    public void logincheck(EmailMemberDTO emailMemberDTO, HttpSession session, HttpServletResponse response)throws Exception{
        logger.info("아이디"+emailMemberDTO.getEm_id());
        logger.info("비밀번호"+emailMemberDTO.getEm_pwd());
        String check = memberService.checkEmail(emailMemberDTO);
        logger.info("체크"+check);
        if(check.equals("success") || check.equals("noauth")){
            MemberDTO Member = memberService.getMemInfo(emailMemberDTO);
            session.setAttribute("Member",Member);
            logger.info((session.getAttribute("Member").toString()));
        }
        response.getWriter().println(check);

    }*/






    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {

        LoginUtil util = naverLoginUtil;
        String naverAuthUrl = util.getAuthorizationUrl(session);
        logger.info("header...");
        logger.info(request.getHeader("referer"));

        logger.info(naverAuthUrl);
        util = facebookLoginUtil;
        String FacebookAuthUrl = util.getAuthorizationUrl(session);

        util = googleLoginUtil;
        String GoogleAuthUrl = util.getAuthorizationUrl(session);

        util = kakaoLoginUtil;
        String KakaoAuthUrl = util.getAuthorizationUrl(session);

        logger.info(naverAuthUrl);
        logger.info(KakaoAuthUrl);
        logger.info(FacebookAuthUrl);
        logger.info(GoogleAuthUrl);

        model.addAttribute("k", KakaoAuthUrl);
        model.addAttribute("n", naverAuthUrl);
        model.addAttribute("f", FacebookAuthUrl);
        model.addAttribute("g", GoogleAuthUrl);



        return "member/login";
    }






    @RequestMapping(value = "/findPwd", method = RequestMethod.POST)
    public @ResponseBody
    void findPwdPOST(@RequestParam String find_pwd, HttpServletResponse response) throws Exception {
        String check = "success";
        boolean result;

        logger.info(find_pwd);
//        이메일로 가입한 회원조회
//        check구분해야함
        int findPwdCheck = memberService.findEPwd(find_pwd);

        if(findPwdCheck == 0){
            check = "error";
            response.getWriter().println(check);
            logger.info("가입된 이메일이 없거나 이메일 인증을 해주세요.");
            return;
        }
        response.getWriter().println(check);
        return;
    }






    // 이메일 가입 / 인증번호 발송 버튼
    @RequestMapping(value = "/authCheck", method = RequestMethod.POST)
    public void authCheck(@ModelAttribute EmailMemberDTO emailMemberDTO, HttpServletResponse response) throws Exception {
        String check = "success";
        String result;
        logger.info("아이디 받아옴"+emailMemberDTO.getEm_id());
        result = memberService.isEMExist(emailMemberDTO.getEm_id());
        logger.info(result);
//        이미 완전 가입완료한 이메일 일 경우
        if (result.equals("Y")) {
            logger.info("이미 존재하는 이메일입니다.");
            check = "isExist";
            response.getWriter().println(check);
            return;
//        가입은 했지만 인증을 안한 이메일
        } else if (result.equals("N")) {
            logger.info("N이다");  // N 이니까 UPDATE를 해야해서 N으로 구분해줌
            emailMemberDTO.setEm_acheck("N");
            logger.info(emailMemberDTO.getEm_id());
        }
//        N이거나 null이거나 무조건 들어옴 service에서 구분
        check = memberService.sendKey(emailMemberDTO);

        logger.info(check);
        if (check.equals("sendAuthKey")) {
            logger.info("인증키를 보냈습니다.");
            response.getWriter().println(check);
        }
    }







    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public @ResponseBody
    void joinPOST(@ModelAttribute EmailMemberDTO emailMemberDTO, HttpServletResponse response) throws Exception {
        String check = "success";
        boolean result;

        logger.info(emailMemberDTO.getEm_id());
        logger.info(emailMemberDTO.getEm_akey());

        result = memberService.authCheck(emailMemberDTO);

        // 이메일 인증 실패시 (잘못된 인증키 입력시)
        if (!result) {
            check = "error";
            response.getWriter().println(check);
            return;
        }
        logger.info(check);
        response.getWriter().println(check);

    }








    @RequestMapping(value ="/modMemInfo", method = RequestMethod.GET)
    public String modMemberInfoGET(HttpSession session, Model model) throws Exception {
//        List<MemberDTO> list = new ArrayList<MemberDTO>();
        logger.info(": : : modMemberInfoGET 들어옴");
        logger.info("바뀌기 전 파일경로 "+((MemberDTO) session.getAttribute("Member")).getMem_profile());
        logger.info("바뀌기 전 닉네임 "+((MemberDTO) session.getAttribute("Member")).getMem_nick());
        logger.info("바뀌기 전 소개 "+((MemberDTO) session.getAttribute("Member")).getMem_introduce());

        MemberDTO memberDTO = memberService.modMemberInfoGET((MemberDTO)session.getAttribute("Member"));

        List<String> favor = memberService.getMemFavor(((MemberDTO) session.getAttribute("Member")).getMem_iuid());
        logger.info("컨트롤러 페버"+favor);

       /* String parsedURL = (memberDTO.getMem_profile()).replace( "D:\\jengaResource\\upload\\",  "");
        logger.info("사용자 profile... " + parsedURL);*/

       // memberDTO.setMem_profile(parsedURL);
        model.addAttribute("DTO", memberDTO);   // 닉네임, 파일경로 복호화 후 받은 DTO를 뷰에 넘겨줌
        model.addAttribute("favor", favor);      // 선택한 favor 가져옴

        return "member/modMemInfo";
    }






    @RequestMapping(value ="/modMemInfo", method = RequestMethod.POST)
    public String modMemberInfoPOST(@RequestParam("mem_nick") String mem_nick, @RequestParam(value = "mem_profile", required = false) MultipartFile uploadFile, @RequestParam("mem_introduce") String mem_introduce,
                                    @RequestParam String[] favor, HttpSession session, Model model) throws Exception {
        String s_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();
        logger.info(": : : modMemberInfoPOST 들어옴");
        logger.info("Session에서 뽑아온 iuid " + s_iuid);
        logger.info("수정 후 받아온 닉네임 " + mem_nick);                          //tbl_memInfo
        logger.info("수정 후 받아온 파일이름 " + uploadFile);                      //tbl_memInfo
        logger.info("수정 후 받아온 소개 " + mem_introduce);                      //tbl_memInfo


//      파일 업로드 결과값을 path로 받아온다. (이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝났음)
//      프사 새로 안올렸으면 utilFile에서 return ""임
        String uploadName;
        if(uploadFile != null){
            uploadName = memberUtilFile.fileUpload(uploadFile);
        }else {
            uploadName = "";    //바꾸기
        }

        logger.info("수정 후 받아온 파일이름 " + uploadName);                  //tbl_memInfo

        logger.info("수정 후 받아온 취향 선택된 개수 " + favor.length);         //tbl_mfavor

        for (String s : favor) {
            logger.info("수정 후 받아온 취향 선택된 카테고리 " + s);
        }

        MemberDTO memberDTO = memberService.modMemberInfoPOST(s_iuid, mem_nick, mem_introduce, uploadName, favor);
        session.setAttribute("Member", memberDTO);

        return "redirect:/";
    }





    // 모달에서 이메일, 비밀번호 넘어옴 / 여기서 uuid 만들어야함
    // 임시 추가정보 페이지 (GET)
    @RequestMapping(value = "/setMemInfo", method = RequestMethod.POST)
    public String setMemberInfoPOST(@ModelAttribute EmailMemberDTO emailMemberDTO, @ModelAttribute SocialMemberDTO socialMemberDTO, Model model, HttpServletResponse response) throws IOException {
        logger.info(": : setMemberInfoPOST : : emailMemberDTO.getEm_id()는 : " + emailMemberDTO.getEm_id());
        logger.info(": : setMemberInfoPOST : : emailMemberDTO.getEm_pwd()는 : " + emailMemberDTO.getEm_pwd());


        logger.info(": : setMemberInfoPOST : : socialMemberDTO.getSm_id()는 : " + socialMemberDTO.getSm_id());
        logger.info(": : setMemberInfoPOST : : socialMemberDTO.getSm_type()는 : " + socialMemberDTO.getSm_type());

        return "member/setMemInfo"; //추가 정보 페이지로
    }




    // 추가정보페이지에 있는 submit 버튼
    // iuid, 파읾명 정하기, 등급은 Default
    // 임시 추가정보 페이지 (POST) / 프로필사진, 닉네임, 관심분야
    @RequestMapping(value = "/regMemInfo", method = RequestMethod.POST)
    public String regMemberInfoPOST(@RequestParam("mem_nick") String mem_nick, @RequestParam("mem_introduce") String mem_introduce, EmailMemberDTO emailMemberDTO, String[] favor, SocialMemberDTO socialMemberDTO,
                                    @RequestParam("mem_profile") MultipartFile uploadFile, HttpSession session) throws Exception {

        MemberDTO memberDTO = new MemberDTO();
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 em_id : "+ emailMemberDTO.getEm_id());         // 1단계에서 이메일
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 em_pwd : "+ emailMemberDTO.getEm_pwd());       // 1단계에서 비밀번호
//       이미 암호화 후 받아온 고유아이디, 소셜 타입
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 sm_id : " + socialMemberDTO.getSm_id());        // 1단계에서 고유아이디
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 sm_type : " + socialMemberDTO.getSm_type());    // 1단계에서 소셜 타입

        logger.info("선택한 취향 개수 "+favor.length);
        logger.info(favor[0]);
        logger.info("닉은"+ mem_nick);
        logger.info("소개는"+ mem_introduce);

//        logger.info(favor[0]);

//      UtilFile 객체 생성
//      파일 업로드 결과값을 path로 받아온다. (이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝났음)
        logger.info(uploadFile.getOriginalFilename());
        String uploadName;
        if(uploadFile != null){
            uploadName = memberUtilFile.fileUpload(uploadFile);
        }else {
            uploadName = "";    //바꾸기
        }

        memberDTO.setMem_nick(mem_nick);
        memberDTO.setMem_introduce(mem_introduce);

        logger.info("uploadName은 " +uploadName);
//        memberDTO.setMem_profile(uploadName);
//        이메일을 이용해서 임시로 넣음 iuid를 찾아야함
//        TODO 여기부터 다시
        if(!emailMemberDTO.getEm_id().equals("")) {
            logger.info("이메일은 여기서 다 처리");
            String aes_iuid = memberService.findIuid(emailMemberDTO);   // 이메일을 통하여 해당 이메일의 iuid (em_ref)를 가져옴 /  서비스에서
            logger.info("띠용 "+aes_iuid);

            memberService.addMemberInfo(socialMemberDTO, emailMemberDTO, memberDTO, uploadName,  "email");
            logger.info("이메일1");
            memberService.addMemberFavor(aes_iuid, favor);
            logger.info("이메일2");
            memberService.addEMember(aes_iuid);
            logger.info("이메일3");
            return "redirect:/";

//            session.setAttribute("Member", memberDTO);       // 입력된 정보를 세션에 넣어줌 -> 왜 넣더라 바로 로그인 된 상태로 하려고 세션에 넣는거였나 일단 주석
        }
    /*    // 인증여부 Y로 바꿔야함
        // update로 iuid, 닉네임, 파일경로, level, date)바꿔주기   IN tbl_memInfo
        // update로 이메일, 비밀번호, 인증여부 'Y' emember  WHERE

        if (!(emailMemberDTO.getEm_id().equals(""))) {
//      eMember에 이메일, 비밀번호 넣어야함
            logger.info("이메일 회원가입입니다.");
//            이메일, 비밀번호는 이미 넣었으니까 인증여부를 'Y' 로 바꾸고 WHERE 이메일
            memberService.addEMember(aes_iuid);

            return "redirect:/";
        }*/

        logger.info("소셜 " + socialMemberDTO.getSm_type() + " 회원가입입니다.");

        memberService.addMemberInfo(socialMemberDTO, emailMemberDTO, memberDTO, uploadName, "social");

        logger.info("2222222");
        socialMemberDTO.setSm_id(socialMemberDTO.getSm_id());
        socialMemberDTO.setSm_type(socialMemberDTO.getSm_type());

        logger.info("id는 " + socialMemberDTO.getSm_id());
        logger.info("type은 " + socialMemberDTO.getSm_type());

        memberService.addSMember(socialMemberDTO, memberDTO.getMem_iuid());


        return "redirect:/";
    }





    // 회원삭제
    @RequestMapping(value = "/delMemInfo", method = RequestMethod.GET)
    public String delMemberInfoGET(HttpSession session) throws Exception {
        logger.info("회원탈퇴로 들어옵니다");
        logger.info(((MemberDTO) session.getAttribute("Member")).getMem_iuid());
        String session_mem_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();

        memberService.delMemInfo(session_mem_iuid);

        session.invalidate();
//        회원정보 삭제 후 보여지는 페이지
        return "redirect:/";
    }


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> getUserInfo(HttpSession session, @RequestParam(value = "profile", required = false) String profile, @RequestParam(value = "nick", required = false) String nick,
                                                         @RequestParam(value = "introduce", required = false) String introduce) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String mem_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();
        Map<String, String> result = new HashMap();
        String check_profile = "";
        String check_nick = "";
        String check_introduce = "";
        if(profile != null){
            check_profile = "profile";
        }
        if(nick != null){
            check_nick = "nick";
        }
        if(introduce != null){
            check_introduce  = "introduce";
        }
        result = memberService.getUserInfo(mem_iuid, check_profile, check_nick, check_introduce);

        return result;

    }

    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    public ResponseEntity<Void> changePwd(HttpSession session, @RequestParam("pwd") String pwd) throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        String mem_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();
        memberService.changePwd(mem_iuid, pwd);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }




    // 로그아웃
    @RequestMapping("logout")
    @ResponseBody
    public boolean logOut(HttpSession session){

        try {
            if (session.getAttribute("Member") != null) {
                session.invalidate();
                return true;
                // TODO 여기도 로그아웃 이전 페이지 url 저장해서 redirect 해줘야하나 음
            }

            String getSes = (String) session.getAttribute("access_token");
            logger.info(getSes);
            String[] check = getSes.split("%&");

            if (check[0].equals("kakao")) {
                LoginUtil util = kakaoLoginUtil;
                util.logOut(check[1]);
                logger.info("kakao 세션 제거");
            } else if (check[0].equals("google")) {
                logger.info("google 세션 제거");
            } else if (check[0].equals("facebook")) {
                logger.info("facebook 세션 제거");
            } else if (check[0].equals("naver")) {
                LoginUtil util = naverLoginUtil;
                util.logOut("");
                logger.info("naver 세션 제거");
            }
            logger.info("제거완료");
            session.invalidate();
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;


    }

    /******************************** 소셜별 콜백 매핑 *********************************/

    @RequestMapping(value = "/facebookcallback", method = RequestMethod.GET)
    public String facebookcallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
        SocialMemberDTO socialMemberDTO = new SocialMemberDTO();
        OAuth2AccessToken oauthToken;
        LoginUtil util = facebookLoginUtil;
        oauthToken = util.getAccessToken(session, code, state);
        //로그인 사용자 정보를 읽어온다.
        apiResult = util.getUserProfile(oauthToken);
        /*model.addAttribute("result", apiResult);*/

        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(apiResult);
        String id = (String) json.get("id");
        logger.info(id);

        String aes_id = aes256Cipher.AES_Encode(id);
        MemberDTO memberDTO = memberService.isSMExist(aes_id);
        if (addSocialSession(model, session, memberDTO)) return "redirect:/";

        socialMemberDTO.setSm_id(aes_id);
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("facebook"));

        model.addAttribute("socialMemberDTO", socialMemberDTO);

        return "member/setMemInfo";
    }



    @RequestMapping(value = "/kakaocallback", method = RequestMethod.GET)
    public String kakaocallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
        SocialMemberDTO socialMemberDTO = new SocialMemberDTO();

        String oauthToken;
        String oauthToken1;
        LoginUtil util = kakaoLoginUtil;

        oauthToken = util.getAccessTokens(session, code, state);
        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(oauthToken);
        oauthToken1 = ((String) json.get("access_token"));
        session.setAttribute("access_token", "kakao%&"+oauthToken1);
        //로그인 사용자 정보를 읽어온다.
        apiResult = util.getUserProfiles(oauthToken1);
        JSONObject json1 = (JSONObject) jsonParser.parse(apiResult);
        String id = String.valueOf(json1.get("id"));

        String aes_id = aes256Cipher.AES_Encode(id);
        MemberDTO memberDTO = memberService.isSMExist(aes_id);
        if (addSocialSession(model, session, memberDTO)) return "redirect:/";

        socialMemberDTO.setSm_id(aes_id);
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("kakao"));

        model.addAttribute("socialMemberDTO", socialMemberDTO);

        return "member/setMemInfo";
    }

    @RequestMapping(value = "/navercallback", method = RequestMethod.GET)
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session, SocialMemberDTO socialMemberDTO) throws Exception {
        OAuth2AccessToken oauthToken;
        LoginUtil util = naverLoginUtil;
        oauthToken = util.getAccessToken(session, code, state);
        //로그인 사용자 정보를 읽어온다.
        apiResult = util.getUserProfile(oauthToken);
        session.setAttribute("access_token", "naver%&"+oauthToken);
        /*model.addAttribute("result", apiResult);*/


        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(apiResult);
        JSONObject json2 = (JSONObject) json.get("response");                                               // 뭐지 response가

        String id = (String) json2.get("id");

        String aes_id = aes256Cipher.AES_Encode(id);
        MemberDTO memberDTO = memberService.isSMExist(aes_id);
        if (addSocialSession(model, session, memberDTO)) return "redirect:/";

        socialMemberDTO.setSm_id(aes_id);
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("naver"));

        model.addAttribute("socialMemberDTO", socialMemberDTO);
        // 타입은 우리가 줘야함


        return "member/setMemInfo";
    }

    @RequestMapping(value = "/googlecallback", method = RequestMethod.GET)
    public String googlecallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
        SocialMemberDTO socialMemberDTO = new SocialMemberDTO();
        OAuth2AccessToken oauthToken;
        oauthToken = googleLoginUtil.getAccessToken(session, code, state);
        apiResult = googleLoginUtil.getUserProfile(oauthToken);

        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(apiResult);
        String id = (String)json.get("id");

        String aes_id = aes256Cipher.AES_Encode(id);
        MemberDTO memberDTO = memberService.isSMExist(aes_id);
        if (addSocialSession(model, session, memberDTO)) return "redirect:/";
        socialMemberDTO.setSm_id(aes_id);
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("google"));

        model.addAttribute("socialMemberDTO", socialMemberDTO);


        return "member/setMemInfo";
    }

    private boolean addSocialSession(Model model, HttpSession session, MemberDTO memberDTO) {
        if (memberDTO != null) {
            logger.info("존재하는 소셜 ID 입니다");
            session.setAttribute("Member",memberDTO);
            model.addAttribute("M_Type", "social");
            return true;
        }
        return false;
    }

    /*******************************************************************************/





    // 취향 선택 IN setMemberInfo
    @ResponseBody
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    public ResponseEntity<String> getCategory() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<Map<String, String>> params = new ArrayList<Map<String, String>>();

        params = memberService.getCategory();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        httpHeaders.add("Content-Type", "Application/xml"); 로 해도됨
        String json = new Gson().toJson(params);

        logger.info(json);

        return new ResponseEntity<String>(json,httpHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "/getBmksUploadDate", method = RequestMethod.GET)
    public @ResponseBody String getBmksUploadDate(HttpSession session) {
        String session_iuid  = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();

        String bmksUploadDate = memberService.getBmksUploadDate(session_iuid);

        return bmksUploadDate;
    }
}
