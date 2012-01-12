class Position {
  int digit;
  int repid;
  int clock;
  
  Position(this.digit, this.repid, this.clock) {
  }
  
  operator ==(Position other) {
    return (this.digit == other.digit && this.repid == other.repid && this.clock == other.clock);
  }
  
  operator <(Position other) {
    return (this.digit < other.digit
            || (this.digit == other.digit && this.repid < other.repid)
            || ( this.digit == other.digit && this.repid == other.repid && this.clock < other.clock));
  }
  
  String toString() {
    return ('<' + this.digit + ', ' + this.repid + ', ' + this.clock + '>');
  }
}
