package hi.im.jenga.board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hi.im.jenga.board.dao.BoardDAO;
import hi.im.jenga.board.dto.BlockPathDTO;
import hi.im.jenga.board.dto.BoardDTO;
import hi.im.jenga.board.dao.BoardDAO;
import hi.im.jenga.member.util.cipher.AES256Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {

	private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

	private final BoardDAO dao;
	private final MongoService mongoService;
	private final AES256Cipher aes256Cipher;

	@Autowired
	public BoardServiceImpl(BoardDAO dao, MongoService mongoService, AES256Cipher aes256Cipher) {
		this.dao = dao;
		this.mongoService = mongoService;
		this.aes256Cipher = aes256Cipher;
	}


	@Transactional
	public void writeViewBlock(BoardDTO boardDTO, String uploadName, String bl_bookmarks) {

		mongoService.writeViewBmks(boardDTO.getBl_uid(), bl_bookmarks);		// bl_objId가 있어야지 block에 값을 넣을 수 있다.
		boardDTO.setBl_objId(mongoService.getObjId("_refBoardId",boardDTO.getBl_uid()));
		logger.info("북마크 블록의 uid "+boardDTO.getBl_objId());


		dao.writeViewBlock(boardDTO);
		dao.writeViewReadCount(boardDTO.getBl_uid());


		// 사진을 직접 안넣을 시 디폴트 이미지로 설정
		if(uploadName.equals("")) {
			// 디폴트 이미지를 넣어준다
			uploadName = "D:\\jengaResource\\default\\noImage.png";
		}
			dao.writeViewThumbImg(boardDTO.getBl_uid(), uploadName);



		dao.writeViewTag(boardDTO.getBl_uid(), boardDTO.getBt_name());


	}

	@Transactional
	public Map<String, Object> getModifyBlock(String bl_uid) throws JsonProcessingException {

		Map<String, Object> map = dao.getModifyBlock(bl_uid);

		List<String> list = dao.getBoardDetailTags(bl_uid);
		ObjectMapper mapper = new ObjectMapper();
		String tagJSON = mapper.writeValueAsString(list);

		map.put("tag", tagJSON);

		String bookmarks = mongoService.getView("_refBoardId", bl_uid);
		map.put("bookmarks", bookmarks);

		map.put("bti_url", dao.getUploadName(bl_uid));

		return map;
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


	@Transactional
	public Map<String, Object> getView(String bl_uid) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
		dao.getAddReadCount(bl_uid);	// 조회수 + 1
		Map<String, Object> map = dao.getBoardDetailBlock(bl_uid);

		map.put("mem_nick", aes256Cipher.AES_Decode(String.valueOf(map.get("mem_nick"))));
		map.put("mem_introduce", aes256Cipher.AES_Decode(String.valueOf(map.get("mem_introduce"))));
		map.put("mem_profile", aes256Cipher.AES_Decode(String.valueOf(map.get("mem_profile"))));

		System.out.println(map.get("bl_date"));
		System.out.println(map.get("blrc_count"));

		List<String> list = dao.getBoardDetailTags(bl_uid);
		ObjectMapper mapper = new ObjectMapper();
		String tagJSON = mapper.writeValueAsString(list);
		map.put("tag", tagJSON);

		map.put("likes", dao.likeCount(bl_uid));

		String bookmarks = mongoService.getView("_refBoardId", bl_uid);
		map.put("bookmarks", bookmarks);


		return map;
	}








	public void likeCheck(String bl_iuid, String session_mem_iuid) { dao.likeCheck(bl_iuid, session_mem_iuid); }

	public String getBookMarkFromHTML(String session_iuid) {
		String attachPath  = "Y:\\go\\Jenga\\";
		String fileFullName = dao.getBookMarkFromHTML(session_iuid);
//		String 하나 더만들어서 비교
		if(fileFullName != null) {
			logger.info("로컬에 있는 북마크 경로는 "+attachPath+fileFullName);
			FileIO fileIO = new FileIO(attachPath + fileFullName);
			String result = fileIO.InputHTMLBookMark();
			return result;
		}
		return "notExist";
	}

	public Map<String, List<String>> getCategoryName() {
		return dao.getCategoryName();
	}

	public String transCtgUID(String bl_smCtg, String flag) {
		return dao.transCtgUID(bl_smCtg, flag);
	}

	public List<BoardDTO> search(String search, String search_check, String session_iuid) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
		if(session_iuid != null){
			dao.setSearchKeyword(search,session_iuid); //검색 워드 저장
		}
		if(search_check.equals("name")){
			search = aes256Cipher.AES_Encode(search);
			return dao.searchName(search);
		}else if(search_check.equals("tag")){
			return dao.searchTag(search);
		}else{
			String[] splitsearch = search.split(" ");

			List<String> list = new ArrayList<String>();
			for(int i = 0; i<splitsearch.length; i++){
				list.add(splitsearch[i]);
			}
			return dao.searchContents(list);
		}
	}

	public void follow(String bl_writer, String session_iuid) {
		dao.follow(bl_writer, session_iuid);
	}

	public void unFollow(String bl_writer, String session_iuid) {
		dao.unFollow(bl_writer, session_iuid);
	}

	public List<BoardDTO> getFollowerBoard(String my_iuid) {
		return dao.getFollowerBoard(my_iuid);
	}

	public int likeCount(String bl_iuid) {
		return dao.likeCount(bl_iuid);
	}

	public List<BoardDTO> getMyBlock(String my_iuid) {
		return dao.getMyBlock(my_iuid);
	}

	public List<String> searchImg(String search, String search_check) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
		if(search_check.equals("name")){
			logger.info("변경 전" + search);
			search = aes256Cipher.AES_Encode(search);
			logger.info("변경후 아이디"+search);
			return dao.searchImgName(search);
		}else if(search_check.equals("tag")){
			dao.searchImgTag(search);
		}else{
			String[] splitsearch = search.split(" ");
			List<String> list = new ArrayList<String>();
			for(int i = 0; i<splitsearch.length; i++){
				list.add(splitsearch[i]);
				logger.info("add 했음");
			}
			dao.searchImgContents(list);
		}
		return null;
	}


}
