package hi.im.jenga.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hi.im.jenga.board.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private static Logger logger = Logger.getLogger(BoardController.class);
	
	@RequestMapping(value="/stackBlock")
	public String getWriteView(Model model) {
		
		
		String resultJSON = boardService.getBookMark();
		String resultHTML = boardService.getBookMarkFromHTML();
		model.addAttribute("resultJSON", resultJSON);
		model.addAttribute("resultHTML", resultHTML);

		
		return "editor/stackBoard/stackBlock";
	}
	
	@RequestMapping(value="/fileUpload", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile uploadFile) throws Exception {
		
		ResponseEntity<String> result;
		try {

		System.out.println("업로드 된 파일");
		System.out.println(uploadFile.getOriginalFilename());
		System.out.println(uploadFile.getSize());
		System.out.println(uploadFile.getBytes().toString());

		result = new ResponseEntity<String>(HttpStatus.OK);
		
		}catch(Exception e) {
			
			e.printStackTrace();
			
			result = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return result;

	}
	
	
	@PostMapping(value="/uploadBookmarks")
	public void uploadBookmark (@RequestPart("bookmark") String bookmark) {
		
		
		
		
		
		
	}

	
}
