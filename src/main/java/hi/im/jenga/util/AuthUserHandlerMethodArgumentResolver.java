package hi.im.jenga.util;

import hi.im.jenga.member.dto.MemberDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private AES256Cipher aes256Cipher;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);
        if(authUser == null) return false;
        if(!parameter.getParameterType().equals(MemberDTO.class)) return false;


        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

     if(!supportsParameter(parameter)){
         return WebArgumentResolver.UNRESOLVED;
     }

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        HttpSession session = request.getSession();

        AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);
        AuthUser.AuthUserType type = authUser.value();
        MemberDTO memberDTO = (MemberDTO)session.getAttribute("Member");
        if(session == null || memberDTO == null) return null;

        if(type == AuthUser.AuthUserType.DEFAULT){
            return memberDTO;

        }else if(type == AuthUser.AuthUserType.DECODED){

            try {
                memberDTO.setMem_iuid(aes256Cipher.AES_Decode(memberDTO.getMem_iuid()));
                memberDTO.setMem_nick(aes256Cipher.AES_Decode(memberDTO.getMem_nick()));
                memberDTO.setMem_profile(aes256Cipher.AES_Decode(memberDTO.getMem_introduce()));
                return memberDTO;

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;


    }
}
