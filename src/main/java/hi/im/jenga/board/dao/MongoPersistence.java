package hi.im.jenga.board.dao;


import hi.im.jenga.board.dto.MongoDTO;
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
		MongoDTO mongoDTO = new MongoDTO();
		mongoDTO.setName("Asdfgh");

		mongoTemplate.insert(mongoDTO, "hi");

		/*Criteria criteria = new Criteria("name").is("ㅎㅇㅂㅇ");
		Query query = new Query(criteria);
		String result = mongoTemplate.findOne(query, String.class, "hi");
		System.out.println("get Result... " + result);*/
	}
}
