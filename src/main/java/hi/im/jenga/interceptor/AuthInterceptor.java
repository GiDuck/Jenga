package hi.im.jenga.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private void saveDestination(HttpServletRequest request){

        String uri = request.getRequestURI();

        String query = request.getQueryString();

        if(query == null || query.equals("null")){
            query = "";
        }else {
            query = "?" +  query;
        }

        logger.info("request.getMethod()는 "+request.getMethod());

        if(request.getMethod().equals("GET")){
            logger.info("uri는 "+ uri);          // /modMemInfo
            logger.info("query는 "+ query);
            logger.info("destination : " + (uri + query));
            request.getSession().setAttribute("dest", uri + query);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(": : : : AuthInterceptor preHandle 들어옴");
        HttpSession session = request.getSession();
        if(session.getAttribute("Member") == null) {
            logger.info("not logined");
            logger.info("세션없음");
            saveDestination(request);

            response.sendRedirect("/login");
            return false;
        }

        logger.info("세션있음");
        return true;
    }

/*    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info(": : : : AuthInterceptor postHandle 들어옴");
        HttpSession session = request.getSession();

        ModelMap modelMap = modelAndView.getModelMap();
        Object memberDTO = modelMap.get("Member");

        if(memberDTO != null){
            logger.info("new login success");
            session.setAttribute("Member", memberDTO);
        }

        Object dest = session.getAttribute("dest");
        logger.info(dest.toString());
        response.sendRedirect(dest != null ? (String)dest : "/");
    }*/
}
