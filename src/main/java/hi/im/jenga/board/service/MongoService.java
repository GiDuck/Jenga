package hi.im.jenga.board.service;

import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;

public interface MongoService {
    void getAnyway(MemberDTO member, JSONObject json);

    MongoDTO modifyViewGET(String key, String bl_uid);

    void writeViewBmks(String bl_uid, String bl_bookmarks);

    MongoDTO getView(String key, String bl_uid);

    String getObjId(String key, String bl_uid);
}
