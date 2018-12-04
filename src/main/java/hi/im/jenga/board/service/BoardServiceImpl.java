package hi.im.jenga.board.service;

import hi.im.jenga.board.dao.BoardDAO;
import hi.im.jenga.board.dto.BlockPathDTO;
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

	private final BoardDAO dao;

	@Autowired
	public BoardServiceImpl(AES256Cipher aes256Cipher, BoardDAO dao) {
		this.dao = dao;
	}

	public void writeViewBlock(String session_iuid, BoardDTO boardDTO) {

		dao.writeViewBlock(boardDTO, session_iuid);
		dao.writeViewReadCount(boardDTO.getBl_uid());
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

	public void modifyViewPOST(BoardDTO boardDTO, String uploadName, String[] bt_name) {

		// 수정안했으면
		if(uploadName.equals("")){
			uploadName = dao.getUploadName(boardDTO.getBl_uid());
		}
		dao.modifyViewPOST(boardDTO, uploadName, bt_name);
	}

	public void addBmksPath(String session_iuid, BlockPathDTO blockPathDTO) {
//		회원이 북마크를 업로드했는지 check  없으면 INSERT / 있으면 UPDATE
		String check = dao.checkBmksPath(session_iuid);

		if(check.equals("X")) {
			dao.insertBmksPath(session_iuid, blockPathDTO);
			return;
		}else if(check.equals("O")){
			dao.updateBmksPath(session_iuid, blockPathDTO);
			return;
		}
	}

	public int deleteBlock(String bl_uid) {
		return dao.deleteBlock(bl_uid);
	}

	public HashMap getView(String bl_uid) { return dao.getView(bl_uid); }

	public void likeCheck(String bl_iuid, String session_mem_iuid) { dao.likeCheck(bl_iuid, session_mem_iuid); }

	public String getBookMarkFromHTML(String session_iuid) {

		String fileFullName = dao.getBookMarkFromHTML(session_iuid);

		FileIO fileIO = new FileIO(fileFullName);
		String result = fileIO.InputHTMLBookMark();

		return result;
	}


}
