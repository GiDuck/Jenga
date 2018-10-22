package hi.im.jenga.member.dto;

public class EmailMemberDTO {
    private String em_id;
    private String em_ref;  //FK -> mem_iuid
    private String em_pwd;

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
}
