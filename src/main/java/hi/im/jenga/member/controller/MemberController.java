package hi.im.jenga.member.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.service.MemberService;
import hi.im.jenga.member.util.MemberUtilFile;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.login.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        logger.info("세션은 "+(MemberDTO)session.getAttribute("Member"));
//        logger.info("세션의 iuid는 "+((MemberDTO) session.getAttribute("Member")).getMem_iuid());
        return "main/main";
    }




/*
//  interceptor처리
//    Model에 DTO를 넣어서 NULL 체크 후 interceptor에서 세션에 넣음
//    HttpServletSession 이어서 Logininterceptor에서 계속 getSession()이 안됐음 -> HttpSession으로 바꾸니 됨
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    public void logincheck(EmailMemberDTO emailMemberDTO, Model model, HttpSession session)throws Exception{
        logger.info("아이디"+emailMemberDTO.getEm_id());
        logger.info("비밀번호"+emailMemberDTO.getEm_pwd());
        String check = memberService.checkEmail(emailMemberDTO);
        logger.info("체크"+check);
        model.addAttribute("check", check);
        if(check.equals("success") || check.equals("noauth")){
            MemberDTO Member = memberService.getMemInfo(emailMemberDTO);
            model.addAttribute("Member", Member);
            logger.info("if문 들어옴");
        }
        logger.info("if문 안들어감");


    }*/
