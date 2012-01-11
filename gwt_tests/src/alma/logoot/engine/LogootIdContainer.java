package alma.logoot.engine;

import java.util.ArrayList;

public class LogootIdContainer {
	private ArrayList<LogootIdentifier> chaine;
	
	public LogootIdContainer(){}

	public ArrayList<LogootIdentifier> getChaine() {
		return chaine;
	}

	public ArrayList<LogootIdentifier> getChaine1() {
		return this.chaine;
	};
	
	public void add(LogootIdentifier l){
		this.chaine.add(l);
	}
	
	public int size(){
		return this.chaine.size();
	}
	
	
}
