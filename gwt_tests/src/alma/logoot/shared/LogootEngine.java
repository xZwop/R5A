package alma.logoot.shared;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Classe utilitaire comprenant les fonctions de manipulation des
 * identifiants, notament les algos p61-63.
 * 
 * @author Driz
 *
 */
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
	public static ArrayList<LogootIdContainer> generateLineIdentier(
			LogootIdContainer p, LogootIdContainer q, int N,
			LogootIdentifier rep) {

		BigInteger MAXINT = new BigInteger(Integer.MAX_VALUE + "");
		ArrayList<LogootIdContainer> list = new ArrayList<LogootIdContainer>();
		int index = 0;
		int interval = 0;
		while (interval < N) {
			index++;
			BigInteger intervalB = prefix(q, index).subtract(prefix(p, index));
			intervalB = intervalB.subtract(new BigInteger("1"));
			if ((intervalB).compareTo(MAXINT) != -1)
				interval = Integer.MAX_VALUE;
			else
				interval = intervalB.intValue();
		}
		int step;
		if (LogootConf.USEBOUNDARY) {
			step = Math.min(LogootConf.BOUNDARY, interval/N);
		} else {
			step = interval / N;
		}
		BigInteger stepB = new BigInteger(step + "");
		BigInteger r = prefix(p, index);
		Random random = new Random();
		for (int j = 0; j < N; j++) {
			BigInteger rand;
			if (step == 1) {
				rand = r.add(new BigInteger("1"));
			} else {
				rand = r.add(new BigInteger((random
						.nextInt(step - 1) + 1) + ""));
			}
			list.add(constructIdentifier(rand, p, q, rep));
			p=list.get(list.size()-1);
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
	static public LogootIdContainer constructIdentifier(BigInteger r,
			LogootIdContainer p, LogootIdContainer q, LogootIdentifier rep) {
		// TODO : Ici, la fonction risque de prendre des identifiants a la fois
		// dans p et dans q.
		LogootIdContainer result = new LogootIdContainer();
		LinkedList<Integer> prefix = prefixToList(r);
		int index = 0;
		for (int i : prefix) {
			LogootIdentifier triplet = new LogootIdentifier();
			triplet.setDigit(i);
			if (index < p.size() && i == p.get(index).getDigit()) {
				triplet.setClock(p.get(index).getClock());
				triplet.setIdentifier(p.get(index).getIdentifier());
			} else if (index < q.size() && i == q.get(index).getDigit()) {
				triplet.setClock(q.get(index).getClock());
				triplet.setIdentifier(q.get(index).getIdentifier());
			} else {
				rep.setClock(rep.getClock() + 1);
				triplet.setClock(rep.getClock());
				triplet.setIdentifier(rep.getIdentifier());
			}
			index++;
			result.add(triplet);
		}
		return result;
	}

	/**
	 * 
	 * @param id Identifiant de caractère
	 * @param n Nombre de triplet à prendre en compte
	 * @return les n identifiants dans la base concatenés.
	 */
	static public BigInteger prefix(LogootIdContainer id, int n) {
		String result = "";
		int size = new Integer(LogootConf.BASE - 1).toString().length();
		for (int i = 0; i < n; i++) {
			String s = "";
			if (i < id.size()) {
				s = String.valueOf(id.get(i).getDigit());
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

		int endIndex = ts.length();
		int beginIndex = Math.max(0, endIndex - size);
		String cs = ts.substring(beginIndex, endIndex);
		result.addLast(Integer.parseInt(cs));
		while (beginIndex != 0) {
			endIndex -= cs.length();
			beginIndex = Math.max(0, endIndex - size);
			cs = ts.substring(beginIndex, endIndex);
			result.addFirst(Integer.parseInt(cs));
		}
		return result;
	}

}
