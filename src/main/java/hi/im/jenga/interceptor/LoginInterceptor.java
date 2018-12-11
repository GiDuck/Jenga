package hi.im.jenga.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;
import hi.im.jenga.member.dto.MemberDTO;
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

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("preHandle~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        HttpSession session = request.getSession();
        if(session.getAttribute("Member") != null){
            logger.info("현재 가지고 있는 session 제거 ");
            session.removeAttribute("Member");
//            response.setContentType("text/plain");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write("c");
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    logger.info("postHandle로 들어옴");
        HttpSession session = request.getSession();
//        ModelMap modelMap = modelAndView.getModelMap();
//        Object check = modelMap.get("check");

        String check = (String)request.getAttribute("check");
        MemberDTO memberDTO = (MemberDTO)request.getAttribute("Member");

        logger.info("check는 "+check);

        if(check.equals("iderror") || check.equals("pwderror")){
            response.getWriter().println(check);
            logger.info("id, pwd error");


            //            response.setContentType("text/plain");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write((String)check);
            return;

        }
//        Object memberDTO = modelMap.get("Member");
            logger.info("success, noauth");
            session.setAttribute("Member", memberDTO);
            response.getWriter().println(check);

            return;
    }
}
