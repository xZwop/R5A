package alma.logoot.logootengine;


import java.util.ArrayList;

/**
 * Ensemble de triplet définissant une position pour un caractère
 * 
 * @author Driz
 */
public class LogootIdContainer extends ArrayList<LogootIdentifier> implements
		Comparable<LogootIdContainer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8707580451647621697L;

	public LogootIdContainer() {
		super();
	}

	public LogootIdContainer(String s) {
		try {
			s = s.split("^[<]")[1];
			s = s.split("[>]$")[0];
		} catch (Exception e) {
			System.err.println("LogootIdContainer : Deserialization error.");
		}
		String[] splited = s.split("[>][<]");
		for ( int i = 0; i < splited.length; i++) {
			this.add(new LogootIdentifier(splited[i]));
		}
	}

	public String toString() {
		String result = "";
		for (LogootIdentifier l : this) {
			result += l.toString();
		}
		return result;
	}

	/**
	 * Def23 p59
	 */
	public int compareTo(LogootIdContainer o) {
		if (size() > 0 && o.size() > 0) {
			for (int i = 0; i < Math.min(size(), o.size()); i++) {
				int comp = get(i).compareTo(o.get(i));
				if (comp != 0)
					return comp;
			}
			if (size() > o.size())
				return 1;
			else if (size() < o.size())
				return -1;
		}
		return 0;
	}

	public boolean equals(LogootIdContainer o) {
		return this.compareTo(o) == 0;
	}

}
