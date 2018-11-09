package hi.im.jenga.board.service;

import hi.im.jenga.board.dao.BoardDAO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class BoardServiceImpl implements BoardService {

	private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

	private final AES256Cipher aes256Cipher;
	private final BoardDAO dao;

	@Autowired
	public BoardServiceImpl(AES256Cipher aes256Cipher, BoardDAO dao) {
		this.aes256Cipher = aes256Cipher;
		this.dao = dao;
	}


	public String getBookMark() {
		
		FileIO fileIO = new FileIO("Bookmarks");
		String result = fileIO.InputBookMark();
		
		return result;

	}


	public void writeViewBlock(String session_iuid, BoardDTO boardDTO) {

		dao.writeViewBlock(boardDTO, session_iuid);
	}

	public void writeViewThumbImg(String bl_uid, String uploadName) {

		// 사진을 직접 안넣을 시 디폴트 이미지로 설정
		if(uploadName.equals("")){
			// 디폴트 이미지를 넣어준다
		}
		dao.writeViewThumbImg(bl_uid, uploadName);
	}

	public void writeViewTag(String bl_uid, String[] bt_name) {

		dao.writeViewTag(bl_uid, bt_name);
	}

	public HashMap modifyViewGET(String bl_uid) {
		return dao.modifyViewGET(bl_uid);
	}
}
