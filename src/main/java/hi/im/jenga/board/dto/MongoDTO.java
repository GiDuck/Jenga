package hi.im.jenga.board.dto;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


public class MongoDTO {

	private String _blockid;
	private String _refBoardId;
	private Object _value;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String get_blockid() {
		return _blockid;
	}

	public void set_blockid(String _blockid) {
		this._blockid = _blockid;
	}

	public String get_refBoardId() {
		return _refBoardId;
	}

	public void set_refBoardId(String _refBoardId) {
		this._refBoardId = _refBoardId;
	}

	public Object get_value() {
		return _value;
	}

	public void set_value(Object _value) {
		this._value = _value;
	}

}
