import socket
import threading

class Cli:
    def __init__(self):
        self.cli = socket.socket()
        self.conn = None    
        self.isrun = False
        self.isconnected = False
        self.ip = ""
        self.port = ""

    def setSock(self, ip, port):
        self.ip = ip
        self.port = port

    def reconnect(self):
        self.cli = socket.socket()
        self.cli.connect((self.ip, self.port))
    def connect(self):
        self.cli.connect((self.ip, self.port))

    def sendToServer(self, body):
        self.cli.send(body.encode())

    def recvData(self,length):
            return self.cli.recv(length)

    



    