class LineIdentifier {
  static final int MAX = 100;
  static final int NA = 1;

  List<Position> _positions;
  
  LineIdentifier() {
    this._positions = new List<Position>();
  }
  
  static LineIdentifier firstIDL() {
    LineIdentifier idl = new LineIdentifier();
    
    idl.conc(new Position(1, LineIdentifier.NA, LineIdentifier.NA));
    
    return idl;
  }
  
  static LineIdentifier lastIDL() {
    LineIdentifier idl = new LineIdentifier();
    
    idl.conc(new Position(LineIdentifier.MAX, LineIdentifier.NA, LineIdentifier.NA));
    
    return idl;
  }
  
  List<Position> get position() => this.position;
  
  void set position(List<Position> position) {
    this._positions = position;
  }
  
  operator ==(LineIdentifier other) {
    return (this._positions == other._positions);
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
    } else if(this._positions.length > 0 && other._positions.length > 0) {
      for(int i = 0; i < other._positions.length; ++i) {
        if(i >= this._positions.length) {
          return true;
        } else if(this._positions[i] < other._positions[i]) {
          return true;
        } else if(this._positions[i] > other._positions[i]) {
          return false;
        }
      } 
    }
    
    return false;
  }
  
  operator [](int index) {
    return this._positions[index];
  }
  
  String toString() {
    String result = "";

    for(int i = 0; i < this._positions.length; ++i) {
      result += this._positions[i].toString();
    }
    
    return result;
  }
  
  static fromString(String descr) {
    LineIdentifier idl = new LineIdentifier();
    String stringIDL = descr.replaceAll('><', '>|<');
    List<String> idls = stringIDL.split('|');
    
    for(int i = 0; i < idls.length; ++i) {
      idl.conc(Position.fromString(idls[i]));
    }

    return idl;
  }
  
  void conc(Position position) {
    this._positions.add(position);
  }
  
  int length() {
    return this._positions.length;
  }
}
