package hi.im.jenga.member.util.login;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


public class NaverLoginUtil implements LoginUtil {
        /* 인증 */
        @Value("#{data['naver.client_id']}")
        private  String CLIENT_ID;
        @Value("#{data['naver.client_secret']}")
        private  String CLIENT_SECRET;
        @Value("#{data['naver.redirect_uri']}")
        private  String REDIRECT_URI;
        private final static String SESSION_STATE = "state_naver";
        /*네이버 프로필 조회*/
        private final static String PROFILE_API_URL ="https://openapi.naver.com/v1/nid/me";
      
        public String getAuthorizationUrl(HttpSession session) {

            /* 세션 유효성 검증을 위하여 난수를 생성 */
            String state = generateRandomString();
            /* 생성한 난수 값을 session에 저장 */
            setSession(session,state);

            /* Scribe에서 제공하는 인증 URL 생성 기능을 이용하여 네아로 인증 URL 생성 */
            OAuth20Service oauthService = new ServiceBuilder(CLIENT_ID)
                    .apiSecret(CLIENT_SECRET)
                    .callback(REDIRECT_URI)
                    .state(state) //앞서 생성한 난수값을 인증 URL생성시 사용함
                    .build(NaverLoginApi.instance());

            return oauthService.getAuthorizationUrl();
        }

        /* 네이버아이디로 Callback 처리 및  AccessToken 획득 Method */
        public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException, InterruptedException, ExecutionException {
            System.out.println("엑세스토큰");
            /* Callback으로 전달받은 세선검증용 난수값과 세션에 저장되어있는 값이 일치하는지 확인 */
            String sessionState = getSession(session);

            System.out.println(sessionState);
            System.out.println(state);
            if(StringUtils.pathEquals(sessionState, state)){

                OAuth20Service oauthService = new ServiceBuilder(CLIENT_ID)
                        .apiSecret(CLIENT_SECRET)
                        .callback(REDIRECT_URI)
                        .state(state)
                        .build(NaverLoginApi.instance());

                /* Scribe에서 제공하는 AccessToken 획득 기능으로 네아로 Access Token을 획득 */
                OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
                System.out.println("엑세스 토큰"+accessToken.getAccessToken());
                return accessToken;
            }
            return null;
        }

        /* 세션 유효성 검증을 위한 난수 생성기 */
        public String generateRandomString() {
            return UUID.randomUUID().toString();
        }

    public String getAccessTokens(HttpSession session, String code, String state) {
        return null;
    }

    public String getUserProfiles(String oauthToken) {
        return null;
    }

    /* http session에 데이터 저장 */
        private void setSession(HttpSession session,String state){
            session.setAttribute(SESSION_STATE, state);     
        }

        /* http session에서 데이터 가져오기 */ 
        private String getSession(HttpSession session){
            return (String) session.getAttribute(SESSION_STATE);
        }
        /* Access Token을 이용하여 네이버 사용자 프로필 API를 호출 */
        public String getUserProfile(OAuth2AccessToken oauthToken) throws Exception{

            OAuth20Service oauthService =new ServiceBuilder(CLIENT_ID)

                    .apiSecret(CLIENT_SECRET)
                    .callback(REDIRECT_URI).build(NaverLoginApi.instance());

                OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL);
                System.out.println("리퀘"+request);
                System.out.println("오오스토큰"+oauthToken);
                System.out.println(request);
            oauthService.signRequest(oauthToken, request);
            Response response = oauthService.execute(request);
            return response.getBody();
        }
}
