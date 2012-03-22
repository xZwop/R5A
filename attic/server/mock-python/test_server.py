import server
import socket
import threading
import SocketServer

HOST = 'localhost'

class ReceiveHandler(SocketServer.StreamRequestHandler):
  '''
  Receive Handler.

  Receive Handler wait for new information from other users.
  '''
  def handle(self):
    '''
    Get data from other user.
    '''
    print self.rfile.__doc__
    self.server.client.receive(self.request.recv(1024).strip())

class Receiver(SocketServer.ThreadingTCPServer):
  '''
  Receive Service.
  '''
  def __init__(self,  server_address, RequestHandlerClass, client):
    SocketServer.ThreadingTCPServer.__init__(\
        self, server_address, RequestHandlerClass)
    self.client = client

class Client:
  '''
  Client.

  Client will use the flood server to send informations to other clients.
  '''
  port = 1337

  def start(self):
    '''
    Launch the client server on port server.RECEIVE_PORT
    '''
    receiver = Receiver((HOST, self.port), ReceiveHandler, self)

    # Start a thread with the receiver -- that thread will then start one
    # more thread for each request
    receiver_thread = threading.Thread(target = receiver.serve_forever)

    # Exit the server thread when the main thread terminates
    receiver_thread.daemon = True

    receiver_thread.start()

  def connect(self):
    '''
    Connect client to the flood server.
    '''
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
      # Register on server, send receiver port and get UniquID
      sock.connect((server.HOST, server.REGISTER_PORT))

      sock.send(str(self.port))
      self.uniquid = sock.recv(1024)

      self.start()
    finally:
      sock.close()
  
  def send(self, data):
    '''
    Send data to server.
    '''
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
      sock.connect((server.HOST, server.SEND_PORT))
      sock.send(str(data))
    finally:
      sock.close()

  def receive(self, datas):
    '''
    Callback call on receive informations.
    '''
    print "{%s} --RECEIVE--> {%s}" % (self.uniquid, datas)

  def __init__(self):
    self.uniquid = -1
    self.port = Client.port

    Client.port += 1

def runserver(server):
  server.start()

def runclients(clients):
  # Connect clients
  for client in clients:
    client.connect()

  # Flood id to other clients
  for client in clients:
    client.send(client.uniquid)

if __name__ == '__main__':
  clients = []
  for i in range(10):
    clients.append(Client())

  runclients(clients)

