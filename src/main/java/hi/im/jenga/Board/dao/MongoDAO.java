package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;

public interface MongoDAO {

    void getAnyway(MemberDTO memberDTO, JSONObject json);

    MongoDTO modifyViewGET(String key, String bl_uid);

    void writeViewBmks(String bl_uid, String bl_bookmarks);

    String getView(String key, String bl_uid);

    String getObjId(String key, String bl_uid);

    void modifyViewPOST(String key, String bl_uid, String bl_bookmarks);

    void deleteBlock(String key, String bl_uid);
}
