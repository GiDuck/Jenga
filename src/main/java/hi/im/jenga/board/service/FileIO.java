package hi.im.jenga.board.service;

import java.io.*;

public class FileIO {
	
	public final String filePath = "C:\\Users\\LG\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\";	// 북마크 파일있는 위치
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
		buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		
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
