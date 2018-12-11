package hi.im.jenga.board.dto;

import java.util.Date;

public class BlockPathDTO {
    private String bp_iuid;
    private String bp_path;
    private Date bp_date;
    private String bp_browstype;
    private String bp_booktype;

    public String getBp_iuid() {
        return bp_iuid;
    }

    public void setBp_iuid(String bp_iuid) {
        this.bp_iuid = bp_iuid;
    }

    public String getBp_path() {
        return bp_path;
    }

    public void setBp_path(String bp_path) {
        this.bp_path = bp_path;
    }

    public Date getBp_date() {
        return bp_date;
    }

    public void setBp_date(Date bp_date) {
        this.bp_date = bp_date;
    }

    public String getBp_browstype() {
        return bp_browstype;
    }

    public void setBp_browstype(String bp_browstype) {
        this.bp_browstype = bp_browstype;
    }

    public String getBp_booktype() {
        return bp_booktype;
    }

    public void setBp_booktype(String bp_booktype) {
        this.bp_booktype = bp_booktype;
    }
}
