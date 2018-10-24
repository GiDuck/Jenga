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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    public String hi(){

        return "main/main";
    }


    @RequestMapping(value = "/login",  method = { RequestMethod.GET, RequestMethod.POST })
    public String login(HttpSession session, Model model) {

        LoginUtil util = naverLoginUtil;
        String naverAuthUrl = util.getAuthorizationUrl(session);
        logger.info("session"+session);
        util = facebookLoginUtil;
        String FacebookAuthUrl = util.getAuthorizationUrl(session);

        util = googleLoginUtil;
        String GoogleAuthUrl = util.getAuthorizationUrl(session);

        util = kakaoLoginUtil;
        String KakaoAuthUrl = util.getAuthorizationUrl(session);

        model.addAttribute("k", KakaoAuthUrl);
        model.addAttribute("n", naverAuthUrl);
        model.addAttribute("f",FacebookAuthUrl);
        model.addAttribute("g",GoogleAuthUrl);

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
    public String setMemberInfoPOST(@ModelAttribute EmailMemberDTO emailMemberDTO , @ModelAttribute SocialMemberDTO socialMemberDTO, Model model){

        logger.info(": : setMemberInfoPOST : : emailMemberDTO.getEm_id()는 : " +emailMemberDTO.getEm_id());
        logger.info(": : setMemberInfoPOST : : emailMemberDTO.getEm_pwd()는 : "+ emailMemberDTO.getEm_pwd());


        logger.info(": : setMemberInfoPOST : : socialMemberDTO.getSm_id()는 : "+ socialMemberDTO.getSm_id());
        logger.info(": : setMemberInfoPOST : : socialMemberDTO.getSm_type()는 : "+ socialMemberDTO.getSm_type());
//        value="/callback"
//        소셜로그인 후 uid를 추가정보입력페이지로 넘겨야함

        return "member/setMemInfo"; //추가 정보 페이지로
    }

    // 추가정보페이지에 있는 submit 버튼
    // iuid, 파읾명 정하기, 등급은 Default
    // 임시 추가정보 페이지 (POST) / 프로필사진, 닉네임, 관심분야
    @RequestMapping(value = "/regMemInfo", method = RequestMethod.POST)
    public String regMemberInfoPOST(@RequestParam String mem_nick, EmailMemberDTO emailMemberDTO, SocialMemberDTO socialMemberDTO,  @RequestParam("mem_profile") MultipartFile uploadFile,
                                    MultipartHttpServletRequest request) throws Exception {
        logger.info(": : : : : /regMemInfo 들어옴");
        MemberDTO memberDTO = new MemberDTO();

        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 em_id : "+ emailMemberDTO.getEm_id());         // 1단계에서 이메일
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 em_pwd : "+ emailMemberDTO.getEm_pwd());       // 1단계에서 비밀번호
//       이미 암호화 후 받아온 고유아이디, 소셜 타입
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 sm_id : "+ socialMemberDTO.getSm_id());        // 1단계에서 고유아이디
        logger.info(": : regMemberInfoPOST : : 1단계에서 넘어온 sm_type : "+ socialMemberDTO.getSm_type());    // 1단계에서 소셜 타입


        logger.info(": : regMemberInfoPOST : : uploadFile : "+ uploadFile);                                    // 2단계에서 넣은 이미지
        logger.info(": : regMemberInfoPOST : : mem_nick : "+ mem_nick);                                        // 2단계에서 입력한 닉네임

//      UtilFile 객체 생성
        UtilFile utilFile = new UtilFile();
//      파일 업로드 결과값을 path로 받아온다. (이미 fileUpload() 메소드에서 해당 경로에 업로드는 끝났음)
        String uploadPath = utilFile.fileUpload(request, uploadFile);

        logger.info(": : regMemberInfoPOST : : uploadPath : " + uploadPath);                                   // Y:\go\Jenga\profiles\파일명


        // 소셜이든 이메일이든 만들어서 uuid 넣어주기
        String iuid = UUID.randomUUID().toString();
/*
        // 2단계에서 입력한 닉네임, 파일경로를 DTO에 삽입 + 생성한 고유아이디 넣기
        memberDTO.setMem_iuid(iuid);
        memberDTO.setMem_nick(mem_nick);
        memberDTO.setMem_profile(uploadPath);
*/

//        AES 암호화(iuid, mem_nick, uploadPath) 후 memberDTO에 넣기
//        iuid는 밑 Smember테이블 넣을때 써야해서 변수에 넣음
        String aes_iuid = aes256Cipher.AES_Encode(iuid);
        memberDTO.setMem_iuid(aes_iuid);
        logger.info(": : : aes_iuid는 "+aes_iuid);

        memberDTO.setMem_nick(aes256Cipher.AES_Encode(mem_nick));
        memberDTO.setMem_profile(aes256Cipher.AES_Encode(uploadPath));


//      해당 경로만 받아 DB에 저장
//      이메일가입이든, 소셜가입이든 tbl_memInfo에 넣어야함
//      memInfo에 iuid, nick, profile, level, joinData 넣어야함
        memberService.addMemberInfo(memberDTO);


        // if 이메일 회원가입이면
        if(!(emailMemberDTO.getEm_id().equals(""))){
//      eMember에 이메일, 비밀번호 넣어야함
            logger.info("이메일 회원가입입니다.");
            SHA256Cipher sha = SHA256Cipher.getInstance();


            //String aes_id = aes256Cipher.AES_Encode(emailMemberDTO.getEm_id());
            //String sha_pw= sha.getEncSHA256(emailMemberDTO.getEm_pwd()); // error -> regMemberInfoPOST throws Exceptions 추가

            emailMemberDTO.setEm_id(aes256Cipher.AES_Encode(emailMemberDTO.getEm_id()));
            emailMemberDTO.setEm_pwd(sha.getEncSHA256(emailMemberDTO.getEm_pwd()));

            logger.info("emailMemberDTO.getEm_id()에서 뽑은 aes_id는 "+emailMemberDTO.getEm_id());
            logger.info("emailMemberDTO.getEm_pwd()에서 뽑은 sha_pw는 "+emailMemberDTO.getEm_pwd());

            memberService.addEMember(emailMemberDTO, aes_iuid);

            return "redirect:/";
        }

        logger.info("소셜 "+socialMemberDTO.getSm_type()+" 회원가입입니다.");
        // 아니고 소셜로그인이면 소셜고유아이디, iuid, type

        socialMemberDTO.setSm_id(socialMemberDTO.getSm_id());
        socialMemberDTO.setSm_type(socialMemberDTO.getSm_type());

        logger.info("id는 "+socialMemberDTO.getSm_id());
        logger.info("type은 "+socialMemberDTO.getSm_type());

        memberService.addSMember(socialMemberDTO, aes_iuid);

        return "redirect:/";
    }



  @RequestMapping(value = "/facebookcallback",  method = { RequestMethod.GET, RequestMethod.POST })
  public String facebookcallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception{
      OAuth2AccessToken oauthToken;
      LoginUtil util = facebookLoginUtil;
      oauthToken = util.getAccessToken(session, code, state);
      System.out.println(oauthToken);
      //로그인 사용자 정보를 읽어온다.
      apiResult = util.getUserProfile(oauthToken);
      logger.info(apiResult);
      model.addAttribute("result", apiResult);


      JSONParser jsonParser = new JSONParser();
      JSONObject json = (JSONObject) jsonParser.parse(apiResult);
      String email = (String) json.get("email");
      System.out.println(email);

      return "facebookcallback";
  }

    @RequestMapping(value = "/oauth",  method = { RequestMethod.GET, RequestMethod.POST })
    public String kakaocallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
        String oauthToken;
        String oauthToken1;
        LoginUtil util = kakaoLoginUtil;

        oauthToken = util.getAccessTokens(session, code, state);
        System.out.println(oauthToken);
        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(oauthToken);
        logger.info((String)json.get("access_token"));
        oauthToken1 = ((String)json.get("access_token"));
        //로그인 사용자 정보를 읽어온다.
        apiResult = util.getUserProfiles(oauthToken1);
        logger.info(apiResult);


        return "callback";
    }

    @RequestMapping(value = "/navercallback",  method = { RequestMethod.GET, RequestMethod.POST })
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session, SocialMemberDTO socialMemberDTO) throws Exception {
        boolean result = false;
        OAuth2AccessToken oauthToken;
        LoginUtil util = naverLoginUtil;
        oauthToken = util.getAccessToken(session, code, state);
        logger.info("oauthToken "+oauthToken);
        //로그인 사용자 정보를 읽어온다.
        apiResult = util.getUserProfile(oauthToken);

        model.addAttribute("result", apiResult);


        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(apiResult);
        JSONObject json2 = (JSONObject) json.get("response");                                               // 뭐지 response가

        String id = (String) json2.get("id");           // 네이버 고유아이디


        logger.info(": : callback : : json2 : "+json2);
        logger.info(": : callback : : json2.get(\"id\") "+ id);


        String aes_id = aes256Cipher.AES_Encode(id);

        socialMemberDTO.setSm_id(aes_id);                   // 네이버 고유아이디를 암호화
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("naver"));              // 소셜 타입 직접 정의 "naver"를 암호화

        logger.info(socialMemberDTO.getSm_id());
        logger.info(socialMemberDTO.getSm_type());

        result = memberService.isExist(aes_id);
        if(result){
            logger.info("존재하는 네이버 ID 입니다");
            return "redirect:/";
        }
        model.addAttribute("socialMemberDTO", socialMemberDTO);
        // 타입은 우리가 줘야함





        return "member/setMemInfo";
    }

    @RequestMapping(value = "/googlecallback",  method = { RequestMethod.GET, RequestMethod.POST })
    public String googlecallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws Exception {
        logger.info("구글콜백");
        OAuth2AccessToken oauthToken;
        oauthToken = googleLoginUtil.getAccessToken(session, code, state);
        logger.info("토큰받");
        apiResult = googleLoginUtil.getUserProfile(oauthToken);
        logger.info("결과받");
        logger.info(googleLoginUtil.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        logger.info("result"+apiResult);

        return "googlecallback";
    }

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
    

  /*  @RequestMapping(value = "/login",  method = { RequestMethod.GET, RequestMethod.POST })
    public String login(HttpSession session, Model model) {
         네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 
        String naverAuthUrl = naverLoginUtil.getAuthorizationUrl(session);
        logger.info("네이버:" + naverAuthUrl);
        
        //네이버 
        model.addAttribute("url", naverAuthUrl);

         생성한 인증 URL을 View로 전달 
        return "login";
    }
    
    @RequestMapping(value = "/callback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException{
        OAuth2AccessToken oauthToken;
        oauthToken = naverLoginUtil.getAccessToken(session, code, state);
        //로그인 사용자 정보를 읽어온다.
        apiResult = naverLoginUtil.getUserProfile(oauthToken);
        logger.info(naverLoginUtil.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        logger.info("result"+apiResult);

        return "callback";
    }
    
    @RequestMapping(value = "/googlelogin",  method = { RequestMethod.GET, RequestMethod.POST })
    public String googlelogin(HttpSession session, Model model) {
         네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 
        String googleAuthUrl = googleLoginUtil.getAuthorizationUrl(session);
        logger.info("구글:" + googleAuthUrl);
        
        //네이버 
        model.addAttribute("url", googleAuthUrl);

         생성한 인증 URL을 View로 전달 
      
        return "googlelogin";
    }
    
    

    
    @RequestMapping(value = "/facebooklogin",  method = { RequestMethod.GET, RequestMethod.POST })
    public String facebooklogin(HttpSession session, Model model) {
         네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 
        String facebookAuthUrl = facebookLoginUtil.getAuthorizationUrl(session);
        logger.info("페이스북:" + facebookAuthUrl);
        
        //네이버 
        model.addAttribute("url", facebookAuthUrl);

         생성한 인증 URL을 View로 전달 
      
        return "facebooklogin";
    }
    
    @RequestMapping(value = "/facebookcallback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String facebookcallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException{
        logger.info("페북콜백");
        OAuth2AccessToken oauthToken;
        oauthToken = facebookLoginUtil.getAccessToken(session, code, state);
        logger.info("토큰받");
        apiResult = facebookLoginUtil.getUserProfile(oauthToken);
        logger.info("결과받");
        logger.info(facebookLoginUtil.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        logger.info("result"+apiResult);

        return "facebookcallback";
    }*/
    
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
    

    
   
}
