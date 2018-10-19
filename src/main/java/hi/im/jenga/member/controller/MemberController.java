package hi.im.jenga.member.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.scribejava.core.model.OAuth2AccessToken;

import hi.im.jenga.member.util.cipher.AES256Cipher;
import hi.im.jenga.member.util.cipher.SHA256Cipher;
import hi.im.jenga.member.util.login.FacebookLoginUtil;
import hi.im.jenga.member.util.login.GoogleLoginUtil;
import hi.im.jenga.member.util.login.LoginUtil;
import hi.im.jenga.member.util.login.NaverLoginUtil;

@Controller
public class MemberController {
    
    
    @Autowired
    private NaverLoginUtil naverLoginUtil;
    @Autowired
    private GoogleLoginUtil googleLoginUtil;
    @Autowired
    private FacebookLoginUtil facebookLoginUtil;
  
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
        System.out.println("���̹�����"+session);
        util = facebookLoginUtil;
        String FacebookAuthUrl = util.getAuthorizationUrl(session);
        System.out.println("��ϼ���"+session);
        util = googleLoginUtil;
        String GoogleAuthUrl = util.getAuthorizationUrl(session);
        System.out.println("���ۼ���"+session);
        model.addAttribute("n", naverAuthUrl);
        model.addAttribute("f",FacebookAuthUrl);
        model.addAttribute("g",GoogleAuthUrl);
        System.out.println(naverAuthUrl);
        System.out.println(FacebookAuthUrl);
        System.out.println(GoogleAuthUrl);
        
        return "login";
    }
    
    @RequestMapping(value = "/callback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException, ParseException{
        OAuth2AccessToken oauthToken;
        LoginUtil util = naverLoginUtil;
        oauthToken = util.getAccessToken(session, code, state);
        System.out.println(oauthToken);
        //�α��� ����� ������ �о�´�.
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
         ���̹����̵�� ���� URL�� �����ϱ� ���Ͽ� naverLoginBOŬ������ getAuthorizationUrl�޼ҵ� ȣ�� 
        String naverAuthUrl = naverLoginUtil.getAuthorizationUrl(session);
        System.out.println("���̹�:" + naverAuthUrl);
        
        //���̹� 
        model.addAttribute("url", naverAuthUrl);

         ������ ���� URL�� View�� ���� 
        return "login";
    }
    
    @RequestMapping(value = "/callback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException{
        OAuth2AccessToken oauthToken;
        oauthToken = naverLoginUtil.getAccessToken(session, code, state);
        //�α��� ����� ������ �о�´�.
        apiResult = naverLoginUtil.getUserProfile(oauthToken);
        System.out.println(naverLoginUtil.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        System.out.println("result"+apiResult);

        return "callback";
    }
    
    @RequestMapping(value = "/googlelogin",  method = { RequestMethod.GET, RequestMethod.POST })
    public String googlelogin(HttpSession session, Model model) {
         ���̹����̵�� ���� URL�� �����ϱ� ���Ͽ� naverLoginBOŬ������ getAuthorizationUrl�޼ҵ� ȣ�� 
        String googleAuthUrl = googleLoginUtil.getAuthorizationUrl(session);
        System.out.println("����:" + googleAuthUrl);
        
        //���̹� 
        model.addAttribute("url", googleAuthUrl);

         ������ ���� URL�� View�� ���� 
      
        return "googlelogin";
    }
    
    
    @RequestMapping(value = "/googlecallback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String googlecallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException{
        System.out.println("�����ݹ�");
        OAuth2AccessToken oauthToken;
        oauthToken = googleLoginUtil.getAccessToken(session, code, state);
        System.out.println("��ū��");
        apiResult = googleLoginUtil.getUserProfile(oauthToken);
        System.out.println("�����");
        System.out.println(googleLoginUtil.getUserProfile(oauthToken).toString());
        model.addAttribute("result", apiResult);
        System.out.println("result"+apiResult);

        return "googlecallback";
    }
    
    @RequestMapping(value = "/facebooklogin",  method = { RequestMethod.GET, RequestMethod.POST })
    public String facebooklogin(HttpSession session, Model model) {
         ���̹����̵�� ���� URL�� �����ϱ� ���Ͽ� naverLoginBOŬ������ getAuthorizationUrl�޼ҵ� ȣ�� 
        String facebookAuthUrl = facebookLoginUtil.getAuthorizationUrl(session);
        System.out.println("���̽���:" + facebookAuthUrl);
        
        //���̹� 
        model.addAttribute("url", facebookAuthUrl);

         ������ ���� URL�� View�� ���� 
      
        return "facebooklogin";
    }
    
    @RequestMapping(value = "/facebookcallback",  method = { RequestMethod.GET, RequestMethod.POST }) 
    public String facebookcallback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException{
        System.out.println("����ݹ�");
        OAuth2AccessToken oauthToken;
        oauthToken = facebookLoginUtil.getAccessToken(session, code, state);
        System.out.println("��ū��");
        apiResult = facebookLoginUtil.getUserProfile(oauthToken);
        System.out.println("�����");
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
