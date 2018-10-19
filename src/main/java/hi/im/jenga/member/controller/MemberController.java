package hi.im.jenga.member.controller;

import com.fasterxml.jackson.databind.JsonNode;
import hi.im.jenga.member.service.MongoService;
import hi.im.jenga.util.KakaoLoginUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MongoService mongoService;
    @Autowired
    private KakaoLoginUtil kakaoLoginUtil;

    @RequestMapping(value = "")
    public String hi(){
        //mongoService.getAnyway();
        return "home";
    }

    @RequestMapping(value="login")
    public String login(Model model){

        String KAKAO_JS_KEY = kakaoLoginUtil.getKakaoJsKey();
        String KAKAO_REST_KEY = kakaoLoginUtil.getKakaoRestKey();
        model.addAttribute("KakaoJsKey",KAKAO_JS_KEY);
        model.addAttribute("KakaoRestKey",KAKAO_REST_KEY);
        return "login";
    }

    @RequestMapping(value ="oauth", produces = "application/json", method = {RequestMethod.GET, RequestMethod.POST})
    public String oauth(@RequestParam("code") String code , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
        logger.info(": : : code는 "+code.toString());
        logger.info(code.toString());

        JsonNode token = KakaoLoginUtil.getAccessToken(code);

        JsonNode profile = KakaoLoginUtil.getKakaoUserInfo(token.path("access_token").toString());
        logger.info(": : : profile은 "+profile.toString());
        logger.info(profile.toString());
        //KakaoDTO dto = KakaoLogin.changeData(profile);
        //dto.setUser_snsId("k"+dto.getUser_snsId());

        System.out.println(session);
        //session.setAttribute("login", dto);
        //System.out.println(dto.toString());

        //dto = service.kakaoLogin(dto);
        return "login/kakaoLogin";
    }


    /*@RequestMapping(value = "login/kakaologin" , produces = "application/json", method = {RequestMethod.GET, RequestMethod.POST})
    public String kakaoLogin(@RequestParam("code") String code , HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{

        JsonNode token = KakaoLogin.getAccessToken(code);

        JsonNode profile = KakaoLogin.getKakaoUserInfo(token.path("access_token").toString());
        System.out.println(profile);
        //KakaoDTO dto = KakaoLogin.changeData(profile);
        //dto.setUser_snsId("k"+dto.getUser_snsId());

        System.out.println(session);
        //session.setAttribute("login", dto);
        //System.out.println(dto.toString());

        //dto = service.kakaoLogin(dto);
        return "login/kakaoLogin";
    }*/
}
