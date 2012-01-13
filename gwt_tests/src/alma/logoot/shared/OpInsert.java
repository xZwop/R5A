package alma.logoot.shared;

public class OpInsert extends Operation {
	Character content;

	public OpInsert(LogootIdContainer position, Character content) {
		this.position = position;
		this.content = content;
	}

	@Override
	public boolean isIns() {
		return true;
	}

	@Override
	public boolean isDel() {
		return false;
	}

	public Character getContent() {
		return content;
	}

}