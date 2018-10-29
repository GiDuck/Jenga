package hi.im.jenga.member.dto;

public class EmailMemberDTO {
    private String em_id;
    private String em_ref;  //FK -> mem_iuid
    private String em_pwd;
    private String em_akey;
    private String em_acheck;

    public String getEm_id() {
        return em_id;
    }

    public void setEm_id(String em_id) {
        this.em_id = em_id;
    }

    public String getEm_ref() {
        return em_ref;
    }

    public void setEm_ref(String em_ref) {
        this.em_ref = em_ref;
    }

    public String getEm_pwd() {
        return em_pwd;
    }

    public void setEm_pwd(String em_pwd) {
        this.em_pwd = em_pwd;
    }

    public String getEm_akey() {
        return em_akey;
    }

    public void setEm_akey(String em_akey) {
        this.em_akey = em_akey;
    }

    public String getEm_acheck() {
        return em_acheck;
    }

    public void setEm_acheck(String em_acheck) {
        this.em_acheck = em_acheck;
    }
}
