package alma.logoot.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.google.gwt.user.client.Random;

public class LogootEngine {

	/**
	 * 
	 * @param p
	 *            premier identifiant de position
	 * @param q
	 *            second identifiant de position
	 * @param N
	 *            nombre d'identifiant souhaités
	 * @param s
	 *            identifiant de la réplique
	 * @return N identifiants pour la réplique s entre p et q
	 */
	static Collection<LogootIdContainer> generateLineIdentier(LogootIdContainer p,
			LogootIdContainer q, int N, int s) {
		// TODO
		Collection<LogootIdContainer> list=new ArrayList<LogootIdContainer>();
		int index = 0;
		int interval = 0;
		while (interval < N) {
			index++;
			interval = prefix(q, index) - prefix(p, index) - 1;
		}
		int step = interval / N;
		int r = prefix(p, index);
		
		for (int j = 1; j < N; j++) {
			int rand=r+Random.nextInt(step)+1;
			list.add(constructIdentifier(rand, p, q, s));
			r+=step;
		}
		return list;
	}

	static LogootIdContainer constructIdentifier(int r, LogootIdContainer p,
			LogootIdContainer q, int s) {
		LogootIdContainer result=new LogootIdContainer();
		LinkedList<Integer> prefix=prefixToList(r);
		for(int i:prefix){
			// Pas compris ici !
		}
		return result;
	}

	static public int prefix(LogootIdContainer id, int index) {
		String result="";
		int size=new Integer(LogootConf.BASE-1).toString().length();
		for(int i=0;i<index;i++){
			String s= String.valueOf(id.getChaine().get(i).getI());
			while(s.length()<size)
				s="0"+s;
			result+=s;
		}
		return Integer.parseInt(result);
	}
	
	static private LinkedList<Integer> prefixToList(int prefix){
		LinkedList<Integer> result=new LinkedList<Integer>();
		String ts=String.valueOf(prefix);
		int size=new Integer(LogootConf.BASE-1).toString().length();
		
		int endIndex=ts.length()-1;
		int beginIndex=Math.max(0,endIndex-size);
		ts.substring(beginIndex, endIndex);
		result.addLast(Integer.parseInt(ts));
		while(beginIndex!=0){
			endIndex=ts.length()-1;
			beginIndex=Math.max(0,endIndex-size);
			ts.substring(beginIndex, endIndex);
			result.addFirst(Integer.parseInt(ts));
		}
		return result;
	}

}
