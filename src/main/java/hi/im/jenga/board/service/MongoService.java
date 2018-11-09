package hi.im.jenga.board.service;


import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;

public interface MongoService {

    void getAnyway(MemberDTO member, JSONObject json);
}
