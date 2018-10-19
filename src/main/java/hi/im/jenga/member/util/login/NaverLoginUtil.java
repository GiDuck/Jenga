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

public class NaverLoginUtil implements LoginUtil {
        /* ���� */
        @Value("#{data['naver.client_id']}")
        private  String CLIENT_ID;
        @Value("#{data['naver.client_secret']}")
        private  String CLIENT_SECRET;
        @Value("#{data['naver.redirect_uri']}")
        private  String REDIRECT_URI;
        private final static String SESSION_STATE = "states";
        /*���̹� ������ ��ȸ*/
        private final static String PROFILE_API_URL ="https://openapi.naver.com/v1/nid/me";
      
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
                    .state(state) //�ռ� ������ �������� ���� URL������ �����
                    .build(NaverLoginApi.instance());

            return oauthService.getAuthorizationUrl();
        }

        /* ���̹����̵�� Callback ó�� ��  AccessToken ȹ�� Method */
        public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException{
            System.out.println("��������ū");
            /* Callback���� ���޹��� ���������� �������� ���ǿ� ����Ǿ��ִ� ���� ��ġ�ϴ��� Ȯ�� */
            String sessionState = getSession(session);
            System.out.println(sessionState);
            System.out.println(state);
            if(StringUtils.pathEquals(sessionState, state)){

                OAuth20Service oauthService = new ServiceBuilder()
                        .apiKey(CLIENT_ID)
                        .apiSecret(CLIENT_SECRET)
                        .callback(REDIRECT_URI)
                        .state(state)
                        .build(NaverLoginApi.instance());

                /* Scribe���� �����ϴ� AccessToken ȹ�� ������� �׾Ʒ� Access Token�� ȹ�� */
                OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
                System.out.println("������ ��ū"+accessToken.getAccessToken());
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
        /* Access Token�� �̿��Ͽ� ���̹� ����� ������ API�� ȣ�� */
        public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException{

            OAuth20Service oauthService =new ServiceBuilder()
                    .apiKey(CLIENT_ID)
                    .apiSecret(CLIENT_SECRET)
                    .callback(REDIRECT_URI).build(NaverLoginApi.instance());

                OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL, oauthService);
                System.out.println("����"+request);
                System.out.println("��������ū"+oauthToken);
                System.out.println(request);
            oauthService.signRequest(oauthToken, request);
            Response response = request.send();
            return response.getBody();
        }
}
