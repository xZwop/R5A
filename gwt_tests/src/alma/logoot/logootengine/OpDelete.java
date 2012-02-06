package alma.logoot.logootengine;

public class OpDelete extends Operation {

	private static final long serialVersionUID = 1L;
	
	public OpDelete(){
	}
	
	public OpDelete(LogootIdContainer position) {
		super("delete", position);
	}
}
