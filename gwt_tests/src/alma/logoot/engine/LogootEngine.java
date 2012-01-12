package alma.logoot.engine;

import java.math.BigInteger;
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
	 * @param rep
	 *            identifiant de la réplique
	 * @return N identifiants pour la réplique s entre p et q
	 */
	static Collection<LogootIdContainer> generateLineIdentier(
			LogootIdContainer p, LogootIdContainer q, int N,
			LogootIdentifier rep) {
		// TODO
		BigInteger MAXINT = new BigInteger(Integer.MAX_VALUE+"");
		Collection<LogootIdContainer> list = new ArrayList<LogootIdContainer>();
		int index = 0;
		int interval = 0;
//		BigInteger interval = new BigInteger("0");
		while (interval< N ) {
			index++;
			BigInteger intervalB = prefix(q, index).subtract(prefix(p, index));
			if ( (intervalB).compareTo(MAXINT) != -1 )
				interval = Integer.MAX_VALUE;
			else 
				interval = intervalB.intValue();
		}
		int step = interval / N;
		BigInteger stepB = new BigInteger(step+"");
		BigInteger r = prefix(p, index);

		for (int j = 1; j < N; j++) {
			BigInteger rand = r.add(new BigInteger((Random.nextInt(step) + 1)+""));
			list.add(constructIdentifier(rand, p, q, rep));
			r = r.add(stepB);
		}
		return list; 
	}

	/**
	 * 
	 * @param r
	 *            liste d'identifiants concaténés
	 * @param p
	 *            premier identifiant de position
	 * @param q
	 *            second identifiant de position
	 * @param rep
	 *            defini horloge et id de la replique pour laquelle on genere la
	 *            position
	 * @return l'identifiant pour la replique définit par rep(id+horloge)
	 */
	static LogootIdContainer constructIdentifier(BigInteger r, LogootIdContainer p,
			LogootIdContainer q, LogootIdentifier rep) {
		LogootIdContainer result = new LogootIdContainer();
		LinkedList<Integer> prefix = prefixToList(r);
		for (int i : prefix) {
			LogootIdentifier triplet = new LogootIdentifier();
			triplet.setI(i);
			if (i == p.getChaine().get(i).getI()) {
				triplet.setR(p.getChaine().get(i).getR());
				triplet.setS(p.getChaine().get(i).getS());
			} else if (i == q.getChaine().get(i).getI()) {
				triplet.setR(q.getChaine().get(i).getR());
				triplet.setS(q.getChaine().get(i).getS());
			} else {
				triplet.setR(rep.getR());
				rep.setS(rep.getS() + 1);
				triplet.setS(rep.getS());
			}
			result.add(triplet);
		}
		return result;
	}

	static public BigInteger prefix(LogootIdContainer id, int index) {
		String result = "";
		int size = new Integer(LogootConf.BASE - 1).toString().length();
		for (int i = 0; i < index; i++) {
			String s = "";
			if ( i < id.size() ) {
				s = String.valueOf(id.getChaine().get(i).getI());
			}
			while (s.length() < size)
				s = "0" + s;
			result += s;
		}
		return new BigInteger(result);
	}

	static private LinkedList<Integer> prefixToList(BigInteger prefix) {
		LinkedList<Integer> result = new LinkedList<Integer>();
		String ts = String.valueOf(prefix.toString());
		int size = new Integer(LogootConf.BASE - 1).toString().length();

		int endIndex = ts.length() - 1;
		int beginIndex = Math.max(0, endIndex - size);
		ts.substring(beginIndex, endIndex);
		result.addLast(Integer.parseInt(ts));
		while (beginIndex != 0) {
			endIndex = ts.length() - 1;
			beginIndex = Math.max(0, endIndex - size);
			ts.substring(beginIndex, endIndex);
			result.addFirst(Integer.parseInt(ts));
		}
		return result;
	}

}
