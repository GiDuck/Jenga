package hi.im.jenga.board.dto;

public class MongoDTO {

	String _blockid;
	String _refBoardId;
	Object _value;
	String name;

	@Override
	public String toString() {
		return "MongoDTO{" +
				"_blockid='" + _blockid + '\'' +
				", _refBoardId='" + _refBoardId + '\'' +
				", _value=" + _value +
				", name='" + name + '\'' +
				'}';
	}

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
