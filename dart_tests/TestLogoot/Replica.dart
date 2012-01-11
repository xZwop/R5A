class Replica {
  static final num BASE = 100;

  String name;
  int identifier;
  int clock;

  Replica(this.name, this.identifier) {
    this.clock = 0;

    document.query('#label_' + this.name).innerHTML = this.name;
    document.query('#' + this.name).on.keyUp.add(logoot, false);
  }

  void logoot(event) {
    var char = new List<int>();
    char.add(event.keyCode);
    document.query('#label_' + this.name).innerHTML = new String.fromCharCodes(char);
  }
  
  List<Position> generateLineIdentifiers(List<Position> p, List<Position> q, int N, int boundary) {
    List<Position> list = new List<Position>();
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
      
      list.addAll(constructIdentifier(r + random, p, q));
      r += step;
    }

    return list;
  }
  
  List<Position> constructIdentifier(Prefix r, List<Position> p, List<Position> q) {
    List<Position> id = new List<Position>();
    
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
      
      id.add(new Position(d, s, c));
    }
  }
  
  void deliver(List<Operation> patch) {
    for(int i = 0; i < patch.length; ++i) {
      Operation op = patch[i];
      
      if(op.type == Operation.INSERTION) {
        // TODO recherche de la position de l'insertion du caractere
        // TODO insertion du caracter dans le client
        // TODO insertion dans la table des identifiants
      } else if(op.type == Operation.DELETION) {
        // TODO recherche de la position de l'insertion du caractere
        // TODO si la suppression n'a pas deja ete faite par une operation concurante
        // TODO --> suppression du caractere a la position
        // TODO --> suppression de l'identifiant de la table des identifiants
      }
    }
  }
}
