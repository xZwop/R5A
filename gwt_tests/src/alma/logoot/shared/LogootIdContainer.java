package alma.logoot.shared;

import java.util.ArrayList;

public class LogootIdContainer implements Comparable<LogootIdContainer>{
	private ArrayList<LogootIdentifier> chaine;
	
	public LogootIdContainer(){
		chaine=new ArrayList<LogootIdentifier>();
	}

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
	
	public String toString() {
		String result = "";
			for ( LogootIdentifier l : chaine ) {
			result += l.toString();
		}
		return result;
	}

	@Override
	public int compareTo(LogootIdContainer o) {
		if(size()>0 && o.size()>0){
			for(int i=0;i<Math.min(size(), o.size());i++){
				int comp=getChaine().get(i).compareTo(o.getChaine().get(i));
				if(comp!=0)
					return comp;
			}
			if(size()>o.size())
				return 1;
			else if (size()<o.size())
				return -1;
		}
		return 0;
	}
	
}
