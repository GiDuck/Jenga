package hi.im.jenga.board.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileIO {
	
	public final String filePath = "C:\\Users\\YUJUN\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\";	// 북마크 파일있는 위치
	private FileReader reader;
	private BufferedReader buffer;
	private File file;
	
	
	
	
	
 	public FileIO() {
		super();
	}



	public FileIO(String fileName){

		file = new File(filePath + fileName);
		try {
			reader = new FileReader(file);
		buffer = new BufferedReader(reader);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}



	public String InputBookMark() {
		StringBuffer resultJSON = new StringBuffer();
		
		try {
		resultJSON.append("{");
	
		
 		while(buffer.read() != -1) {
 			
 			resultJSON.append(buffer.readLine());
 			
 		}
 		
 		resultJSON.append("}");
 		
 		
 		
		}catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}finally {
			
			
			try {
				buffer.close();
				reader.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
 		
 		
		
 		return resultJSON.toString();

		
		
	}

}
