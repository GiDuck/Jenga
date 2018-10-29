package hi.im.jenga.member.util.login;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class KakaoLoginUtil implements LoginUtil {

    /* 인증 */
    @Value("#{data['kakao.client_id']}")
    private String CLIENT_ID;
    @Value("#{data['kakao.redirect_uri']}")
    private String REDIRECT_URI;
    @Value("#{data['kakao.client_secret']}")
    private String CLIENT_SECRET;

    private final static String SESSION_STATE = "state_kakao";
    /*네이버 프로필 조회*/
    private final static String PROFILE_API_URL = "https://kapi.kakao.com/v2/user/me";
    private static final Logger logger = LoggerFactory.getLogger(KakaoLoginUtil.class);

    public String getAuthorizationUrl(HttpSession session) {

        /* 세션 유효성 검증을 위하여 난수를 생성 */
        String state = generateRandomString();
        /* 생성한 난수 값을 session에 저장 */
        setSession(session, state);

        /* Scribe에서 제공하는 인증 URL 생성 기능을 이용하여 네아로 인증 URL 생성 */
        OAuth20Service oauthService = new ServiceBuilder(CLIENT_ID)
                .callback(REDIRECT_URI)
                .apiSecret(CLIENT_SECRET)
                .state(state) //앞서 생성한 난수값을 인증 URL생성시 사용함
                .build(KakaoLoginApi.instance());

        return oauthService.getAuthorizationUrl();
    }

    public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException, InterruptedException, ExecutionException {

        return null;
    }

    public String getUserProfile(OAuth2AccessToken oauthToken) throws Exception {
        return "";
    }

    public String getAccessTokens(HttpSession session, String code, String state) {
        final String AUTH_HOST = "https://kauth.kakao.com";
        final String tokenRequestUrl = AUTH_HOST + "/oauth/token";


        HttpsURLConnection conn = null;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        InputStreamReader isr = null;

        try {
            final String params = String.format("grant_type=authorization_code&client_secret=%s&client_id=%s&redirect_uri=%s&code=%s",
                    CLIENT_SECRET, CLIENT_ID, REDIRECT_URI, code);


            String sessionState = getSession(session);
            if (StringUtils.pathEquals(sessionState, state)) {
                final URL url = new URL(tokenRequestUrl);

                conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(params);
                writer.flush();

                final int responseCode = conn.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + tokenRequestUrl);
                System.out.println("Post parameters : " + params);
                System.out.println("Response Code : " + responseCode);

                isr = new InputStreamReader(conn.getInputStream());
                reader = new BufferedReader(isr);
                final StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                System.out.println(buffer.toString());
                return buffer.toString();
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // clear resources
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ignore) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignore) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception ignore) {
                }
            }
        }
        return null;
    }

    /* 세션 유효성 검증을 위한 난수 생성기 */
    public String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    /* http session에 데이터 저장 */
    private void setSession(HttpSession session, String state) {
        session.setAttribute(SESSION_STATE, state);
    }

    /* http session에서 데이터 가져오기 */
    private String getSession(HttpSession session) {
        return (String) session.getAttribute(SESSION_STATE);
    }

    /* Access Token을 이용하여 네이버 사용자 프로필 API를 호출 */
    public String getUserProfiles(String oauthToken) {

        RestTemplate template = new RestTemplate();
        /*template = new RestTemplate();*/
        StringBuffer baseURL;
        String requestUri;
        ResponseEntity<String> response;
        System.out.println(oauthToken);

        HttpHeaders setHeader = new HttpHeaders();

        setHeader.set("Authorization", "bearer" + " " + oauthToken);
        setHeader.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        /*setHeader.set("Accept", "application/json");*/

        System.out.println("made header token : ... " + setHeader.getAccept());
        System.out.println("made header token : ... " + setHeader.getContentType());
        System.out.println("made header token : ... " + setHeader.toString());

        baseURL = new StringBuffer("https://kapi.kakao.com").append("/v2/user/me");
        requestUri = baseURL.toString();
        HttpEntity request1 = new HttpEntity(setHeader);


        response = template.exchange(requestUri, HttpMethod.POST, request1, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }


    public void logOut(String oauthToken) {
        RestTemplate template = new RestTemplate();
        /*template = new RestTemplate();*/
        StringBuffer baseURL;
        String requestUri;
        ResponseEntity<String> response;
        System.out.println(oauthToken);

        HttpHeaders setHeader = new HttpHeaders();

        setHeader.set("Authorization", "bearer" + " " + oauthToken);
        setHeader.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("made header token : ... " + setHeader.getAccept());
        System.out.println("made header token : ... " + setHeader.getContentType());
        System.out.println("made header token : ... " + setHeader.toString());

        baseURL = new StringBuffer("https://kapi.kakao.com").append("/v1/user/logout");
        requestUri = baseURL.toString();
        HttpEntity request1 = new HttpEntity(setHeader);


        response = template.exchange(requestUri, HttpMethod.POST, request1, String.class);
        System.out.println(response.getBody());

    }
}
