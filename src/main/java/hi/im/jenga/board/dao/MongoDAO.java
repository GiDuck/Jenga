package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;

public interface MongoDAO {

    void getAnyway(MemberDTO memberDTO, JSONObject json);

    MongoDTO modifyViewGET(String key, String bl_uid);

    void writeViewBmks(String bl_uid);
}
