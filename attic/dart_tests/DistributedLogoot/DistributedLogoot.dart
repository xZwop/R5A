#import('dart:html');

#source('Replicat.dart');
#source('Position.dart');
#source('Operation.dart');
#source('LogootPatch.dart');
#source('LineIdentifier.dart');
#source('diff_match_patch/DiffClass.dart');
#source('diff_match_patch/PatchClass.dart');
#source('diff_match_patch/EncodeDecode.dart');
#source('diff_match_patch/DMPClass.dart');

class DistributedLogoot {
  static final GET_ID_URL = "http://localhost/~adrienbougouin/logoot/connection.php";
  static final SEND_URL = "http://localhost/~adrienbougouin/logoot/emission.php";
  
  XMLHttpRequest _connectionRequest;
  ReceivePort _receivePort;
  SendPort _sendPort;
  String _textAreaID;

  DistributedLogoot() {
    XMLHttpRequestEvents connectionEvents = null;
    this._receivePort = new ReceivePort();
    this._sendPort = null;
    this._connectionRequest = new XMLHttpRequest();
    this._sendPort = null;
    this._textAreaID = "textZone";

    this._connectionRequest.open("GET", DistributedLogoot.GET_ID_URL, true);
    this._connectionRequest.on.load.add(onConnectionEstablished);
    this._receivePort.receive(onSend);
  }

  void run() {
    document.query('#status').innerHTML = 'Initializing client...';
    document.query('#' + this._textAreaID).disabled = true;

    Future<SendPort> futurePort = new Replica().spawn();
    
    futurePort.then(onClientPortInitialized);
  }
  
  void onClientPortInitialized(SendPort port) {
    this._sendPort = port;
    
    connect();
  }
  
  void connect() {
    document.query('#status').innerHTML = 'Waiting for connection to server...';
    this._connectionRequest.send();
  }  

  void onConnectionEstablished(Event event) {
    String repID = this._connectionRequest.responseText;

    this._sendPort.call({'id' : Replica.INIT, 'args' : [this._textAreaID, repID, this._receivePort.toSendPort()]});
    document.query('#receiver').on.change.add(onReceive);
    document.query('#status').innerHTML = 'Client ready...';
    document.query('#' + this._textAreaID).disabled = false;
  }
  
  void onReceive(Event event) {
    print('onReceive');
    
    String message = document.query('#receiver').value;
    List<String> args = message.split('@');
    
    this._sendPort.call({'id' : Replica.DELIVER, 'args' : [args[0], args[1]]});
  }
  
  void onSend(message, SendPort replyTo) {
    print('onSend');
    String args = message['args'][0] + '@' + message['args'][1];
    XMLHttpRequest sendRequest = new XMLHttpRequest();
    
    sendRequest.open("POST", SEND_URL, true);
    sendRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    sendRequest.on.load.add((Event event) {print(sendRequest.responseText);});
    sendRequest.send("message=" + args);
  }
}

void main() {
  new DistributedLogoot().run();
}
