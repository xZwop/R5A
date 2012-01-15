class Replica {
  static final num BASE = 100;
  static final num BOUNDARY = 10;

  String name;
  int identifier;
  int clock;
  Replica neighbor;
  List<LineIdentifier> idTable;

  Replica(this.name, this.identifier) {
    this.clock = 1;
    this.neighbor = null;
    this.idTable = new List<LineIdentifier>();

    this.idTable.add(LineIdentifier.firstIDL());
    this.idTable.add(LineIdentifier.lastIDL());

    document.query('#label_' + this.name).innerHTML = this.name;
    document.query('#' + this.name).on.keyUp.add(logoot, false);
  }
  
  void setNeighbor(Replica neighbor) {
    this.neighbor = neighbor;
  }

  // FIXME considere que tout est ajout, pour l'instant
  void logoot(event) {
    print('===================================================================');
    for(int i = 0; i < idTable.length; ++i) {
      print(idTable[i]);
    }
    print('-------------------------------------------------------------------');
    int keyCode = event.keyCode;
    var charCodes = new List<int>(); charCodes.add(event.keyCode);
    String content = new String.fromCharCodes(charCodes);
    
    // recherche de la position du nouveau caractere
    int position = getCursorPosition(); // TODO
    // generation du LineIdentifier du nouveau caractere
    List<LineIdentifier> lineIds = generateLineIdentifiers(idTable[position - 1], idTable[position], 1, Replica.BOUNDARY);
    LineIdentifier idl = lineIds.last();
    // envoie du patch d'insertion du caractere
    List<Operation> operations = new List<Operation>();
    operations.add(new Operation(Operation.INSERTION, idl, content));
    // FIXME il faut en partie le faire sur ce replica (ajout dans la table)
    deliver(operations, this.identifier);
    neighbor.deliver(operations, this.identifier);
    print('-------------------------------------------------------------------');
    for(int i = 0; i < idTable.length; ++i) {
      print(idTable[i]);
    }
    print('===================================================================');
  }

  int getCursorPosition() {
    TextAreaElement textZone = document.query('#' + this.name);
    
    return textZone.selectionStart;
  }
  
  List<LineIdentifier> generateLineIdentifiers(LineIdentifier p, LineIdentifier q, int N, int boundary) {
    List<LineIdentifier> list = new List<LineIdentifier>();
    int index = 0;
    int interval = 0;
    int step;
    Prefix r;
    
    // FIXME Si interval est negatif, alors la boucle est infinie...
    //       prefix(p,index) peut etre superieur a prefix(q,index)...
    while(interval < N) {
      index++;
      interval = new Prefix(q, index).toInt() - new Prefix(p, index).toInt() - 1;
    }
    
    step = Math.min(interval / N, boundary);
    r = new Prefix(p, index);
    
    for(int i = 0; i < N; ++i) {
      num random = (Math.random() * (step - 1)) + 1;

      list.add(constructIdentifier(r + random.toInt(), p, q));

      r += step;
    }

    return list;
  }
  
  LineIdentifier constructIdentifier(Prefix r, LineIdentifier p, LineIdentifier q) {
    LineIdentifier id = new LineIdentifier();

    for(int i = 0; i < r.length(); ++i) {
      int d = r[i];
      int s;
      int c;
      
      if(p.length() > i && d == p[i].digit) {
        s = p[i].repid;
        c = p[i].clock;
      } else if(q.length() > i && d == q[i].digit) {
        s = q[i].repid;
        c = q[i].clock;
      } else {
        s = this.identifier;
        c = this.clock++;
      }
      
      id.conc(new Position(d, s, c));
    }
    
    return id;
  }
  
  void deliver(List<Operation> patch, int identifier) {
    var textZone = document.query('#' + this.name);

    for(int i = 0; i < patch.length; ++i) {
      Operation op = patch[i];
      
      if(op.type == Operation.INSERTION) {
        // cherche l'identifiant precedant le nouvel identifiant
        int position = closest(op.id);
        String text = textZone.value;
        
        if(!(this.identifier == identifier)) {
          // insertion du content a la position, dans le textarea
          text = text.substring(0, position) + op.content + text.substring(position);
          textZone.value = text;
        }
        // ajout du nouvel identifiant, a la bonne position
        idTable.insertRange(position + 1, 1, op.id);
      } else if(op.type == Operation.DELETION) {
        // TODO recherche de la position de l'insertion du caractere
        // TODO si la suppression n'a pas deja ete faite par une operation concurante
        // TODO --> suppression du caractere a la position
        // TODO --> suppression de l'identifiant de la table des identifiants
      }
    }
  }
  
  // TODO appliquer l'algorithme de recherche dicotomique
  int closest(LineIdentifier idl) {
    int index = 0;

    for(int i = 0; i < idTable.length; ++i) {
      if(idTable[i] < idl) {
        index = i;
      } else {
        break;
      }
    }

    return index;
  }
}
