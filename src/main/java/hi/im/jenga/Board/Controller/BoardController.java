package hi.im.jenga.Board.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

	
	@RequestMapping(value="/stackBlock")
	public String getWriteView() {

		
		return "editor/stackBoard/stackBlock";
	}
	
	
}
