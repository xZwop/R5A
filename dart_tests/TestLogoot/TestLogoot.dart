#import('dart:html');

#source('Replica.dart');
#source('Operation.dart');
#source('Position.dart');
#source('Prefix.dart');
#source('LineIdentifier.dart');

class TestLogoot {

  TestLogoot() {
  }

  void run() {
    var client1 = new Replica('client1', 1);
    var client2 = new Replica('client2', 2);
  }
}

void main() {
  new TestLogoot().run();
}