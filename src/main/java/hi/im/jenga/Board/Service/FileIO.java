package hi.im.jenga.board.service;

import java.io.*;

/**
 *  파일 읽어오기
 *
 *  FileReader reader = new FileReader([파일경로]) : [파일 경로]에 있는 파일의 내용을 읽어온다.			/ 파일 문자 입력 라이브러리
 *  BufferedReader buffer = new BUfferedReader(reader) : [파일 경로]에 있는 파일의 내용을 읽어온다.		/ 버퍼를 이용한 입력 라이브러리
 *  buffer.readLine() : BufferedReader에 저장된 내용을 String형으로 한 줄씩 읽어온다. "\n", "\r"을 만날때 까지 읽어온다.
 *     - readLine() 메소드를 사용하면서 문자 읽기를 더 효율적으로 할 수 있게 됐다.
 *	   - 기존의 read() 메소드로 한 문자씩 읽어오는 것보다 한줄씩 읽어서 처리하기에 더 간편하다.
 *
 * */


public class FileIO {
	
	public final String filePath = "C:\\Users\\LG\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\";	// 북마크 파일있는 위치
	private FileReader reader;
	private BufferedReader buffer;
	private File file;
	
	
	
 	public FileIO() {
		super();
	}



	public FileIO(String fileName){

		//file = new File(filePath + fileName);
		try {


			/*reader = new FileReader(file);*/
		buffer = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+fileName),"UTF-8"));
		

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
				//reader.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}


		System.out.println(resultJSON.toString());

 		return resultJSON.toString();

		
		
	}

}
