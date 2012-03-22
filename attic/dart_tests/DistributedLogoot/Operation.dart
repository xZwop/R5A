class Operation {
  static final int INSERTION = 0;
  static final int DELETION = 1;
  
  int _type;
  LineIdentifier _id;
  String _content;
  
  Operation(this._type, this._id, this._content) {
  }
  
  int get type() => this._type;
  
  void set type(int type) {
    this._type = type;
  }
  
  LineIdentifier get id() => this._id;
  
  void set id(LineIdentifier id) {
    this._id = id;
  }
  
  String get content() => this._content;
  
  void set content(String content) {
    this._content = _content;
  }
  
  String toString() {
    return '{' + this._type + ';' + this._id.toString() + ';' + this._content + '}';
  }
  
  static Operation fromString(String descr) {
    if(descr.length > 2) {
      String stringOperation = descr.substring(1, descr.length - 1);
      List<String> operation = stringOperation.split(';');
      int type = Math.parseInt(operation[0]);
      LineIdentifier id = LineIdentifier.fromString(operation[1]);
      String content = operation[2];
      
      return new Operation(type, id, content);
    }
    
    return null;
  }
}
