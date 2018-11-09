package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDAOImpl implements MongoDAO {


    @Autowired
    private MongoTemplate mongoTemplate;


    public void getAnyway(MemberDTO memberDTO, JSONObject json) {
        MongoDTO mongoDTO = new MongoDTO();
        mongoDTO.setName("Asdfgh");
        mongoDTO.set_value(json);

        System.out.println(mongoTemplate);
        mongoTemplate.insert(mongoDTO,"jenga");


    }
}
