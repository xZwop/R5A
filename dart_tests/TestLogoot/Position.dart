class Position {
  int digit;
  int repid;
  int clock;
  
  Position(this.digit, this.repid, this.clock) {
  }
  
  operator ==(Position other) {
    return (this.digit == other.digit && this.repid == other.repid && this.clock == other.clock);
  }
}
