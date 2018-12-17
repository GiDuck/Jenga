package hi.im.jenga.board.dto;


import org.springframework.data.annotation.Id;

public class MongoDTO {

	@Id
	private String _blockId;
	private String _refBoardId;
	private Object _value;

	public String get_blockId() {
		return _blockId;
	}

	public void set_blockId(String _blockId) {
		this._blockId = _blockId;
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
