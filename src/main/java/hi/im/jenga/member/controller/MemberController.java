package hi.im.jenga.member.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import hi.im.jenga.member.dto.EmailMemberDTO;
import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.dto.SocialMemberDTO;
import hi.im.jenga.member.service.MemberService;
import hi.im.jenga.member.util.UtilFile;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.cipher.SHA256Cipher;
import hi.im.jenga.member.util.login.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;

import java.util.*;

@Controller
public class MemberController {

    public static final Logger logger = LoggerFactory.getLogger(MemberController.class);
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


    private String apiResult = null;


    @Autowired
    AES256Cipher aes256Cipher;




    @RequestMapping(value = "/")
    public String hi() {

        return "main/main";
    }


    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(HttpSession session, Model model) {

        LoginUtil util = naverLoginUtil;
        String naverAuthUrl = util.getAuthorizationUrl(session);
        logger.info("session" + session);
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
/*
    // 모달에서 이메일, 비밀번호 넘어옴 / 여기서 uuid 만들어야함
    // 임시 추가정보 페이지 (GET)
    @RequestMapping(value = "/setMemInfo", method = RequestMethod.GET)
    public String addMemberInfoGET(Model model){
//        value="/callback"
//        소셜로그인 후 uid를 추가정보입력페이지로 넘겨야함

        return "member/setMemInfo";
    }*/

    // 모달에서 이메일, 비밀번호 넘어옴 / 여기서 uuid 만들어야함
    // 임시 추가정보 페이지 (GET)
    @RequestMapping(value = "/setMemInfo", method = RequestMethod.POST)
    public String setMemberInfoPOST(@ModelAttribute EmailMemberDTO emailMemberDTO, @ModelAttribute SocialMemberDTO socialMemberDTO, Model model) {


//        value="/callback"
//        소셜로그인 후 uid를 추가정보입력페이지로 넘겨야함

        return "member/setMemInfo"; //추가 정보 페이지로
    }

    // 추가정보페이지에 있는 submit 버튼
    // iuid, 파읾명 정하기, 등급은 Default
    // 임시 추가정보 페이지 (POST) / 프로필사진, 닉네임, 관심분야
    @RequestMapping(value = "/regMemInfo", method = RequestMethod.POST)
    public String regMemberInfoPOST(@RequestParam String mem_nick, EmailMemberDTO emailMemberDTO, SocialMemberDTO socialMemberDTO, @RequestParam("mem_profile") MultipartFile uploadFile,
                                    MultipartHttpServletRequest request) throws Exception {
        MemberDTO memberDTO = new MemberDTO();

//      UtilFile 객체 생성
        UtilFile utilFile = new UtilFile();
//      파일 업로드 결과값을 path로 받아온다. (이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝났음)
        String uploadPath = utilFile.fileUpload(request, uploadFile);




        // 소셜이든 이메일이든 만들어서 uuid 넣어주기
        String iuid = UUID.randomUUID().toString();

//        AES 암호화(iuid, mem_nick, uploadPath) 후 memberDTO에 넣기
//        iuid는 밑 Smember테이블 넣을때 써야해서 변수에 넣음
        String aes_iuid = aes256Cipher.AES_Encode(iuid);
        memberDTO.setMem_iuid(aes_iuid);


        memberDTO.setMem_nick(aes256Cipher.AES_Encode(mem_nick));
        memberDTO.setMem_profile(aes256Cipher.AES_Encode(uploadPath));


//      해당 경로만 받아 DB에 저장
//      이메일가입이든, 소셜가입이든 tbl_memInfo에 넣어야함
//      memInfo에 iuid, nick, profile, level, joinData 넣어야함
        memberService.addMemberInfo(memberDTO);


        // if 이메일 회원가입이면
        if (!(emailMemberDTO.getEm_id().equals(""))) {
//      eMember에 이메일, 비밀번호 넣어야함

            SHA256Cipher sha = SHA256Cipher.getInstance();


            //String aes_id = aes256Cipher.AES_Encode(emailMemberDTO.getEm_id());
            //String sha_pw= sha.getEncSHA256(emailMemberDTO.getEm_pwd()); // error -> regMemberInfoPOST throws Exceptions 추가

            emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));
            emailMemberDTO.setEm_pwd(sha.getEncSHA256(emailMemberDTO.getEm_pwd()));



            memberService.addEMember(emailMemberDTO, aes_iuid);

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
        result = memberService.isExist(aes_id);
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
        result = memberService.isExist(aes_id);
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
        result = memberService.isExist(aes_id);
        if (result) {
            logger.info("존재하는 소셜 ID 입니다");
            return "redirect:/";
        }

        socialMemberDTO.setSm_id(aes_id);                   // 네이버 고유아이디를 암호화
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
        /*model.addAttribute("result", apiResult);*/

        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(apiResult);
        String id = (String)json.get("id");

        String aes_id = aes256Cipher.AES_Encode(id);
        result = memberService.isExist(aes_id);
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

        map.put("name", "영화");
        map.put("image",
                "https://cdn20.patchcdn.com/users/22924509/20180619/041753/styles/T800x600/public/processed_images/jag_cz_movie_theater_retro_shutterstock_594132752-1529438777-6045.jpg");

        params.add(map);

        Map<String, String> map1 = new HashMap<String, String>();

        map1.put("name", "음악");
        map1.put("image", "https://lajoyalink.com/wp-content/uploads/2018/03/Movie.jpg");
        params.add(map1);

        Map<String, String> map2 = new HashMap<String, String>();

        map2.put("name", "미술");
        map2.put("image",
                "https://www.moma.org/d/assets/W1siZiIsIjIwMTUvMTAvMjEvaWJ3dmJmanIyX3N0YXJyeW5pZ2h0LmpwZyJdLFsicCIsImNvbnZlcnQiLCItcmVzaXplIDIwMDB4MjAwMFx1MDAzZSJdXQ/starrynight.jpg?sha=161d3d1fb5eb4b23");

        params.add(map2);

        Map<String, String> map3 = new HashMap<String, String>();

        map3.put("name", "IT");
        map3.put("image", "https://www.indiewire.com/wp-content/uploads/2017/08/it-trailer-2-938x535.jpg?w=780");

        params.add(map3);

        Map<String, String> map4 = new HashMap<String, String>();

        map4.put("name", "시사");
        map4.put("image", "https://thumb.ad.co.kr/article/54/12/e8/92/i/459922.png");

        params.add(map4);

        return params;

    }

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


}

    /*

    @RequestMapping(value = "/aestest")
    public String aesteset(Model model) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        return "aestest";
    }

    @RequestMapping(value = "/aesres")
    public String aesres(Model model, String test) throws Exception {



        SHA256Cipher sha = SHA256Cipher.getInstance();
        String shas = sha.getEncSHA256(test);

        String res=aes256Cipher.AES_Encode(test);
        String deco = aes256Cipher.AES_Decode(res);
        model.addAttribute("sha",shas);
        model.addAttribute("res", res);
        model.addAttribute("dec", deco);
        return "aesresult";
    }

    public String joinmember() {



        return "";
    }
    */

    


