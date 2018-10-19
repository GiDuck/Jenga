package hi.im.jenga.member.controller;

import com.github.scribejava.core.model.OAuth2AccessToken;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.cipher.SHA256Cipher;
import hi.im.jenga.member.util.login.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

  
    private String apiResult = null;
   
    
  /*@Autowired
    private void setNaverLoginUtil(NaverLoginUtil naverLoginUtil) {
        this.naverLoginUtil = naverLoginUtil;
    }
    @Autowired
    private void setGoogleLoginUtil(GoogleLoginUtil googleLoginUtil) {
        this.googleLoginUtil = googleLoginUtil;
    }
    @Autowired
    private void setFacebookLoginUtil(FacebookLoginUtil facebookLoginUtil) {
        this.facebookLoginUtil = facebookLoginUtil;
    }*/
    
    
    @Autowired
    AES256Cipher aes256Cipher;


    @RequestMapping(value = "/")
    public String hi(){

        return "home";
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
        return "login";
    }
    
    @RequestMapping(value = "/callback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException, ParseException {
        OAuth2AccessToken oauthToken;
        LoginUtil util = naverLoginUtil;
        oauthToken = util.getAccessToken(session, code, state);
        System.out.println(oauthToken);
        //로그인 사용자 정보를 읽어온다.
        apiResult = util.getUserProfile(oauthToken);
      
            model.addAttribute("result", apiResult);
            
            
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(apiResult);
            JSONObject json2 = (JSONObject) json.get("response");
            String email = (String) json2.get("email");

            System.out.println(json2);
            System.out.println(json2.get("email"));
            System.out.println(email);
  
        return "callback";
    }
    
    
  /*  @RequestMapping(value = "/login",  method = { RequestMethod.GET, RequestMethod.POST })
    public String login(HttpSession session, Model model) {
         네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 
        String naverAuthUrl = naverLoginUtil.getAuthorizationUrl(session);
        System.out.println("네이버:" + naverAuthUrl);
        
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
        System.out.println(naverLoginUtil.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        System.out.println("result"+apiResult);

        return "callback";
    }
    
    @RequestMapping(value = "/googlelogin",  method = { RequestMethod.GET, RequestMethod.POST })
    public String googlelogin(HttpSession session, Model model) {
         네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 
        String googleAuthUrl = googleLoginUtil.getAuthorizationUrl(session);
        System.out.println("구글:" + googleAuthUrl);
        
        //네이버 
        model.addAttribute("url", googleAuthUrl);

         생성한 인증 URL을 View로 전달 
      
        return "googlelogin";
    }
    
    
    @RequestMapping(value = "/googlecallback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String googlecallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException{
        System.out.println("구글콜백");
        OAuth2AccessToken oauthToken;
        oauthToken = googleLoginUtil.getAccessToken(session, code, state);
        System.out.println("토큰받");
        apiResult = googleLoginUtil.getUserProfile(oauthToken);
        System.out.println("결과받");
        System.out.println(googleLoginUtil.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        System.out.println("result"+apiResult);

        return "googlecallback";
    }
    
    @RequestMapping(value = "/facebooklogin",  method = { RequestMethod.GET, RequestMethod.POST })
    public String facebooklogin(HttpSession session, Model model) {
         네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 
        String facebookAuthUrl = facebookLoginUtil.getAuthorizationUrl(session);
        System.out.println("페이스북:" + facebookAuthUrl);
        
        //네이버 
        model.addAttribute("url", facebookAuthUrl);

         생성한 인증 URL을 View로 전달 
      
        return "facebooklogin";
    }
    
    @RequestMapping(value = "/facebookcallback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String facebookcallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException{
        System.out.println("페북콜백");
        OAuth2AccessToken oauthToken;
        oauthToken = facebookLoginUtil.getAccessToken(session, code, state);
        System.out.println("토큰받");
        apiResult = facebookLoginUtil.getUserProfile(oauthToken);
        System.out.println("결과받");
        System.out.println(facebookLoginUtil.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        System.out.println("result"+apiResult);

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