//    기존 로그인 POST
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

    }






    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session, Model model) {

        LoginUtil util = naverLoginUtil;
        String naverAuthUrl = util.getAuthorizationUrl(session);
        logger.info("session 은 " + session);
        util = facebookLoginUtil;
        String FacebookAuthUrl = util.getAuthorizationUrl(session);

        util = googleLoginUtil;
        String GoogleAuthUrl = util.getAuthorizationUrl(session);

        util = kakaoLoginUtil;
        String KakaoAuthUrl = util.getAuthorizationUrl(session);

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
            System.out.println(emailMemberDTO.getEm_id());
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
        // 인증여부 Y로 바꿔야함, 테이블에 값 넣어야함

        /*memberService.join(emailMemberDTO);*/
        response.getWriter().println(check);

    }








    @RequestMapping(value ="/modMemInfo", method = RequestMethod.GET)
    public String modMemberInfoGET(HttpSession session, Model model) throws Exception {
//        List<MemberDTO> list = new ArrayList<MemberDTO>();
        logger.info(": : : modMemberInfoGET 들어옴");
        logger.info("바뀌기 전 파일경로 "+((MemberDTO) session.getAttribute("Member")).getMem_profile());
        logger.info("바뀌기 전 닉네임 "+((MemberDTO) session.getAttribute("Member")).getMem_nick());

        MemberDTO memberDTO = memberService.modMemberInfoGET((MemberDTO)session.getAttribute("Member"));

        logger.info("복호화 한 파일경로 "+((MemberDTO) session.getAttribute("Member")).getMem_profile());
        logger.info("복호화 한 닉네임 "+((MemberDTO) session.getAttribute("Member")).getMem_nick());

        List<String> favor = memberService.getMemFavor(((MemberDTO) session.getAttribute("Member")).getMem_iuid());
        logger.info("컨트롤러 페버"+favor);
        logger.info("컨트롤러 페버"+favor.get(0));
        logger.info("컨트롤러 페버"+favor.get(0).toString());
        model.addAttribute("DTO", memberDTO);   // 닉네임, 파일경로 복호화 후 받은 DTO를 뷰에 넘겨줌
        model.addAttribute("favor", favor);      // 선택한 favor 가져옴

        return "member/modMemInfo";
    }





    @RequestMapping(value ="/modMemInfo", method = RequestMethod.POST)
    public String modMemberInfoPOST(@RequestParam String mem_nick, @RequestParam("mem_profile") MultipartFile uploadFile,  MultipartHttpServletRequest request,
                                    @RequestParam String em_pwd, @RequestParam String[] favor, HttpSession session, Model model) throws Exception {
        String s_iuid = ((MemberDTO) session.getAttribute("Member")).getMem_iuid();
        logger.info(": : : modMemberInfoPOST 들어옴");
        logger.info("Session에서 뽑아온 iuid " + s_iuid);
        logger.info("수정 후 받아온 닉네임 " + mem_nick);                          //tbl_memInfo
        logger.info("수정 후 받아온 파일이름 " + uploadFile);                      //tbl_memInfo


//      파일 업로드 결과값을 path로 받아온다. (이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝났음)
//      프사 새로 안올렸으면 utilFile에서 return ""임
        String uploadName = memberUtilFile.fileUpload(request, uploadFile);

        logger.info("수정 후 받아온 파일이름 " + uploadName);                  //tbl_memInfo

        logger.info("수정 후 받아온 비밀번호 " + em_pwd);                       //tbl_Emember

        logger.info("수정 후 받아온 취향 선택된 개수 " + favor.length);         //tbl_mfavor

        for (String s : favor) {
            logger.info("수정 후 받아온 취향 선택된 카테고리 " + s);
        }

        MemberDTO memberDTO = memberService.modMemberInfoPOST(s_iuid, mem_nick, uploadName, em_pwd, favor);
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
//        value="/callback"
//        소셜로그인 후 uid를 추가정보입력페이지로 넘겨야함

        return "member/setMemInfo"; //추가 정보 페이지로
    }







    // 추가정보페이지에 있는 submit 버튼
    // iuid, 파읾명 정하기, 등급은 Default
    // 임시 추가정보 페이지 (POST) / 프로필사진, 닉네임, 관심분야
    @RequestMapping(value = "/regMemInfo", method = RequestMethod.POST)
    public String regMemberInfoPOST(@RequestParam String mem_nick, EmailMemberDTO emailMemberDTO, String[] favor, SocialMemberDTO socialMemberDTO, @RequestParam("mem_profile") MultipartFile uploadFile,
                                    MultipartHttpServletRequest request, HttpSession session) throws Exception {
        MemberDTO memberDTO = new MemberDTO();

        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 em_id : "+ emailMemberDTO.getEm_id());         // 1단계에서 이메일
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 em_pwd : "+ emailMemberDTO.getEm_pwd());       // 1단계에서 비밀번호
//       이미 암호화 후 받아온 고유아이디, 소셜 타입
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 sm_id : " + socialMemberDTO.getSm_id());        // 1단계에서 고유아이디
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 sm_type : " + socialMemberDTO.getSm_type());    // 1단계에서 소셜 타입

       /* logger.info(": : regMemberInfoPOST : : uploadFile : " + uploadFile);                                    // 2단계에서 넣은 이미지
        logger.info(": : regMemberInfoPOST : : mem_nick : " + mem_nick);                                        // 2단계에서 입력한 닉네임*/

        System.out.println(favor.length);

        logger.info(favor[0]);

//      UtilFile 객체 생성
//      파일 업로드 결과값을 path로 받아온다. (이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝났음)
        String uploadName = memberUtilFile.fileUpload(request, uploadFile);


        /*** 다시
         *
         * service에서 암호화, iuid 생성
         *
         * ***/

//        이메일을 이용해서 임시로 넣음 iuid를 찾아야함

        String aes_iuid = memberService.findIuid(emailMemberDTO);   // 이메일을 통하여 해당 이메일의 iuid (ref)를 가져옴

        logger.info("aes_iuid는 " + aes_iuid);

        memberDTO.setMem_profile(uploadName);
        memberDTO.setMem_nick(mem_nick);
        memberDTO.setMem_iuid(aes_iuid);

        memberService.addMemberInfo(memberDTO);
        memberService.addMemberFavor(aes_iuid,favor);

        session.setAttribute("Member",memberDTO);       // 입력된 정보를 세션에 넣어줌

        /*** 다시 ***/
        // if 이메일 회원가입이면
        // 인증여부 Y로 바꿔야함
        // update로 iuid, 닉네임, 파일경로, level, date)바꿔주기   IN tbl_memInfo
        // update로 이메일, 비밀번호, 인증여부 'Y' emember  WHERE

        if (!(emailMemberDTO.getEm_id().equals(""))) {
//      eMember에 이메일, 비밀번호 넣어야함
            logger.info("이메일 회원가입입니다.");
//            이메일, 비밀번호는 이미 넣었으니까 인증여부를 'Y' 로 바꾸고 WHERE 이메일
            memberService.addEMember(aes_iuid);

            return "redirect:/";
        }

        logger.info("소셜 " + socialMemberDTO.getSm_type() + " 회원가입입니다.");
        // 아니고 소셜로그인이면 소셜고유아이디, iuid, type

        socialMemberDTO.setSm_id(socialMemberDTO.getSm_id());
        socialMemberDTO.setSm_type(socialMemberDTO.getSm_type());

        logger.info("id는 " + socialMemberDTO.getSm_id());
        logger.info("type은 " + socialMemberDTO.getSm_type());

        memberService.addSMember(socialMemberDTO, aes_iuid);


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






    // 로그아웃
    @RequestMapping("logout")
    public String logOut(HttpSession session){
        String getSes = (String)session.getAttribute("access_token");
        System.out.println(getSes);
        String[] check =getSes.split("%&");

        if(check[0].equals("kakao")){
            LoginUtil util = kakaoLoginUtil;
            util.logOut(check[1]);
        }else if(check[0].equals("google")){

        }else if(check[0].equals("facebook")){

        }else if(check[0].equals("naver")){
            LoginUtil util = naverLoginUtil;
            util.logOut("");
        }
        session.invalidate();
        return "redirect:/";

    }






    /******************************** 소셜별 콜백 매핑 *********************************/

    @RequestMapping(value = "/facebookcallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String facebookcallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
        SocialMemberDTO socialMemberDTO = new SocialMemberDTO();
        boolean result = false;
        OAuth2AccessToken oauthToken;
        LoginUtil util = facebookLoginUtil;
        oauthToken = util.getAccessToken(session, code, state);
        //로그인 사용자 정보를 읽어온다.
        apiResult = util.getUserProfile(oauthToken);
        /*model.addAttribute("result", apiResult);*/

        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(apiResult);
        String id = (String) json.get("id");
        System.out.println(id);

        String aes_id = aes256Cipher.AES_Encode(id);
        result = memberService.isSMExist(aes_id);
        if (result) {
            logger.info("존재하는 소셜 ID 입니다");
            return "redirect:/";
        }

        socialMemberDTO.setSm_id(aes_id);                   // 네이버 고유아이디를 암호화
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("facebook"));              // 소셜 타입 직접 정의 "naver"를 암호화

        model.addAttribute("socialMemberDTO", socialMemberDTO);

        return "member/setMemInfo";
    }

    @RequestMapping(value = "/kakaocallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String kakaocallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
        SocialMemberDTO socialMemberDTO = new SocialMemberDTO();
        boolean result = false;
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
        result = memberService.isSMExist(aes_id);
        if (result) {
            logger.info("존재하는 소셜 ID 입니다");
            return "redirect:/";
        }

        socialMemberDTO.setSm_id(aes_id);                   // 네이버 고유아이디를 암호화
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("kakao"));              // 소셜 타입 직접 정의 "naver"를 암호화

        model.addAttribute("socialMemberDTO", socialMemberDTO);

        return "member/setMemInfo";
    }

    @RequestMapping(value = "/navercallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session, SocialMemberDTO socialMemberDTO) throws Exception {
        boolean result = false;
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

        String id = (String) json2.get("id");           // 네이버 고유아이디

        String aes_id = aes256Cipher.AES_Encode(id);
        result = memberService.isSMExist(aes_id);
        if (result) {
            logger.info("존재하는 소셜 ID 입니다");

            return "redirect:/";
        }

        socialMemberDTO.setSm_id(aes_id);                                               // 네이버 고유아이디를 암호화
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("naver"));              // 소셜 타입 직접 정의 "naver"를 암호화

        model.addAttribute("socialMemberDTO", socialMemberDTO);
        // 타입은 우리가 줘야함


        return "member/setMemInfo";
    }

    @RequestMapping(value = "/googlecallback", method = {RequestMethod.GET, RequestMethod.POST})
    public String googlecallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
        SocialMemberDTO socialMemberDTO = new SocialMemberDTO();
        boolean result = false;
        OAuth2AccessToken oauthToken;
        oauthToken = googleLoginUtil.getAccessToken(session, code, state);
        apiResult = googleLoginUtil.getUserProfile(oauthToken);

        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(apiResult);
        String id = (String)json.get("id");

        String aes_id = aes256Cipher.AES_Encode(id);
        result = memberService.isSMExist(aes_id);
        if (result) {
            logger.info("존재하는 소셜 ID 입니다");
            return "redirect:/";
        }
        socialMemberDTO.setSm_id(aes_id);                   // 네이버 고유아이디를 암호화
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("google"));              // 소셜 타입 직접 정의 "naver"를 암호화

        model.addAttribute("socialMemberDTO", socialMemberDTO);


        return "member/setMemInfo";
    }


    /*******************************************************************************/





    // 취향 선택 IN setMemberInfo
    @ResponseBody
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    public List<Map<String, String>> getCategory() {

        List<Map<String, String>> params = new ArrayList<Map<String, String>>();

        Map<String, String> map = new HashMap<String, String>();

        map.put("name", "문화/예술");
        map.put("image",
                "https://cdn20.patchcdn.com/users/22924509/20180619/041753/styles/T800x600/public/processed_images/jag_cz_movie_theater_retro_shutterstock_594132752-1529438777-6045.jpg");

        params.add(map);

        Map<String, String> map1 = new HashMap<String, String>();

        map1.put("name", "경제/경영");
        map1.put("image", "https://lajoyalink.com/wp-content/uploads/2018/03/Movie.jpg");
        params.add(map1);

        Map<String, String> map2 = new HashMap<String, String>();

        map2.put("name", "IT");
        map2.put("image",
                "https://www.moma.org/d/assets/W1siZiIsIjIwMTUvMTAvMjEvaWJ3dmJmanIyX3N0YXJyeW5pZ2h0LmpwZyJdLFsicCIsImNvbnZlcnQiLCItcmVzaXplIDIwMDB4MjAwMFx1MDAzZSJdXQ/starrynight.jpg?sha=161d3d1fb5eb4b23");

        params.add(map2);

        Map<String, String> map3 = new HashMap<String, String>();

        map3.put("name", "스포츠");
        map3.put("image", "https://www.indiewire.com/wp-content/uploads/2017/08/it-trailer-2-938x535.jpg?w=780");

        params.add(map3);

        Map<String, String> map4 = new HashMap<String, String>();

        map4.put("name", "라이프");
        map4.put("image", "https://thumb.ad.co.kr/article/54/12/e8/92/i/459922.png");

        params.add(map4);

        return params;

    }

}
