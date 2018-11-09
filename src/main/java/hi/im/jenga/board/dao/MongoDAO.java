package hi.im.jenga.board.dao;

import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;

public interface MongoDAO {

    void getAnyway(MemberDTO memberDTO, JSONObject json);
}
