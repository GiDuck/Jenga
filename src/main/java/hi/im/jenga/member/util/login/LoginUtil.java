package hi.im.jenga.member.util.login;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import com.github.scribejava.core.model.OAuth2AccessToken;

public interface LoginUtil {
    public String getAuthorizationUrl(HttpSession session);
    public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException;
    public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException;
    public String generateRandomString();

}
