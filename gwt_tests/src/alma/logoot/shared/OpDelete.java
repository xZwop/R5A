package alma.logoot.shared;

public class OpDelete extends Operation {

	public OpDelete(LogootIdContainer position) {
		this.position = position;
	}

	@Override
	public boolean isIns() {
		return true;
	}

	@Override
	public boolean isDel() {
		return false;
	}

}
