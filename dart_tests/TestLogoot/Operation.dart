class Operation {
  static final int INSERTION = 0;
  static final int DELETION = 1;
  
  int type;
  int id;
  String content;
  
  Operation(this.type, this.id, this.content) {
  }
}
