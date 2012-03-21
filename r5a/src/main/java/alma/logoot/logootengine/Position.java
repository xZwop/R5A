package alma.logoot.logootengine;

import java.io.Serializable;

/**
 * Logoot Position is a ordered triplet.
 * 
 * @author Adrien Bougouin adrien.bougoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class Position implements Comparable<Position>, Serializable {

  private static final long serialVersionUID = 8727581451647621694L;

  /**
   * Integer in range <code>[1..BASE[</code>.
   */
  private int digit;

  /**
   * Unique replica identifier.
   */
  private long replica;

  /**
   * Timestamps.
   */
  private int clock;

  /**
   * Default Position constructor.
   */
  public Position() {
  }

  /**
   * Position constructor according to Logoot algorithm.
   * 
   * @param digit
   *          Integer in range of <code>[1..BASE[</code>.
   * @param replica
   *          Unique replica identifier.
   * @param clock
   *          Timestamps.
   */
  public Position(int digit, Integer replica, int clock) {
    this.digit = digit;
    this.replica = replica;
    this.clock = clock;
  }

  // TODO -- Explicité cette méthode.
  public Position(String s) {
    String[] splited = s.split("[,][ ]");
    if (splited.length != 3) {
      System.err.println("ILogootIdentifier : Deserialization error.");
    } else {
      this.digit = new Integer(splited[0]);
      this.replica = new Long(splited[1]);
      this.clock = new Integer(splited[2]);
    }
  }

  /**
   * Compare current position with another.
   * 
   * Position is an ordered Triplet. The order definition is given p59 of Logoot
   * algorithm.
   * 
   * @param position
   *          Position to compare with current.
   * @return Result of comparison. Values is one of (-1, 0, 1).
   */
  public int compareTo(Position position) {
    if (this.digit > position.digit) {
      return 1;
    } else if (this.digit < position.digit) {
      return -1;
    }

    if (this.replica > position.replica) {
      return 1;
    } else if (this.replica < position.replica) {
      return -1;
    }

    if (this.clock > position.clock) {
      return 1;
    } else if (this.clock < position.clock) {
      return -1;
    }

    return 0;
  }
  /**
   * Compare current position with another.
   * 
   * Position is an ordered Triplet. With compareToFake, only the digit is
   * compare (not the clock and the replica).
   * 
   * @param position
   *          Position to compare with current.
   * @return Result of comparison. Values is one of (-1, 0, 1).
   */
  public int compareToFake(Position position) {
    if (this.digit > position.digit) {
      return 1;
    } else if (this.digit < position.digit) {
      return -1;
    } else {
      return 0;
    }
  }
  
  /**
   * Returns the Integer in range of <code>[1..BASE[</code>.
   * 
   * @return The integer in range of <code>[1..BASE[</code>.
   */
  public int getDigit() {
    return digit;
  }

  public void setDigit(int i) {
    this.digit = i;
  }

  /**
   * Returns replica identifying the user.
   * 
   * @return The unique identifier.
   */
  public long getReplica() {
    return replica;
  }

  public void setReplica(long replica) {
    this.replica = replica;
  }

  /**
   * Returns the current value of the clock.
   * 
   * @return The value of clock.
   */
  public int getClock() {
    return clock;
  }

  public void setClock(int clock) {
    this.clock = clock;
  }

  /**
   * Returns a string representation of Position object.
   * 
   * @return String representation of Position object.
   */
  public String toString() {
    return "<" + digit + ", " + replica + ", " + clock + ">";
  }

  /**
   * Returns minimum position value possible.
   * 
   * @return New Position that is minimum value accepted.
   */
  public static Position getMin() {
    return new Position(1, 0, 0);
  }

  /**
   * Returns maximum position value possible.
   * 
   * @return New Position that is maximum value accepted.
   */
  public static Position getMax() {
    return new Position(LogootConf.BASE - 1, 0, 0);
  }
}
