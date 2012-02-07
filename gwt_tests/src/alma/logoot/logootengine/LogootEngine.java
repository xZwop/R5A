package alma.logoot.logootengine;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import alma.logoot.logootengine.diff_match_patch.Diff;

/**
 * Classe utilitaire comprenant les fonctions de manipulation des identifiants,
 * notament les algos p61-63.
 * 
 * @author R5A
 * 
 */
public class LogootEngine implements ILogootEngine {

	String oldText;
	ArrayList<LogootIdContainer> idTable;
	LogootIdentifier id = new LogootIdentifier(0, -1, 0);

	private String getOldText() {
		if (oldText == null)
			oldText = "";
		return oldText;
	}

	private void setOldText(String oldText) {
		this.oldText = oldText;
	}

	public ArrayList<LogootIdContainer> getIdTable() {
		if (idTable == null) {
			idTable = new ArrayList<LogootIdContainer>();
			LogootIdContainer first = new LogootIdContainer();
			first.add(new LogootIdentifier(1, 0, 0));
			LogootIdContainer last = new LogootIdContainer();
			last.add(new LogootIdentifier(LogootConf.BASE, 0, 0));
			idTable.add(first);
			idTable.add(last);
		}
		return idTable;
	}

	private LogootIdentifier getId() {
		return id;
	}

	private void setId(LogootIdentifier id) {
		this.id = id;
	}

	/**
	 * 
	 * @param p
	 *            premier identifiant de position
	 * @param q
	 *            second identifiant de position
	 * @param N
	 *            nombre d'identifiant souhaites
	 * @param rep
	 *            identifiant de la replique
	 * @return N identifiants pour la replique s entre p et q
	 */
	public ArrayList<LogootIdContainer> generateLineIdentier(
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
			step = Math.min(LogootConf.BOUNDARY, interval / N);
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
				rand = r.add(new BigInteger((random.nextInt(step - 1) + 1) + ""));
			}
			list.add(constructIdentifier(rand, p, q, rep));
			p = list.get(list.size() - 1);
			r = r.add(stepB);
		}
		return list;
	}

	/**
	 * 
	 * @param r
	 *            liste d'identifiants concatenes
	 * @param p
	 *            premier identifiant de position
	 * @param q
	 *            second identifiant de position
	 * @param rep
	 *            defini horloge et id de la replique pour laquelle on genere la
	 *            position
	 * @return l'identifiant pour la replique definit par rep(id+horloge)
	 */
	public LogootIdContainer constructIdentifier(BigInteger r,
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
	 * @param id
	 *            Identifiant de caractere
	 * @param n
	 *            Nombre de triplet a prendre en compte
	 * @return les n identifiants dans la base concatenes.
	 */
	public BigInteger prefix(LogootIdContainer id, int n) {
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

	private LinkedList<Integer> prefixToList(BigInteger prefix) {
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

	@Override
	public String generatePatch(String text) {

		// Initialization by making a diff between the old text and the new one.
		diff_match_patch diffEngine = new diff_match_patch();
		LinkedList<Diff> diff = diffEngine.diff_main(getOldText(), text, false);
		setOldText(text);
		int index = 0;
		Collection<IOperation> patch = new ArrayList<IOperation>();

		// For each difference, we need to add or delete some positions.
		for (Diff d : diff) {
			if (d.operation == alma.logoot.logootengine.diff_match_patch.Operation.EQUAL) {
				index += d.text.length();
			} else if (d.operation == alma.logoot.logootengine.diff_match_patch.Operation.INSERT) {
				LogootIdContainer p = getIdTable().get(index);
				LogootIdContainer q = getIdTable().get(index + 1);
				ArrayList<LogootIdContainer> idList = generateLineIdentier(p,
						q, d.text.length(), getId());
				// Mise a jour idTable
				getIdTable().addAll(index + 1, idList);
				// Creation operations
				int i = 0;
				for (LogootIdContainer lic : idList) {
					IOperation op = new Operation("i", lic, d.text.charAt(i));
					patch.add(op);
					i++;
				}
				index += d.text.length();
			} else { // DELETE
				for (int i = 0; i < d.text.length(); i++) {
					LogootIdContainer position = getIdTable().get(index + 1);
					getIdTable().remove(position);
					IOperation op = new Operation("d", position);
					patch.add(op);
				}
			}
		}
		// TODO : serialization
		// return serializeToJson(person);
		return patch.toString();
	}

	@Override
	public String deliver(String patch) {
		Collection<IOperation> patched = new ArrayList<IOperation>();
		try {
			patch = patch.split("^[\\[]{2}")[1];
			patch = patch.split("[\\]]{2}$")[0];
			String[] splited = patch.split("[\\]],[ ][\\[]");
			for (int i = 0; i < splited.length; i++) {
				patched.add(new Operation(splited[i]));
			}
		} catch (Exception e) {
			System.err.println("LogootEngine : Deserialization error.");
		}
		System.out.println("L'objet apres serialization : "+patched.getClass().getName()+ " "+ patched);
		for (IOperation o : patched)
			deliver(o);
		return getOldText();
	}

	private void deliver(IOperation op) {
		// TODO : FAIRE UNE VERIFICATION SUR LID, VERIFIER SI CE NEST PAS LE MEME QUE CELUI DU CLIENT
		// ( sinon probleme dans la table des ids. )
		Operation o = (Operation) op;
		o.getPosition().get(o.getPosition().size()-1).getIdentifier();
		if (o.getPosition().get(o.getPosition().size()-1).getIdentifier().equals(id.getIdentifier())){
			return;
		}
		if (o.isIns()) {
			int index = -Collections
					.binarySearch(getIdTable(), o.getPosition()) - 1;
			StringBuffer sb = new StringBuffer(getOldText());
			sb.insert(index - 1, o.getContent());
			setOldText(sb.toString());
			getIdTable().add(index, o.getPosition());
		} else {
			int index = Collections.binarySearch(getIdTable(), o.getPosition());
			if (index > 0) {
				StringBuffer sb = new StringBuffer(getOldText());
				sb.deleteCharAt(index - 1);
				setOldText(sb.toString());
				getIdTable().remove(index);
			}
		}
	}

	@Override
	public void setId(Integer id) {
		System.out.println("LogootEngine - Reception d'un id : " + id);
		this.id.setIdentifier(id);
	}

}
