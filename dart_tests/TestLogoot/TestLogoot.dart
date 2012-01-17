#import('dart:html');

#source('Replica.dart');
#source('Operation.dart');
#source('Position.dart');
#source('LineIdentifier.dart');
#source('diff_match_patch/DiffClass.dart');
#source('diff_match_patch/PatchClass.dart');
#source('diff_match_patch/EncodeDecode.dart');
#source('diff_match_patch/DMPClass.dart');

class TestLogoot {

  TestLogoot() {
  }

  void run() {
    Replica client1 = new Replica('client1', 1);
    Replica client2 = new Replica('client2', 2);
    
    client1.setNeighbor(client2);
    client2.setNeighbor(client1);
  }
}

void main() {
  new TestLogoot().run();
}