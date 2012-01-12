package alma.logoot.shared;

public abstract class Operation {
	LogootIdContainer position;
	
	abstract public boolean isIns();
	abstract public boolean isDel();
	
	public LogootIdContainer getPosition(){
		return position;
	}
}
