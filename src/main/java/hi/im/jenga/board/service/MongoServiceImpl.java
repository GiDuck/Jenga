package hi.im.jenga.board.service;

import hi.im.jenga.board.dao.MongoDAO;
import hi.im.jenga.board.dto.MongoDTO;
import hi.im.jenga.member.dto.MemberDTO;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MongoServiceImpl implements MongoService {


    private final MongoDAO dao;

    public MongoServiceImpl(MongoDAO dao) {
        this.dao = dao;
    }

    public void getAnyway(MemberDTO member, JSONObject json) { dao.getAnyway(member, json); }

    public MongoDTO modifyViewGET(String key, String bl_uid) {
        return dao.modifyViewGET(key, bl_uid);
    }

    public void writeViewBmks(String bl_uid, String bl_bookmarks) { dao.writeViewBmks(bl_uid, bl_bookmarks); }

    public MongoDTO getView(String key, String bl_uid) { return dao.getView(key, bl_uid); }

    public String getObjId(String key, String bl_uid) { return dao.getObjId(key, bl_uid); }
}
