package alma.logoot.shared;

public class OpInsert extends Operation {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * caractere de l'ajout
	 */
	private Character content;

	public OpInsert(){
		
	}
	
	
	public void setContent(Character content) {
		this.content = content;
	}


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

	public String toString() {
		return "[INS, " + content + "," + position + "]";
	}

}
