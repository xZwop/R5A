class LineIdentifier {
  static final int MAX = 100;
  static final int NA = -1;

  List<Position> positions;
  
  LineIdentifier() {
    this.positions = new List<Position>();
  }
  
  static LineIdentifier firstIDL() {
    LineIdentifier idl = new LineIdentifier();
    
    idl.conc(new Position(0, LineIdentifier.NA, LineIdentifier.NA));
    
    return idl;
  }
  
  static LineIdentifier lastIDL() {
    LineIdentifier idl = new LineIdentifier();
    
    idl.conc(new Position(LineIdentifier.MAX, LineIdentifier.NA, LineIdentifier.NA));
    
    return idl;
  }
  
  operator ==(LineIdentifier other) {
    return (this.positions == other.positions);
  }
  
  operator <(LineIdentifier other) {
    if(this == LineIdentifier.firstIDL()) {
      return true;
    } else if(other == LineIdentifier.firstIDL()) {
      return false;
    } else if(this == LineIdentifier.lastIDL()) {
      return false;
    } else if(other == LineIdentifier.lastIDL()) {
      return true;
    } else if(this.positions.length > 0 && other.length() > 0) {
      for(int i = 0; i < this.positions.length; ++i) {
        if(i >= other.length()) {
          return true;
        } else if(this.positions[i] < other.positions[i]) {
          return true;
        } else if(!(this.positions[i] == other.positions[i])) {
          return false;
        }
      } 
    }
    
    return false;
  }
  
  operator [](int index) {
    return this.positions[index];
  }
  
  String toString() {
    String result = "";

    for(int i = 0; i < this.positions.length; ++i) {
      result += this.positions[i].toString();
    }
    
    return result;
  }
  
  void conc(Position position) {
    this.positions.add(position);
  }
  
  int length() {
    return this.positions.length;
  }
}
