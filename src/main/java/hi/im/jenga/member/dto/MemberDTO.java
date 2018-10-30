package hi.im.jenga.member.dto;

import java.util.Date;

public class MemberDTO {
    private String mem_iuid;
    private String mem_nick;
    private String mem_profile;
    private int mem_level;
    private Date mem_joinDate;

    @Override
    public String toString() {
        return "MemberDTO{" +
                "mem_iuid='" + mem_iuid + '\'' +
                ", mem_nick='" + mem_nick + '\'' +
                ", mem_profile='" + mem_profile + '\'' +
                ", mem_level=" + mem_level +
                ", mem_joinDate=" + mem_joinDate +
                '}';
    }

    public String getMem_iuid() {
        return mem_iuid;
    }

    public void setMem_iuid(String mem_iuid) {
        this.mem_iuid = mem_iuid;
    }

    public String getMem_nick() {
        return mem_nick;
    }

    public void setMem_nick(String mem_nick) {
        this.mem_nick = mem_nick;
    }

    public String getMem_profile() {
        return mem_profile;
    }

    public void setMem_profile(String mem_profile) {
        this.mem_profile = mem_profile;
    }

    public int getMem_level() {
        return mem_level;
    }

    public void setMem_level(int mem_level) {
        this.mem_level = mem_level;
    }

    public Date getMem_joinDate() {
        return mem_joinDate;
    }

    public void setMem_joinDate(Date mem_joinDate) {
        this.mem_joinDate = mem_joinDate;
    }
}
