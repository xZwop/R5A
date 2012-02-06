package alma.logoot.logootengine;

public class OpInsert extends Operation {
	private static final long serialVersionUID = 1L;

	
	public OpInsert(){
	}
	
	public OpInsert(LogootIdContainer position, Character content) {
		super("insert", position, content);
	}
}
