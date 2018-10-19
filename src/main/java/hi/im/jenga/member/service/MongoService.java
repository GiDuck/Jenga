package hi.im.jenga.member.service;

import hi.im.jenga.member.dao.MongoPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoService {

	
	@Autowired
	private MongoPersistence mongoDAO;
	
	public void getAnyway() {
	
		mongoDAO.getAnyway();
	
	}
	
	
}
