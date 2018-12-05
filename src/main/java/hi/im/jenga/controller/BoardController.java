package hi.im.jenga.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hi.im.jenga.board.service.BoardService;
import hi.im.jenga.vo.BoardDTO;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private static Logger logger = Logger.getLogger(BoardController.class);
	
	@RequestMapping(value="/stackBlock")
	public String getWriteView(Model model) throws JsonProcessingException {
		
		Map<String, List<String>> category = new HashMap<String, List<String>>();
		
		List<String> sport = new ArrayList<String>();
		sport.add("축구");
		sport.add("농구");
		sport.add("스키");
		sport.add("수영");
		category.put("스포츠", sport);
		
		List<String> social = new ArrayList<String>();
		social.add("시사");
		social.add("정치");
		social.add("국제");
		social.add("수영");
		category.put("사회", social);
		
		List<String> life = new ArrayList<String>();
		life.add("요리");
		life.add("독서");
		life.add("패션");
		category.put("라이프", life);
		
		ObjectMapper mapper = new ObjectMapper();
		
		String categoryJSON = mapper.writeValueAsString(category);
		

		String resultHTML = boardService.getBookMarkFromHTML();
		model.addAttribute("resultHTML", resultHTML);
		model.addAttribute("category", categoryJSON);

		
		return "editor/stackBoard/stackBlock";
	}
	
	
	//Block Upload 컨트롤러
	@RequestMapping(value="/uploadBlock", method=RequestMethod.POST, produces="multipart/form-data; charset=utf-8")
	@ResponseBody
	public String stackBlock(@RequestPart("image") MultipartFile image, @ModelAttribute BoardDTO board) {
		
		System.out.println("들어온 값");
		System.out.println(image);
		
		System.out.println(board.toString());
		
		return "success";
		
		
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
