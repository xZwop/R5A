class LogootPatch {
  List<Operation> _patch;

  LogootPatch(): super() {
    this._patch = new List<Operation>();
  }

  String toString() {
    String result = '{';

    if(this._patch.length > 0) {
      for(int i = 0; i < length() - 1; ++i) {
        result += this._patch[i].toString();
        result += '-';
      }
      
      result += this._patch[length() - 1].toString();
    }

    result += '}';

    return result;
  }
  
  static fromString(String descr) {
    LogootPatch patch = new LogootPatch();
    
    if(descr.length > 2) {
      String stringOperations = descr.substring(1, descr.length - 1);
      List<String> operations = stringOperations.split('-');
  
      for(int i = 0; i < operations.length; ++i) {
        patch.add(Operation.fromString(operations[i]));
      }
    }
    
    return patch;
  }
  
  Operation operator[](int index) {
    return this._patch[index];
  }
  
  int length() {
    return _patch.length;
  }
  
  void add(Operation op) {
    this._patch.add(op);
  }
}
