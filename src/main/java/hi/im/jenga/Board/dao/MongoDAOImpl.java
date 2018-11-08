package hi.im.jenga.board.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDAOImpl implements MongoDAO{

    private final MongoTemplate mongoTemplate;


    public MongoDAOImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
