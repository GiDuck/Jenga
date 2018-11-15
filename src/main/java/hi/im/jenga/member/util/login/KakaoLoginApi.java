package hi.im.jenga.member.util.login;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class KakaoLoginApi extends DefaultApi20 {
    protected KakaoLoginApi() {

    }

    private static class InstanceHolder {
        private static final KakaoLoginApi INSTANCE = new KakaoLoginApi();
    }

    public static KakaoLoginApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        System.out.println("카카오 엑ㅅ세스토큰");
        return "https://kauth.kakao.com/oauth/token?grant_type=authorization_code";

    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return "https://kauth.kakao.com/oauth/authorize";
    }
}
