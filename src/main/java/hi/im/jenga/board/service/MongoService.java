package hi.im.jenga.board.service;

import hi.im.jenga.board.dao.MongoPersistence;
import hi.im.jenga.board.dto.MongoDTO;
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
