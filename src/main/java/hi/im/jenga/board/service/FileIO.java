package hi.im.jenga.board.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {

	public final String filePath = "C:\\Users\\gdtbg\\Documents\\Stoarge\\";
	private FileReader reader;
	private BufferedReader buffer;
	private File file;

	public FileIO() {
		super();
	}

	public FileIO(String fileName) {

		file = new File(filePath + fileName);
		try {
			reader = new FileReader(file);
			buffer = new BufferedReader(reader);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String InputHTMLBookMark() {

		StringBuffer resultHTML = new StringBuffer();

		try {

			while (buffer.read() != -1) {

				resultHTML.append(buffer.readLine());

			}

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		} finally {

			try {
				buffer.close();
				reader.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return resultHTML.toString();

	}

	public String InputBookMark() {
		StringBuffer resultJSON = new StringBuffer();

		try {
			resultJSON.append("{");

			while (buffer.read() != -1) {

				resultJSON.append(buffer.readLine());

			}

			resultJSON.append("}");

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		} finally {

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

	public void outputBookMark(String bookmark, String fileName) {
		File file = null;
		FileWriter out = null;
		BufferedWriter bufferWriter = null;
		try {
			file = new File(fileName);
			out = new FileWriter(file, true);
			bufferWriter = new BufferedWriter(out);
			bufferWriter.write(bookmark);

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				out.close();
				bufferWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
