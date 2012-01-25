class Replica extends Isolate {
  static final String INIT = 'init';
  static final String DELIVER = 'deliver';

  static final num BASE = 100;
  static final num BOUNDARY = 10;

  String _name;
  int _identifier;
  int _clock;
  var _textZone;
  List<LineIdentifier> _idTable;
  String _currentText;
  SendPort _deliverPort;

  Replica(): super.light() {
  }

  void init(String name, int identifier, SendPort deliverPort) {
    this._name = name;
    this._identifier = identifier;
    this._clock = 1;
    this._idTable = new List<LineIdentifier>();
    this._textZone = document.query('#' + this._name);
    this._currentText = '';
    this._deliverPort = deliverPort;

    this._idTable.add(LineIdentifier.firstIDL());
    this._idTable.add(LineIdentifier.lastIDL());

    this._textZone.on.keyUp.add(logoot, false);
  }
  
  void main() {
    this.port.receive((message, SendPort replyTo) {
      switch(message['id']) {
        case INIT:
          init(message['args'][0], message['args'][1], message['args'][2]);
        break;

        case DELIVER:
          deliver(LogootPatch.fromString(message["args"][0]), message['args'][1]);
        break;
      }
    });
  }

  void logoot(event) {
    //print('===================================================================');
    //for(int i = 0; i < this._idTable.length; ++i) {
    //  print(this._idTable[i]);
    //}
    //print('-------------------------------------------------------------------');
    LogootPatch patch = new LogootPatch();
    DiffMatchPatch dmp = new DiffMatchPatch();
    String newText = this._textZone.value;
    List<Diff> diffs = dmp.diff_main(this._currentText, newText, false);
    int index = 0;

    for(int i = 0; i < diffs.length; ++i) {
      Diff d = diffs[i];
      int nbChar = d.text.length;

      if(d.operation == DIFF_EQUAL) {
        index += nbChar;
      } else if(d.operation == DIFF_INSERT) {
        LineIdentifier p = this._idTable[index];
        LineIdentifier q = this._idTable[index + 1];
        List<LineIdentifier> newLinesID = generateLineIdentifiers(p, q, nbChar, BOUNDARY);
        
        for(int j = 0; j < newLinesID.length; ++j) {
          patch.add(new Operation(Operation.INSERTION, newLinesID[j], d.text[j]));
        }

        index += nbChar;
      } else if(d.operation == DIFF_DELETE) {
        for(int j = 0; j < nbChar; ++j) {
          LineIdentifier toRemove = this._idTable[index + 1];
          
          patch.add(new Operation(Operation.DELETION, toRemove, ""));
        }
      }
    }

    deliver(patch, this._identifier);
    List<String> test = new List<String>();
    this._deliverPort.call({'id' : DELIVER, 'args' : [patch.toString(), this._identifier]});
    print('-------------------------------------------------------------------');
    for(int i = 0; i < this._idTable.length; ++i) {
      print(this._idTable[i]);
    }
    print('===================================================================');
  }
  
  List<LineIdentifier> generateLineIdentifiers(LineIdentifier p, LineIdentifier q, int N, int boundary) {
    List<LineIdentifier> list = new List<LineIdentifier>();
    int index = 0;
    int interval = 0;
    int step;
    int r;
    
    // FIXME Si interval est negatif, alors la boucle est infinie...
    //       prefix(p,index) peut etre superieur a prefix(q,index)...
    while(interval < N) {
      index++;
      interval = prefix(q, index) - prefix(p, index) - 1;
    }
    
    if((interval / N) < boundary) {
      step = (interval / N).toInt();
    } else {
      step = boundary;
    }
    r = prefix(p, index);
    
    for(int i = 0; i < N; ++i) {
      num random = (Math.random() * (step - 1)) + 1;

      list.add(constructIdentifier(r + random.toInt(), p, q));

      r += step;
    }

    return list;
  }
  
  int prefix(LineIdentifier p, int index) {
    String result = "";
    int size = (Replica.BASE-1).toString().length;
    
    for(int i = 0; i < index; i++){
      String s = "0";
      
      if(i < p.length()) {
        s = p[i].digit.toString();
      }
      
      while(s.length < size) {
        s = "0" + s;
      }
      
      result += s;
    }
    
    return Math.parseInt(result);
  }
  
  List<int> prefix2list(int pref) {
    List<int> result = new List<int>();
    String ts = pref.toString();
    int size = (Replica.BASE - 1).toString().length;
    int endIndex = ts.length;
    int beginIndex = Math.max(0, endIndex - size);
    String cs = ts.substring(beginIndex, endIndex);
    
    result.addLast(Math.parseInt(cs));
    
    while (beginIndex != 0) {
      endIndex -= cs.length;
      beginIndex = Math.max(0, endIndex - size);
      cs = ts.substring(beginIndex, endIndex);
      result.insertRange(0, 1, Math.parseInt(cs));
    }
    
    return result;
  }
  
  LineIdentifier constructIdentifier(int r, LineIdentifier p, LineIdentifier q) {
    LineIdentifier id = new LineIdentifier();
    List<int> pref = prefix2list(r);

    for(int i = 0; i < pref.length; ++i) {
      int d = pref[i];
      int s;
      int c;
      
      if(p.length() > i && d == p[i].digit) {
        s = p[i].repid;
        c = p[i].clock;
      } else if(q.length() > i && d == q[i].digit) {
        s = q[i].repid;
        c = q[i].clock;
      } else {
        s = this._identifier;
        c = this._clock++;
      }
      
      id.conc(new Position(d, s, c));
    }
    
    return id;
  }
  
  void deliver(LogootPatch patch, int identifier) {
    for(int i = 0; i < patch.length(); ++i) {
      Operation op = patch[i];
      
      if(op.type == Operation.INSERTION) {
        // cherche l'identifiant precedant le nouvel identifiant
        int position = closest(op.id);

        this._currentText = this._currentText.substring(0, position) + op.content + this._currentText.substring(position);
        
        if(!(this._identifier == identifier)) {
          // insertion du content a la position, dans le textarea
          this._textZone.value = this._currentText;
        }

        // ajout du nouvel identifiant, a la bonne position
        this._idTable.insertRange(position + 1, 1, op.id);
      } else if(op.type == Operation.DELETION) {
        // cherche l'identifiat precedant l'identifiant a supprimer
        int position = closest(op.id) + 1;

        this._currentText = this._currentText.substring(0, position - 1) + this._currentText.substring(position);
        
        if(!(this._identifier == identifier)) {
          this._textZone.value = this._currentText;
        }

        this._idTable.removeRange(position, 1);
      }
    }
  }
  
  int closest(LineIdentifier idl) {
    int begin = 0;
    int end = this._idTable.length - 1;
    
    while(begin <= end) {
      int center = (begin + ((end - begin) / 2)).toInt();
      
      if((this._idTable[center] < idl) && !(this._idTable[center + 1] < idl)) {
        return center;
      } else {
        if(this._idTable[center] < idl) {
          begin = center + 1;
        } else {
          end = center - 1;
        }
      }
    }

    return 0;
  }
}
