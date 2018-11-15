package hi.im.jenga.board.dao;

import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDAOImpl implements MongoDAO {


    private final MongoTemplate mongoTemplate;

    public MongoDAOImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public void getAnyway(MemberDTO memberDTO, JSONObject json) {
        MongoDTO mongoDTO = new MongoDTO();

        mongoDTO.set_value(json);


        System.out.println(mongoTemplate);
//        mongoTemplate.insert(mongoDTO,"jenga");
        mongoTemplate.insert(mongoDTO,"c_block");


    }

    public void writeViewBmks(String bl_uid) {
        MongoDTO mongoDTO = new MongoDTO();

        mongoDTO.set_refBoardId(bl_uid);
//        mongoDTO.set_value(json);

        mongoTemplate.insert(mongoDTO,"c_block");


    }

    public MongoDTO modifyViewGET(String key, String bl_uid) {

        Criteria criteria = new Criteria(key);
        criteria.is(bl_uid);

        Query query = new Query(criteria);

        return mongoTemplate.findOne(query, MongoDTO.class,"c_block");
    }
}
