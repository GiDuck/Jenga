package hi.im.jenga.Board.Service;

import org.springframework.stereotype.Service;

@Service
public class BoardService {

	
	public String getBookMark() {
		
		
		FileIO fileIO = new FileIO("Bookmarks");
		String result = fileIO.InputBookMark();
		
		return result;
		
		
		
	}
	
	public String getBookMarkFromHTML() {
		
		
		FileIO fileIO = new FileIO("bookmarks_18. 11. 29..html");
		String result = fileIO.InputHTMLBookMark();
		
		
		return result;
		
		
		
	}
	
	
}
