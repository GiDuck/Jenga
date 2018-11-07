package hi.im.jenga.board.dto;

import java.util.Arrays;
import java.util.Date;

public class BoardDTO {
    String bl_uid;                  // PK
    String bl_writer;               // FK (-> tbl_memInfo(mem_iuid)
    String bl_title;
    String bl_description;
    String bl_mainCtg;              // FK (-> tbl_mCategory(mctg_uid)
    String bl_smCtg;                // FK (-> tbl_sCategory(sctg_uid)
    String  bl_date;
/*
    Date bl_date;
*/
    String bl_objId;

    public String getBl_uid() {
        return bl_uid;
    }

    public void setBl_uid(String bl_uid) {
        this.bl_uid = bl_uid;
    }

    public String getBl_writer() {
        return bl_writer;
    }

    public void setBl_writer(String bl_writer) {
        this.bl_writer = bl_writer;
    }

    public String getBl_title() {
        return bl_title;
    }

    public void setBl_title(String bl_title) {
        this.bl_title = bl_title;
    }

    public String getBl_description() {
        return bl_description;
    }

    public void setBl_description(String bl_description) {
        this.bl_description = bl_description;
    }

    public String getBl_mainCtg() {
        return bl_mainCtg;
    }

    public void setBl_mainCtg(String bl_mainCtg) {
        this.bl_mainCtg = bl_mainCtg;
    }

    public String getBl_smCtg() {
        return bl_smCtg;
    }

    public void setBl_smCtg(String bl_smCtg) {
        this.bl_smCtg = bl_smCtg;
    }

    public String getBl_date() {
        return bl_date;
    }

    public void setBl_date(String bl_date) {
        this.bl_date = bl_date;
    }

    public String getBl_objId() {
        return bl_objId;
    }

    public void setBl_objId(String bl_objId) {
        this.bl_objId = bl_objId;
    }

}
