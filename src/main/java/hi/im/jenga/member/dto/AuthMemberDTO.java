package hi.im.jenga.member.dto;

public class AuthMemberDTO {
    private String am_id;
    private String am_pwd;
    private String am_key;

    public String getAm_id() {
        return am_id;
    }

    public void setAm_id(String am_id) {
        this.am_id = am_id;
    }

    public String getAm_pwd() {
        return am_pwd;
    }

    public void setAm_pwd(String am_pwd) {
        this.am_pwd = am_pwd;
    }

    public String getAm_key() {
        return am_key;
    }

    public void setAm_key(String am_key) {
        this.am_key = am_key;
    }
}
