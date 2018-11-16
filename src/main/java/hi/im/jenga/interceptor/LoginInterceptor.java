package hi.im.jenga.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    logger.info("postHandle로 들어옴");
        HttpSession session = request.getSession();
        ModelMap modelMap = modelAndView.getModelMap();
        Object memberDTO = modelMap.get("Member");
        ModelMap checkMap = modelAndView.getModelMap();
        Object check = checkMap.get("check");


        logger.info("1 "+ check.toString());
        logger.info("2 "+ (String)check);
        if(memberDTO != null){
            session.setAttribute("Member", memberDTO);
            response.getWriter().write((String)check);
            logger.info("ㄱ가자");
            return;
        }
        logger.info("야 나두");
        response.getWriter().write((String)check);
        return;

    }
}
