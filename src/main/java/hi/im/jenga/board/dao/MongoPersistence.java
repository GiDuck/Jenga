package hi.im.jenga.board.dao;


import hi.im.jenga.board.dto.MongoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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

		System.out.println(mongoTemplate);
		mongoTemplate.insert(mongoDTO,"jenga");

	}
}
