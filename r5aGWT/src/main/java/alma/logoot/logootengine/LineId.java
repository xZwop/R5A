package alma.logoot.logootengine;

import java.util.ArrayList;

/**
 * Logoot Line identifier.
 * 
 * LineId is a no mutable ordered Position list. The last Position of the list
 * is the last position of line and position used to create line. Moreover, each
 * position in LineId which have same <code>BASE</code>.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class LineId extends ArrayList<Position> implements Comparable<LineId> {

  private static final long serialVersionUID = 8707580451647621697L;

  /**
   * Line Identifier Default Constructor.
   */
  public LineId() {
    super();
  }

  // TODO - Desirialization Doc
  public LineId(String s) {
    try {
      s = s.split("^[<]")[1];
      s = s.split("[>]$")[0];
    } catch (Exception e) {
      System.err.println("LogootIdContainer : Deserialization error.");
    }
    String[] splited = s.split("[>][<]");
    for (int i = 0; i < splited.length; i++) {
      this.add(new Position(splited[i]));
    }
  }

  /**
   * Returns a string representation of the object.
   * 
   * @return A string representation of the object.
   */
  public String toString() {
    String result = "";

    for (Position l : this) {
      result += l.toString();
    }

    return result;
  }

  /**
   * Compare a LineId with the current.
   * 
   * Compare a LineId with the current. The ordered relation is given p59 of
   * Logoot Algorithm (Definition 23).
   * 
   * @param lineId
   *          The LineId to compare with current.
   * @return Result of comparison. Values is one of (-1, 0, 1).
   */
  // public int compareTo(LineId lineid) {
  // if (size() > 0 && lineid.size() > 0) {
  // int lastAvailableIndex = Math.min(size(), lineid.size()) -1;
  //
  // for (int i = 0; i < lastAvailableIndex; ++i) {
  // int comparison = get(i).compareToFake(lineid.get(i));
  //
  // if (comparison != 0) {
  // return comparison;
  // }
  // }
  //
  // return get(lastAvailableIndex).compareTo(lineid.get(lastAvailableIndex));
  // }
  //
  // return 0;
  // }
  public int compareTo(LineId lineid) {
    if (size() > 0 && lineid.size() > 0) {
      for (int i = 0; i < Math.min(size(), lineid.size()); i++) {
        int comp = get(i).compareTo(lineid.get(i));
        if (comp != 0) {
          return comp;
        }
      }

      if (size() > lineid.size()) {
        return 1;
      } else if (size() < lineid.size()) {
        return -1;
      }
    }

    return 0;
  }

  /**
   * Test if an object equals current {@link LineId}.
   * 
   * @param object
   *          The object to test.
   * @return <code>true</code> if object are equals, <code>false</code> else.
   */
  public boolean equals(Object object) {
    if (!(object instanceof LineId)) {
      return false;
    } else {
      return (compareTo((LineId) object) == 0);
    }
  }

  /**
   * Returns new Line Identifier with min position only.
   * 
   * The Line Identifier with min position represent the starter of document.
   * 
   * @return New LineId with Min Position.
   */
  public static LineId getDocumentStarter() {
    LineId lineId = new LineId();

    lineId.add(Position.getMin());
    return lineId;
  }

  /**
   * Returns new Line Identifier with max position only.
   * 
   * The Line Identifier with max position represent the end of document.
   * 
   * @return New LineId with Max Position.
   */
  public static LineId getDocumentFinisher() {
    LineId lineId = new LineId();

    lineId.add(Position.getMax());
    return lineId;
  }
}
