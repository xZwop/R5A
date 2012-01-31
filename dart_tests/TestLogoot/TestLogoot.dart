#import('dart:html');

#source('Replica.dart');
#source('Operation.dart');
#source('Position.dart');
#source('LineIdentifier.dart');
#source('LogootPatch.dart');
#source('diff_match_patch/DiffClass.dart');
#source('diff_match_patch/PatchClass.dart');
#source('diff_match_patch/EncodeDecode.dart');
#source('diff_match_patch/DMPClass.dart');

class TestLogoot {
  SendPort _p1;
  SendPort _p2;

  TestLogoot() {
    this._p1 = null;
    this._p2 = null;
  }
  
  void onClient1Complete(SendPort port) {
    this._p1 = port;
    
    continueIfReady();
  }
  
  void onClient2Complete(SendPort port) {
    this._p2 = port;
    
    continueIfReady();
  }
  
  void continueIfReady() {
    if(this._p1 != null && this._p2 != null) {
      this._p1.call({'id' : Replica.INIT, 'args' : ['client1', 1, this._p2]});
      this._p2.call({'id' : Replica.INIT, 'args' : ['client2', 2, this._p1]});
    }
  }

  void run() {
    document.query('#label_client1').innerHTML = 'Client1';
    document.query('#label_client2').innerHTML = 'Client2';

    Future<SendPort> pc1 = new Replica().spawn();
    Future<SendPort> pc2 = new Replica().spawn();

    pc1.then(onClient1Complete);
    pc2.then(onClient2Complete);
  }
}

void main() {
  new TestLogoot().run();
}
