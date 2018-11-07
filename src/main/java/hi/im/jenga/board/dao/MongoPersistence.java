package hi.im.jenga.board.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoPersistence {

	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	public void getAnyway() {

		mongoTemplate.createCollection("hi");
		mongoTemplate.insert("asdf","hi");
		Criteria criteria = new Criteria("name").is("hi");
		System.out.println(criteria);
		Query query = new Query(criteria);
		System.out.println(query);
		String result = mongoTemplate.findOne(query, String.class, "hi");
		System.out.println("get Result... " + result);
	}
}
