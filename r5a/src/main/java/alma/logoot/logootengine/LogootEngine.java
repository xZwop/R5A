package alma.logoot.logootengine;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import alma.logoot.logootengine.utils.diff_match_patch;
import alma.logoot.logootengine.utils.diff_match_patch.Diff;

/**
 * Logoot algorithm implementation.
 * 
 * Logoot algorithm, {@link ILogootEngine} implementation. For works this
 * implementation use the diff_match_patch library.
 * 
 * @see http://code.google.com/p/google-diff-match-patch
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class LogootEngine implements ILogootEngine {

  /**
   * The current text in model (Id Table).
   */
  private String currentText;

  /**
   * Diff Match Patch Component.
   * @see http://code.google.com/p/google-diff-match-patch
   */
  private diff_match_patch diffEngine;

  /**
   * Logoot model.
   */
  private List<LineId> idTable;

  /**
   * User unique id.
   */
  private int replica;

  /**
   * User Clock.
   */
  private int clock;

  /**
   * Logoot Constructor.
   */
  public LogootEngine() {
    this.diffEngine = new diff_match_patch();
    this.idTable = new ArrayList<LineId>();
    this.currentText = "";
    this.replica = -1;
    this.clock = 0;

    // Initilize logoot model with start and end document
    this.idTable.add(LineId.getDocumentStarter());
    this.idTable.add(LineId.getDocumentFinisher());
  }

  @Override
  public String deliver(String patch) {
    ArrayList<IOperation> patched = new ArrayList<IOperation>();
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
    System.out.println("L'objet apres serialization : "
        + patched.getClass().getName() + " " + patched);

    // Operation o = (Operation) patched.get(0);
    // o.getPosition().get(o.getPosition().size()-1).getIdentifier();
    // if
    // (o.getPosition().get(o.getPosition().size()-1).getIdentifier().equals(
    //    id.getIdentifier())){
    // System.out.println("C'est moi je ne dois pas ecrire huhu.");
    // return null;
    // }

    for (IOperation op : patched)
      deliver(op);
    return getCurrentText();
  }

  @Override
  public String generatePatch(String text) {
    // Initialization by making a diff between the old text and the new one.
    LinkedList<Diff> diff = this.diffEngine.diff_main(getCurrentText(), text,
        false);
    setCurrentText(text);
    int index = 0;
    Collection<IOperation> patch = new ArrayList<IOperation>();

    // For each difference, we need to add or delete some positions.
    for (Diff d : diff) {
      if (d.operation == alma.logoot.logootengine.utils.diff_match_patch.Operation.EQUAL) {
        index += d.text.length();
      } else if (d.operation == alma.logoot.logootengine.utils.diff_match_patch.Operation.INSERT) {
        LineId p = getIdTable().get(index);
        LineId q = getIdTable().get(index + 1);
        ArrayList<LineId> idList = generateLineIdentier(p, q, d.text.length());
        // Mise a jour idTable
        getIdTable().addAll(index + 1, idList);
        // Creation operations
        int i = 0;
        for (LineId lic : idList) {
          IOperation op = Operation.insertOperation(lic, d.text.charAt(i));
          patch.add(op);
          i++;
        }
        index += d.text.length();
      } else { // DELETE
        for (int i = 0; i < d.text.length(); i++) {
          LineId lineId = getIdTable().get(index + 1);
          getIdTable().remove(lineId);
          IOperation op = Operation.deleteOperation(lineId);
          patch.add(op);
        }
      }
    }
    // TODO : serialization
    // return serializeToJson(person);
    return patch.toString();
  }

  @Override
  public void setId(Integer id) {
    System.out.println("LogootEngine - Reception d'un id : " + id);
    this.replica = id;
  }

  private String getCurrentText() {
    if (currentText == null)
      currentText = "";
    return currentText;
  }

  private void setCurrentText(String currentText) {
    this.currentText = currentText;
  }

  public List<LineId> getIdTable() {
    return this.idTable;
  }

  /**
   * 
   * @param p
   *          premier identifiant de position
   * @param q
   *          second identifiant de position
   * @param N
   *          nombre d'identifiant souhaites
   */
  public ArrayList<LineId> generateLineIdentier(LineId p, LineId q, int N) {

    BigInteger MAXINT = new BigInteger(Integer.MAX_VALUE + "");
    ArrayList<LineId> list = new ArrayList<LineId>();
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
      list.add(constructIdentifier(rand, p, q));
      p = list.get(list.size() - 1);
      r = r.add(stepB);
    }
    return list;
  }

  /**
   * 
   * @param r
   *          liste d'identifiants concatenes
   * @param p
   *          premier identifiant de position
   * @param q
   *          second identifiant de position
   * @param rep
   *          defini horloge et id de la replique pour laquelle on genere la
   *          position
   * @return l'identifiant pour la replique definit par rep(id+horloge)
   */
  public LineId constructIdentifier(BigInteger r, LineId p, LineId q) {
    // TODO : Ici, la fonction risque de prendre des identifiants a la fois
    // dans p et dans q.
    LineId result = new LineId();
    LinkedList<Integer> prefix = prefixToList(r);
    int index = 0;
    for (int i : prefix) {
      Position triplet = new Position();
      triplet.setDigit(i);
      if (index < p.size() && i == p.get(index).getDigit()) {
        triplet.setClock(p.get(index).getClock());
        triplet.setReplica(p.get(index).getReplica());
      } else if (index < q.size() && i == q.get(index).getDigit()) {
        triplet.setClock(q.get(index).getClock());
        triplet.setReplica(q.get(index).getReplica());
      } else {
        triplet.setClock(++ this.clock);
        triplet.setReplica(this.replica);
      }
      index++;
      result.add(triplet);
    }
    return result;
  }

  /**
   * 
   * @param id
   *          Identifiant de caractere
   * @param n
   *          Nombre de triplet a prendre en compte
   * @return les n identifiants dans la base concatenes.
   */
  public BigInteger prefix(LineId id, int n) {
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

  private void deliver(IOperation op) {
    // TODO : FAIRE UNE VERIFICATION SUR LID, VERIFIER SI CE NEST PAS LE MEME
    // QUE CELUI DU CLIENT
    // ( sinon probleme dans la table des ids. )
    Operation o = (Operation) op;
    if(o.getLineId().get(o.getLineId().size()-1).getReplica()==replica){
    	return;
    }
    if (o.isIns()) {
      int index = -Collections.binarySearch(getIdTable(), o.getLineId()) - 1;
      StringBuffer sb = new StringBuffer(getCurrentText());
      sb.insert(index - 1, o.getContent());
      setCurrentText(sb.toString());
      getIdTable().add(index, o.getLineId());
    } else {
      int index = Collections.binarySearch(getIdTable(), o.getLineId());
      if (index > 0) {
        StringBuffer sb = new StringBuffer(getCurrentText());
        sb.deleteCharAt(index - 1);
        setCurrentText(sb.toString());
        getIdTable().remove(index);
      }
    }
  }
}

