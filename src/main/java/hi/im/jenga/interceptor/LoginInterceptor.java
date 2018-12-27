package hi.im.jenga.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.mongodb.util.JSON;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(": : : : LoginInterceptor preHandle 들어옴");
        HttpSession session = request.getSession();
        if(session.getAttribute("Member") != null){
            logger.info("현재 가지고 있는 session 제거 ");
            session.removeAttribute("Member");
            session.removeAttribute("dest");
//            response.setContentType("text/plain");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write("c");
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info(": : : : LoginInterceptor postHandle 들어옴");
        HttpSession session = request.getSession();
        Map<String, String> map = new HashMap();

        String dest = null;
        String check = (String)request.getAttribute("check");

        if(session.getAttribute("dest") != null){               //modMemInfo로 들어오면
            dest = session.getAttribute("dest").toString();
            logger.info("@ session.getAttribute(\"dest\") != null 들어옴");
        }

        if(request.getAttribute("Member") != null){    // success, noauth 시에 member 넣음
            logger.info("@@ success니까 들어와서 세션에 request에 있는거를 넣음");
            if(check.equals("success")) {   // TODO  테스트 하기 -> success일때만 세션에 넣고 noauth 시 세션에 안넣음
                session.setAttribute("Member", request.getAttribute("Member"));
            }
            logger.info("########## LoginInterceptor 로그인 완료 세션에 넣었습니다.");
        }

//        logger.info("null이면 AuthInterceptor 안거친거 "+dest);
//        logger.info("@@@ dest는 LoginInterceptor " + dest);
//        map.put("dest", dest != null ? dest : "/");
        logger.info("@@@@ check는 "+check);
/*

        if(check.equals("iderror") || check.equals("pwderror")){
            //response.getWriter().println(check);
            logger.info("id, pwd error");
//            return;

        }
//        Object memberDTO = modelMap.get("Member");
            logger.info("success, noauth");
*/

            map.put("check", check);

            JSONObject jsonObject = new JSONObject(map);
            response.setContentType("application/json; charset=UTF-8;");
            response.getWriter().println(jsonObject);

//            return;
    }
}
