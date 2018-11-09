package hi.im.jenga.board.service;

import hi.im.jenga.board.dao.MongoDAO;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoServiceImpl implements MongoService {

    @Autowired
    private MongoDAO mongoDAO;


    public void getAnyway(MemberDTO member, JSONObject json) {

        mongoDAO.getAnyway(member, json);

    }
}
