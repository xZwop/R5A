package alma.logoot.logootengine;

/**
 * Representation of Logoot Operation.
 * 
 * Operation is a implementation of {@link IOperation}.An operation contains a
 * modification on Id Table. The modification could be an insertion of a char in
 * Id Table or a deletion. If operation is a deletion the operation contains
 * only a {@link LineId}. If operation is an insertion moreover the added char
 * is accessible in getContent method.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class Operation implements IOperation {

  private static final long serialVersionUID = 1L;

  /**
   * Value for insert type operation.
   */
  public static final String INSERT = "i";

  /**
   * Value for delete type operation.
   */
  public static final String DELETE = "d";

  /**
   * Type of operation, one of the :
   * <ul>
   * <li>Operation.INSERT</li>
   * <li>Operation.DELETE</li>
   * </ul>
   */
  private String type;

  /**
   * The LineId of operations.
   */
  private LineId lineId;

  /**
   * The added character (if operation is an insertion).
   */
  private Character content = null;

  /**
   * Default operation constructor (for serialization).
   */
  public Operation() {
  }

  /**
   * Operation constructor.
   * 
   * @param type
   *          Type of operation, one of the :
   *          <ul>
   *          <li>Operation.INSERT</li>
   *          <li>Operation.DELETE</li>
   *          </ul>
   * @param lineId
   *          The LineId of operations.
   * @param content
   *          The added character.
   */
  public Operation(String type, LineId lineId, Character content) {
    this.type = type;
    this.lineId = lineId;
    this.content = content;
  }

  // TODO - Doc Serialization
  public Operation(String s) {
    String[] splited = s.split("[;][ ]");
    if (splited.length != 3) {
      System.err.println("Operation : Deserialization error.");
    } else {
      this.setType(splited[0]);
      this.setContent(splited[1].charAt(0));
      this.setLineId(new LineId(splited[2]));
    }
  }

  /**
   * Returns the type of operation.
   * 
   * @return Type of operation, one of the :
   *         <ul>
   *         <li>Operation.INSERT</li>
   *         <li>Operation.DELETE</li>
   *         </ul>
   */
  public String getType() {
    return type;
  }

  /**
   * Set the type of operation.
   * 
   * @param type
   *          Type of operation, one of the :
   *          <ul>
   *          <li>Operation.INSERT</li>
   *          <li>Operation.DELETE</li>
   *          </ul>
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the line identifier.
   * 
   * @return The LineId of operation.
   */
  public LineId getLineId() {
    return lineId;
  }

  public void setLineId(LineId lineId) {
    this.lineId = lineId;
  }

  /**
   * Returns the content inserted.
   * 
   * Returns the content inserted. If the operation is a deletion, the content
   * would be <code>null</code>.
   * 
   * @return Content inserted.
   */
  public Character getContent() {
    return content;
  }

  public void setContent(Character content) {
    this.content = content;
  }

  @Override
  public boolean isIns() {
    return this.type.equals("i");
  }

  @Override
  public boolean isDel() {
    return this.type.equals("d");
  }

  /**
   * Returns a string representation of the object.
   * 
   * @return A string representation of the object.
   */
  public String toString() {
    return "[" + type + "; " + content + "; " + lineId + "]";
  }

  /**
   * Returns new Insert Operation.
   * 
   * @param lineId
   *          The LineId of insertion.
   * @param content
   *          The added character.
   * @return A new insert operation.
   */
  public static Operation insertOperation(LineId lineId, Character content) {
    return new Operation(Operation.INSERT, lineId, content);
  }

  /**
   * Returns new Delete Operation.
   * 
   * @param lineId
   *          The LineId of deletion.
   * @return A new delete operation.
   */
  public static Operation deleteOperation(LineId lineId) {
    return new Operation(Operation.DELETE, lineId, null);
  }
}
