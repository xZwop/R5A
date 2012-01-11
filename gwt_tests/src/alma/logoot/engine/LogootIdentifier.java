package alma.logoot.engine;

public class LogootIdentifier implements Comparable<LogootIdentifier>{
	
	/**
	 * Identifiant dans la base
	 */
	private int i;
	
	/**
	 * Identifiant de site
	 */
	private int s;
	
	/**
	 * Valeur d'horloge
	 */
	private int r;
	
	public LogootIdentifier(int i, int s, int r) {
		this.i = i;
		this.s = s;
		this.r = r;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	/**
	 * These def 23 (p59)
	 */
	public int compareTo(LogootIdentifier o) {
		if(this.i>o.getI()){
			return 1;
		}else if(this.i<o.getI()){
			return -1;
		}
		if(this.s>o.getS()){
			return 1;
		}else if(this.s<o.getS()){
			return -1;
		}
		if(this.r>o.getR()){
			return 1;
		}else if(this.r<o.getR()){
			return -1;
		}
		return 0;
	}
	
	
	
	
}
