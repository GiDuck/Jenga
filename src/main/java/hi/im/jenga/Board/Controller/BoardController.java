package hi.im.jenga.Board.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import hi.im.jenga.Board.Service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value="/stackBlock")
	public String getWriteView(Model model) {
		
		
		String resultJSON = boardService.getBookMark();
		model.addAttribute("resultJSON", resultJSON);

		
		return "editor/stackBoard/stackBlock";
	}
	
	
}
