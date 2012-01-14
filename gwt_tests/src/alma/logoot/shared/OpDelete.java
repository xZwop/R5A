package alma.logoot.shared;

public class OpDelete extends Operation {

	public OpDelete(LogootIdContainer position) {
		this.position = position;
	}

	@Override
	public boolean isIns() {
		return false;
	}

	@Override
	public boolean isDel() {
		return true;
	}
	
	public String toString(){
		return "[DEL, "+position+"]";
	}
}
