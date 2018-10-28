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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    private AES256Cipher aes256Cipher;
    @Autowired
    private Util util;





    @RequestMapping(value = "/")
    public String hi(){

        return "main/main";
    }

    @RequestMapping(value = "/login",  method = RequestMethod.GET)
    public String loginGET(HttpSession session, Model model) {

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

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public @ResponseBody void loginPOST(EmailMemberDTO emailMemberDTO, HttpSession session, Model model) throws Exception {
        logger.info(emailMemberDTO.getEm_id());
        logger.info(emailMemberDTO.getEm_pwd());
        //memberService.loginEMCheck(emailMemberDTO);
    }

    @RequestMapping(value = "/findPwd", method = RequestMethod.POST)
    public @ResponseBody void findPwdPOST(@RequestParam String find_pwd, HttpServletResponse response) throws Exception{
        String check = "success";
        boolean result;
//        이메일로 가입한 회원조회


        /*result = memberService.isEMExist(aes256Cipher.AES_Encode(find_pwd));
        if(result) {    // 존재하면 해당 (이메일테이블)이메일에 이메일 전송
            memberService.findEPwd(find_pwd);
            response.getWriter().println(check);
            return;
        }*/


        /*result = memberService.isAMExist(aes256Cipher.AES_Encode(find_pwd));
        if(result) {    // 존재하면 해당 (임시테이블)이메일에 이메일 전송
            memberService.findAPwd(find_pwd);
            response.getWriter().println(check);
            return;
        }*/

        check="error";
        response.getWriter().println(check);
        return;
    }

    // 이메일 가입 / 인증번호 발송 버튼
    @RequestMapping(value="/authCheck", method = RequestMethod.POST)
    public void authCheck(@ModelAttribute EmailMemberDTO emailMemberDTO, HttpServletResponse response) throws Exception {
        String check = "success";
        boolean result;
        logger.info(emailMemberDTO.getEm_id());
        List<EmailMemberDTO> list = memberService.isEMExist(emailMemberDTO.getEm_id());

        if(list.get(0).getEm_id() == null){
            logger.info("정보가 없네요");
            return;
        }

        logger.info(""+list.get(0).getEm_id());         // 암호화 된 아이디
        logger.info(""+list.get(0).getEm_acheck());     // 인증여부

        String aes_id = list.get(0).getEm_id();
        String acheck = list.get(0).getEm_acheck();

        // emailMemberDTO는 메일 X, X 일때 쓰려고 보냄
        // list는 메일이 O 일때 쓰려고
        check = memberService.sendKey(emailMemberDTO, list);

        logger.info(check);
        if(check.equals("isExist")){
            logger.info("이미 존재하는 이메일입니다.");
            response.getWriter().println(check);
        }else if(check.equals("sendAuthKey")){
            logger.info("인증키를 보냈습니다.");
            response.getWriter().println(check);
        }
        //


        /*logger.info(String.valueOf(result));
        if(result){
            check = "error";
            response.getWriter().println(check);
            return;
        }*/




        //memberService.sendKey(emailMemberDTO);
     /*   logger.info("돌아와서 "+check);
        response.getWriter().println(check);*/
    }

    @RequestMapping(value="/join", method = RequestMethod.POST)
    public @ResponseBody void joinPOST(@ModelAttribute EmailMemberDTO emailMemberDTO, HttpServletResponse response) throws Exception {
        String check = "success";
        boolean result;

        logger.info(emailMemberDTO.getEm_id());
        logger.info(emailMemberDTO.getEm_akey());

        result = memberService.authCheck(emailMemberDTO);

        // 이메일 인증 실패시 (잘못된 인증키 입력시)
        if(!result){
            check = "error";
            response.getWriter().println(check);
            return;
        }
        logger.info(check);
        // 인증여부 Y로 바꿔야함, 테이블에 값 넣어야함

        /*memberService.join(emailMemberDTO);*/
        response.getWriter().println(check);

    }

    // 모달에서 이메일, 비밀번호 넘어옴 / 여기서 uuid 만들어야함
    // 임시 추가정보 페이지 (GET)

    @RequestMapping(value = "/setMemInfo", method = RequestMethod.POST)
    public String setMemberInfoPOST(@ModelAttribute EmailMemberDTO emailMemberDTO , @ModelAttribute SocialMemberDTO socialMemberDTO, Model model, HttpServletResponse response) throws IOException {

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
        String iuid = util.getIuid();
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


        String aes_Sid = aes256Cipher.AES_Encode(id);
        result = memberService.isSMExist(aes_Sid);
        if(result){
            logger.info("존재하는 네이버 ID 입니다");
            return "redirect:/";
        }

        socialMemberDTO.setSm_id(aes_Sid);                                               // 네이버 고유아이디를 암호화
        socialMemberDTO.setSm_type(aes256Cipher.AES_Encode("naver"));              // 소셜 타입 직접 정의 "naver"를 암호화

        logger.info(socialMemberDTO.getSm_id());
        logger.info(socialMemberDTO.getSm_type());

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


   /* // 메일 인증

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String RegisterPost(MemberVO user, Model model, RedirectAttributes rttr) throws Exception{

        System.out.println("regesterPost 진입 ");
        service.regist(user);
        rttr.addFlashAttribute("msg" , "가입시 사용한 이메일로 인증해주세요");
        return "redirect:/";
    }

    //이메일 인증 코드 검증
    @RequestMapping(value = "/emailConfirm", method = RequestMethod.GET)
    public String emailConfirm(MemberVO user,Model model,RedirectAttributes rttr) throws Exception {

        System.out.println("cont get user"+user);
        MemberVO vo = new MemberVO();
        vo=service.userAuth(user);
        if(vo == null) {
            rttr.addFlashAttribute("msg" , "비정상적인 접근 입니다. 다시 인증해 주세요");
            return "redirect:/";
        }
        //System.out.println("usercontroller vo =" +vo);
        model.addAttribute("login",vo);
        return "/user/emailConfirm";
    }

    /*
    // 이메일로 '임시' 가입하기(이메일, 비밀번호, 인증키 임시테이블에 저장 / mail로 인증키 보내야함)
         @RequestMapping(value = "/setAMem", method = RequestMethod.POST)
    public @ResponseBody void setAMemberPOST(@ModelAttribute AuthMemberDTO authMemberDTO, HttpServletResponse response) throws Exception{
        logger.info(": : : setAMemberPOST 들어옴");
        String check = "success";
        boolean result;

        String aes_Eid = aes256Cipher.AES_Encode(authMemberDTO.getAm_id());

        logger.info("이메일 "+authMemberDTO.getAm_id());
        logger.info("암호화한 이메일 "+aes_Eid);
        // 받은 이메일을 암호화시켜서 비교 / 존재하면 true -> if문 들어감

//        임시테이블에 이메일 존재유무
        result = memberService.isEMExist(aes_Eid);
        if(result){
            logger.info("존재하는 이메일입니다.");
            check = "EMexist";
            response.getWriter().println(check);
            return;
        }

//        임시테이블에 이메일 존재유무
        result = memberService.isAMExist(aes_Eid);
        if(result){
            logger.info("인증 대기중인 이메일입니다.");
            check = "AMexist";
            response.getWriter().println(check);
            return;
        }

        authMemberDTO.setAm_id(authMemberDTO.getAm_id());
        authMemberDTO.setAm_pwd(authMemberDTO.getAm_pwd());
        //메일 인증키 가져오기
        memberService.addAMember(authMemberDTO);

//        tbl_AMember에 데이터 넣고 인증번호 전송완료
        response.getWriter().println(check);

    }*/

}
