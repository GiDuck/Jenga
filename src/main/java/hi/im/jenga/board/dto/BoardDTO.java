package hi.im.jenga.board.dto;

import java.util.Arrays;

public class BoardDTO {

    private String bl_uid;                  // PK UUID로 생성
    private String bl_writer;               // FK (-> tbl_memInfo(mem_iuid)
    private String bl_title;
    private String bl_introduce;
    private String bl_mainCtg;              // FK (-> tbl_mCategory(mctg_uid)
    private String bl_smCtg;                // FK (-> tbl_sCategory(sctg_uid)
    private String bl_description;          // BLOB 타입
    private String [] bt_name;
    private Long bl_date;
    private String bl_objId;

    @Override
    public String toString() {
        return "BoardDTO{" +
                "bl_uid='" + bl_uid + '\'' +
                ", bl_writer='" + bl_writer + '\'' +
                ", bl_title='" + bl_title + '\'' +
                ", bl_introduce='" + bl_introduce + '\'' +
                ", bl_mainCtg='" + bl_mainCtg + '\'' +
                ", bl_smCtg='" + bl_smCtg + '\'' +
                ", bl_description='" + bl_description + '\'' +
                ", bt_name=" + Arrays.toString(bt_name) +
                ", bl_date=" + bl_date +
                ", bl_objId='" + bl_objId + '\'' +
                '}';
    }

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

    public String getBl_introduce() {
        return bl_introduce;
    }

    public void setBl_introduce(String bl_introduce) {
        this.bl_introduce = bl_introduce;
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

    public String getBl_description() {
        return bl_description;
    }

    public void setBl_description(String bl_description) {
        this.bl_description = bl_description;
    }

    public String[] getBt_name() {
        return bt_name;
    }

    public void setBt_name(String[] bt_name) {
        this.bt_name = bt_name;
    }

    public Long getBl_date() {
        return bl_date;
    }

    public void setBl_date(Long bl_date) {
        this.bl_date = bl_date;
    }

    public String getBl_objId() {
        return bl_objId;
    }

    public void setBl_objId(String bl_objId) {
        this.bl_objId = bl_objId;
    }
}
