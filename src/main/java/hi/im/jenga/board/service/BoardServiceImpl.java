package hi.im.jenga.board.service;

import hi.im.jenga.board.dao.BoardDAO;
import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {

	private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

	private final BoardDAO dao;
	private final MongoService mongoService;

	@Autowired
	public BoardServiceImpl(BoardDAO dao, MongoService mongoService) {
		this.dao = dao;
		this.mongoService = mongoService;
	}


	@Transactional
	public void writeViewBlock(BoardDTO boardDTO, String uploadName, String bl_bookmarks) {

		mongoService.writeViewBmks(boardDTO.getBl_uid(), bl_bookmarks);		// bl_objId가 있어야지 block에 값을 넣을 수 있다.
		boardDTO.setBl_objId(mongoService.getObjId("_refBoardId",boardDTO.getBl_uid()));
		logger.info("북마크 블록의 uid "+boardDTO.getBl_objId());


		dao.writeViewBlock(boardDTO);
		dao.writeViewReadCount(boardDTO.getBl_uid());


		// 사진을 직접 안넣을 시 디폴트 이미지로 설정
		if(uploadName.equals("")){
			// 디폴트 이미지를 넣어준다
			dao.writeViewThumbImg(boardDTO.getBl_uid(), "없음");
		}else{

			dao.writeViewThumbImg(boardDTO.getBl_uid(), uploadName);
		}



		dao.writeViewTag(boardDTO.getBl_uid(), boardDTO.getBt_name());


	}

	public HashMap modifyViewGET(String bl_uid) {
		return dao.modifyViewGET(bl_uid);
	}

	@Transactional
	public void modifyViewPOST(BoardDTO boardDTO, String uploadName, String bl_bookmarks) {

		dao.modifyViewBoard(boardDTO);

		// 수정안했으면
		if(uploadName.equals("")){
			uploadName = dao.getUploadName(boardDTO.getBl_uid());
		}

		dao.modifyViewThumbImg(boardDTO, uploadName);

		dao.modifyViewTag(boardDTO);

//		mongoDTO.set_value(bl_bookmarks);를 해서 mongoDTO를 넘길까 음
		mongoService.modifyViewPOST("_refBoardId", boardDTO.getBl_uid(), bl_bookmarks);
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

	@Transactional
	public int deleteBlock(String bl_uid) {
//		MongoDB쪽을 먼저 삭제 / 반대면 bl_uid가 없어서 에러
		mongoService.deleteBlock("_refBoardId", bl_uid);

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

	public Map<String, List<String>> getCategoryName() {
		return dao.getCategoryName();
	}

	public String transCtgUID(String bl_smCtg, String flag) {
		return dao.transCtgUID(bl_smCtg, flag);
	}

	public List<BoardDTO> search(String search, String search_check, String session_iuid) {
		dao.setSearchKeyword(search,session_iuid); //검색 워드 저장
		if(search_check.equals("name")){
			return dao.searchName(search);
		}else if(search_check.equals("tag")){
			return dao.searchTag(search);
		}else{
			return dao.searchContents(search);
		}
	}


}
