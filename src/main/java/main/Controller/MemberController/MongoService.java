package main.Controller.MemberController;

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
