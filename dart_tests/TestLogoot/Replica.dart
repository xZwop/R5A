class Replica {
  static final num BASE = 100;
  static final num BOUNDARY = 10;

  String name;
  int identifier;
  int clock;
  Replica neighbor;
  List<LineIdentifier> idTable;

  Replica(this.name, this.identifier) {
    this.clock = 0;
    
    // TODO ajout des idls speciaux de debut et de fin
    // TODO du coup, attention a utiliser correctement la position du curseur

    document.query('#label_' + this.name).innerHTML = this.name;
    document.query('#' + this.name).on.keyUp.add(logoot, false);
  }
  
  void setNeighbor(Replica neighbor) {
    this.neighbor = neighbor;
  }

  // FIXME considere que tout est ajout, pour l'instant
  void logoot(event) {
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
    neighbor.deliver(operations);
  }

  // TODO
  int getCursorPosition() {
    var cursorPos = 0;
    var textZone = document.query('#' + this.name);
    
    // IE support
    if(textZone.selection) {
      textZone.focus();

      var sel = textZone.selection.createRange();
      sel.moveStart ('character', -textZone.value.length);
      cursorPos = sel.text.length;
      // Firefox support
    } else if(textZone.selectionStart || textZone.selectionStart == '0') {
      cursorPos = textZone.selectionStart;
    }

    return cursorPos;
  }
  
  List<LineIdentifier> generateLineIdentifiers(LineIdentifier p, LineIdentifier q, int N, int boundary) {
    List<LineIdentifier> list = new List<LineIdentifier>();
    int index = 0;
    int interval = 0;
    num step;
    Prefix r;
    
    // FIXME Si interval est negatif, alors la boucle est infinie...
    //       prefix(p,index) peut etre superieur a prefix(q,index)...
    while(interval < N) {
      index++;
      interval = new Prefix(q, index).toInt() - new Prefix(p, index).toInt();
    }
    
    step = Math.min(interval / N, boundary);
    r = new Prefix(p, index);
    
    for(int i = 1; i <= N; ++i) {
      // TODO je suis un noob, il y a peut-etre mieux
      num random = (Math.random() + 0.1) * step;
      
      if(random > step) {
        random = step;
      }
      
      list.add(constructIdentifier(r + random, p, q));
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
      
      if(d == p[i].digit) {
        s = p[i].repid;
        c = p[i].clock;
      } else if(d == q[i].digit) {
        s = q[i].repid;
        c = q[i].clock;
      } else {
        s = this.identifier;
        c = this.clock++;
      }
      
      id.conc(new Position(d, s, c));
    }
  }
  
  void deliver(List<Operation> patch) {
    for(int i = 0; i < patch.length; ++i) {
      Operation op = patch[i];
      
      if(op.type == Operation.INSERTION) {
        // cherche l'identifiant precedant le nouvel identifiant
        int position = closest(op.id);
        
        // TODO insertion du content a la position, dans le textarea
        // ajout du nouvel identifiant, a la bonne position
        idTable.insertRange(position, 1, op.id);
      } else if(op.type == Operation.DELETION) {
        // TODO recherche de la position de l'insertion du caractere
        // TODO si la suppression n'a pas deja ete faite par une operation concurante
        // TODO --> suppression du caractere a la position
        // TODO --> suppression de l'identifiant de la table des identifiants
      }
    }
  }
  
  int closest(LineIdentifier) {
    // TODO binary search
  }
}
