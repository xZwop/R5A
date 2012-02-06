#!/usr/bin/env python

'''
Server mock.

Server mock offer services to mock the P2P networks layer of logoot
engine. Server mock not work as P2P for distribution it only do 
floding to all client.
'''
import SocketServer
import socket
import threading

HOST = 'localhost'
REGISTER_PORT, SEND_PORT = 9991, 9992

class Flooder:
  '''
  Flooder offer services to flood users.

  Flooder offers services to flood usre over TCP network.
  '''

  def add(self, client_address, receiver_port):
    '''
    Add new user for flooding and return unique id.
    '''
    if not self.__flood.has_key(client_address):
      print "New for flood: %s to flood on port %s "\
          % (client_address, receiver_port)
      self.__flood[client_address] = receiver_port

    return "%s%d" % (client_address[0], client_address[1])

  def flood(self, data):
    '''
    Flood data to all users.
    '''
    for client_address, receiver_port in self.__flood.iteritems():
      sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      try:
        sock.connect((client_address[0], receiver_port))
        sock.send(data)
      except Exception as e:
        pass
      finally:
        sock.close()

  def __init__(self):
    self.__flood = {}

class RegistrationHandler(SocketServer.BaseRequestHandler):
  '''
  Registration Handler.

  Registration handler register a new client on the logoot service. The
  registration is made over TCP protocol.
  '''
  def handle(self):
    '''
    Register client for flood.
    '''
    try:
      port = int(self.request.recv(1024))
      uniquid = self.server.flooder.add(self.client_address, port)
      self.request.send(uniquid)
    except ValueError:
      pass

class Register(SocketServer.ThreadingTCPServer):
  '''
  Registration Service.
  '''
  def __init__(self,  server_address, RequestHandlerClass, flooder):
    SocketServer.ThreadingTCPServer.__init__(\
        self, server_address, RequestHandlerClass)
    self.flooder = flooder

class SendHandler(SocketServer.BaseRequestHandler):
  '''
  Send Handler.

  Send Handler flood to all registred client data given. The flood is made over
  TCP protocol.
  '''
  def handle(self):
    '''
    Flood data to other registred clients.
    '''
    data = self.request.recv(1024)
    print data
    flood_thread = threading.Thread(target = self.server.flooder.flood(data))
    flood_thread.start()

class Sender(SocketServer.ThreadingTCPServer):
  '''
  Send Service.
  '''
  def __init__(self,  server_address, RequestHandlerClass, flooder):
    SocketServer.ThreadingTCPServer.__init__(\
        self, server_address, RequestHandlerClass)
    self.flooder = flooder

class Server:
  '''
  Flood Server.

  The flood server offert two services:
    * Registration, to register for futur flood.
    * Send, to flood data over networwk.
  '''
  def start(self):
    '''
    Run the server and offer a register service on port REGISTER_PORT and
    and a send data service on port SEND_PORT.
    '''
    # Start a thread with the register/sender -- that thread will then start one
    # more thread for each request
    register_thread = threading.Thread(target = self.__register.serve_forever)
    sender_thread = threading.Thread(target = self.__sender.serve_forever)

    register_thread.start()
    sender_thread.start()

  def __init__(self):
    self.__flooder = Flooder()
    self.__register = Register((HOST, REGISTER_PORT), RegistrationHandler,\
        self.__flooder)
    self.__sender = Sender((HOST, SEND_PORT), SendHandler, self.__flooder)

if __name__ == '__main__':
  Server().start()

