package hi.im.jenga.member.util.login;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Component
public class GoogleLoginUtil implements LoginUtil {
    
    /* 인증 */
    @Value("#{data['google.client_id']}")
    private  String CLIENT_ID;// = "FMkvrKxBJWMA2Xzp6p_j";
    @Value("#{data['google.client_secret']}")
    private  String CLIENT_SECRET;// ="5bAmjyaLbz";
    @Value("#{data['naver.redirect_uri']}")
    private  String REDIRECT_URI;
    private final static String SESSION_STATE = "state_google";
    /*네이버 프로필 조회*/
    private final static String PROFILE_API_URL ="https://www.googleapis.com/plus/v1/people/me";
    public String getAuthorizationUrl(HttpSession session) {
        /* 세션 유효성 검증을 위하여 난수를 생성 */
        String state = generateRandomString();
        /* 생성한 난수 값을 session에 저장 */
        setSession(session,state);        

        /* Scribe에서 제공하는 인증 URL 생성 기능을 이용하여 네아로 인증 URL 생성 */
        OAuth20Service oauthService = new ServiceBuilder()                                                   
                .apiKey(CLIENT_ID)
                .apiSecret(CLIENT_SECRET)
                .callback(REDIRECT_URI)
                .scope("email")
                .state(state) //앞서 생성한 난수값을 인증 URL생성시 사용함
                .build(GoogleLoginApi.instance());

        return oauthService.getAuthorizationUrl();
    }
    
    public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException{

        /* Callback으로 전달받은 세선검증용 난수값과 세션에 저장되어있는 값이 일치하는지 확인 */
        String sessionState = getSession(session);
        System.out.println("세션지정");
        if(StringUtils.pathEquals(sessionState, state)){

            OAuth20Service oauthService = new ServiceBuilder()
                    .apiKey(CLIENT_ID)
                    .apiSecret(CLIENT_SECRET)
                    .callback(REDIRECT_URI)
                    .scope("email")
                    .state(state)
                    .build(GoogleLoginApi.instance());

            /* Scribe에서 제공하는 AccessToken 획득 기능으로 네아로 Access Token을 획득 */
            OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
            System.out.println(accessToken);
            return accessToken;
        }
        return null;
    }
    /* 세션 유효성 검증을 위한 난수 생성기 */
    public String generateRandomString() {
        return UUID.randomUUID().toString();
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
    public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException{

        OAuth20Service oauthService =new ServiceBuilder()
                .apiKey(CLIENT_ID)
                .apiSecret(CLIENT_SECRET)
                .scope("email")
                .callback(REDIRECT_URI).build(GoogleLoginApi.instance());

            OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL, oauthService);
        oauthService.signRequest(oauthToken, request);
        Response response = request.send();
        return response.getBody();
    }
    
    
}
