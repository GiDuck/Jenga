package hi.im.jenga.board.dto;




public class MongoDTO {

	private String _blockid;
	private String _refBoardId;
	private Object _value;
	private String name;	// 테스트용 나중에 지움 ㅅㄱ

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
