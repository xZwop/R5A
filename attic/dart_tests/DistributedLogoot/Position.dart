class Position {
  int _digit;
  String _repid;
  int _clock;
  
  Position(this._digit, this._repid, this._clock) {
  }
  
  int get digit() => this._digit;
  
  void set digit(int digit) {
    this._digit = digit;
  }
  
  String get repid() => this._repid;
  
  void set repid(String repid) {
    this._repid = repid;
  }
  
  int get clock() => this._clock;
  
  void set clock(int clock) {
    this._clock = clock;
  }
  
  operator ==(Position other) {
    return (this._digit == other._digit && this._repid == other._repid && this._clock == other._clock);
  }
  
  operator <(Position other) {
    return (this._digit < other._digit
            || (this._digit == other._digit && this._repid.compareTo(other._repid) == -1)
            || ( this._digit == other._digit && this._repid == other._repid && this._clock < other._clock));
  }
  
  operator >(Position other) {
    return (this._digit > other._digit
        || (this._digit == other._digit && this._repid.compareTo(other._repid) == 1)
        || ( this._digit == other._digit && this._repid == other._repid && this._clock > other._clock));
  }
  
  String toString() {
    return ('<' + this._digit + ',' + this._repid + ',' + this._clock + '>');
  }
  
  static fromString(String descr) {
    if(descr.length > 2) {
      String stringPos = descr.substring(1, descr.length - 1);
      List<String> position = stringPos.split(',');
      
      return new Position(Math.parseInt(position[0]), position[1], Math.parseInt(position[2]));
    }
    
    return null;
  }
}
