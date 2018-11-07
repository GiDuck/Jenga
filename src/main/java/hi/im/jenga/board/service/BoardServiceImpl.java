package hi.im.jenga.board.service;

import hi.im.jenga.board.dao.BoardDAO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


	public void writeViewPOST(String session_iuid, BoardDTO boardDTO) throws Exception {

//		String decode_s_iuid = aes256Cipher.AES_Decode(session_iuid);
		logger.info("뭐야 너 "+boardDTO.getBl_smCtg());
		dao.writeViewPOST(boardDTO, session_iuid);
	}
}
