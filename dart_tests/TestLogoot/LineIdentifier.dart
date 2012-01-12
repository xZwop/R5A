class LineIdentifier {
  List<Position> positions;
  
  LineIdentifier() {
    this.positions = new List<Position>();
  }
  
  operator [](int index) {
    return positions[index];
  }
  
  void conc(Position position) {
    this.positions.add(position);
  }
  
  int length() {
    return positions.length;
  }
}
