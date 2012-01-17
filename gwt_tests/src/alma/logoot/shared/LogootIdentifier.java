package alma.logoot.shared;

import java.io.Serializable;

/**
 * Triplet representant une identifiant dans la base 
 * @author driz
 *
 */
public class LogootIdentifier implements Comparable<LogootIdentifier>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * digit dans la base [1, BASE[
	 */
	private int digit;
	
	/**
	 * Identifiant de site
	 */
	private int identifier;
	
	/**
	 * Valeur d'horloge
	 */
	private int clock;
	
	public LogootIdentifier(int digit, int identifier, int clock) {
		this.digit = digit;
		this.identifier = identifier;
		this.clock = clock;
	}
	
	public LogootIdentifier(){
	}

	public int getDigit() {
		return digit;
	}

	public void setDigit(int i) {
		this.digit = i;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int s) {
		this.identifier = s;
	}

	public int getClock() {
		return clock;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	/**
	 * def 23 (p59)
	 */
	public int compareTo(LogootIdentifier o) {
		if(this.digit>o.getDigit()){
			return 1;
		}else if(this.digit<o.getDigit()){
			return -1;
		}
		if(this.identifier>o.getIdentifier()){
			return 1;
		}else if(this.identifier<o.getIdentifier()){
			return -1;
		}
		if(this.clock>o.getClock()){
			return 1;
		}else if(this.clock<o.getClock()){
			return -1;
		}
		return 0;
	}
	
	public String toString() {
		return "<"+digit+", "+identifier+", "+clock+">";
	}
	
	
	
}
