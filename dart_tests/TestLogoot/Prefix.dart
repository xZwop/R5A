class Prefix {
  List<int> digits;
  
  Prefix(LineIdentifier idl, int index) {
    digits = new List<int>();

    for(int i = 0; i < idl.length(); ++i) {
      digits.add(idl[i].digit);
    }
  }
  
  operator +(int digit) {
    int last = this.digits.removeLast() + digit;
    
    this.digits.add(last);
    
    return this;
  }
  
  operator [](int index) {
    return digits[index];
  }
  
  int length() {
    return digits.length;
  }
  
  int toInt() {
    String result = "";
    int size = (Replica.BASE-1).toString().length;
    
    for(int i = 0; i < digits.length; i++){
      String s = digits[i].toString();
      
      while(s.length < size) {
        s = "0" + s;
      }
      
      result += s;
    }
    
    return Math.parseInt(result);
  }
}
