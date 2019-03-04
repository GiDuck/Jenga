package hi.im.jenga.member.dto;

public class SocialMemberDTO {
    private String sm_id;
    private String sm_ref; // FK -> mem_iuid
    private String sm_type;

    public String getSm_id() {
        return sm_id;
    }

    public void setSm_id(String sm_id) {
        this.sm_id = sm_id;
    }

    public String getSm_ref() {
        return sm_ref;
    }

    public void setSm_ref(String sm_ref) {
        this.sm_ref = sm_ref;
    }

    public String getSm_type() {
        return sm_type;
    }

    public void setSm_type(String sm_type) {
        this.sm_type = sm_type;
    }

    @Override
    public String toString() {
        return "SocialMemberDTO{" +
                "sm_id='" + sm_id + '\'' +
                ", sm_ref='" + sm_ref + '\'' +
                ", sm_type='" + sm_type + '\'' +
                '}';
    }
}
