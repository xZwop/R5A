package alma.logoot.shared;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;


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
		// TODO
		BigInteger MAXINT = new BigInteger(Integer.MAX_VALUE + "");
		ArrayList<LogootIdContainer> list = new ArrayList<LogootIdContainer>();
		int index = 0;
		int interval = 0;
		// BigInteger interval = new BigInteger("0");
		while (interval < N) {
			index++;
			BigInteger intervalB = prefix(q, index).subtract(prefix(p, index));
			intervalB=intervalB.subtract(new BigInteger("1"));
			System.out.println("Intervale : "+intervalB);
			if ((intervalB).compareTo(MAXINT) != -1)
				interval = Integer.MAX_VALUE;
			else
				interval = intervalB.intValue();
		}
		System.out.println("Index : " + index);
		int step = interval / N;

		BigInteger stepB = new BigInteger(step + "");
		System.out.println("Step : " + step + "(" + stepB + ")");
		BigInteger r = prefix(p, index);
		Random random=new Random();
		for (int j = 0; j < N; j++) {
			BigInteger rand = r.add(new BigInteger((random.nextInt(step)+1 )
					+ ""));
			System.out.println("Prefix(+rand)"+rand+"\n");
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
	static public LogootIdContainer constructIdentifier(BigInteger r,
			LogootIdContainer p, LogootIdContainer q, LogootIdentifier rep) {
		// TODO : Ici, la fonction risque de prendre des identifiants a la fois
		// dans p et dans q.
		LogootIdContainer result = new LogootIdContainer();
		LinkedList<Integer> prefix = prefixToList(r);
		int index = 0;
		for (int i : prefix) {
			LogootIdentifier triplet = new LogootIdentifier();
			triplet.setI(i);
			if (index < p.size() && i == p.getChaine().get(index).getI()) {
				triplet.setR(p.getChaine().get(index).getR());
				triplet.setS(p.getChaine().get(index).getS());
			} else if (index < q.size() && i == q.getChaine().get(index).getI()) {
				triplet.setR(q.getChaine().get(index).getR());
				triplet.setS(q.getChaine().get(index).getS());
			} else {
				rep.setR(rep.getR() + 1);
				triplet.setR(rep.getR());
				triplet.setS(rep.getS());
			}
			index++;
			result.add(triplet);
		}
		return result;
	}

	static public BigInteger prefix(LogootIdContainer id, int index) {
		String result = "";
		int size = new Integer(LogootConf.BASE - 1).toString().length();
		for (int i = 0; i < index; i++) {
			String s = "";
			if (i < id.size()) {
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
