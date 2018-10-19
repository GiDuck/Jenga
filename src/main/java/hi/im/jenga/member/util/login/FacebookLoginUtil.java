package hi.im.jenga.member.util.login;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

public class FacebookLoginUtil implements LoginUtil{
    /* ���� */
    @Value("#{data['facebook.client_id']}")
    private  String CLIENT_ID;
    @Value("#{data['facebook.client_secret']}")
    private  String CLIENT_SECRET;
    @Value("#{data['naver.redirect_uri']}")
    private  String REDIRECT_URI;
    private final static String SESSION_STATE = "connected";
    private final static String PROFILE_API_URL ="https://graph.facebook.com/v3.1/me?fields=id,email,name";
    
    public String getAuthorizationUrl(HttpSession session) {
        /* ���� ��ȿ�� ������ ���Ͽ� ������ ���� */
        String state = generateRandomString();
        /* ������ ���� ���� session�� ���� */
        setSession(session,state);        

        /* Scribe���� �����ϴ� ���� URL ���� ����� �̿��Ͽ� �׾Ʒ� ���� URL ���� */
        OAuth20Service oauthService = new ServiceBuilder()                                                   
                .apiKey(CLIENT_ID)
                .apiSecret(CLIENT_SECRET)
                .callback(REDIRECT_URI)
                .scope("public_profile")
                .state(state) //�ռ� ������ �������� ���� URL������ �����
                .build(FacebookLoginApi.instance());

        return oauthService.getAuthorizationUrl();
    }
    
    public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException{

        /* Callback���� ���޹��� ���������� �������� ���ǿ� ����Ǿ��ִ� ���� ��ġ�ϴ��� Ȯ�� */
        String sessionState = getSession(session);
        if(StringUtils.pathEquals(sessionState, state)){

            OAuth20Service oauthService = new ServiceBuilder()
                    .apiKey(CLIENT_ID)
                    .apiSecret(CLIENT_SECRET)
                    .callback(REDIRECT_URI)
                    .scope("public_profile")
                    .state(state)
                    .build(FacebookLoginApi.instance());

            /* Scribe���� �����ϴ� AccessToken ȹ�� ������� �׾Ʒ� Access Token�� ȹ�� */
            OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
            System.out.println(accessToken);
            return accessToken;
        }
        return null;
    }
    /* ���� ��ȿ�� ������ ���� ���� ������ */
    public String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    /* http session�� ������ ���� */
    private void setSession(HttpSession session,String state){
        session.setAttribute(SESSION_STATE, state);     
    }

    /* http session���� ������ �������� */ 
    private String getSession(HttpSession session){
        return (String) session.getAttribute(SESSION_STATE);
    }
    
     //Access Token�� �̿��Ͽ� ���̹� ����� ������ API�� ȣ�� 
    public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException{
System.out.println(REDIRECT_URI);
        OAuth20Service oauthService =new ServiceBuilder()
                .apiKey(CLIENT_ID)
                .scope("public_profile")
                .apiSecret(CLIENT_SECRET)
                .callback(REDIRECT_URI).build(FacebookLoginApi.instance());

            OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL, oauthService);
        oauthService.signRequest(oauthToken, request);
        Response response = request.send();
        return response.getBody();
    }

  
}
