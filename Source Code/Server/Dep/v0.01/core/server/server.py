import socket

class Server:
    def __init__(self, sock):
        self.sip = sock[0]
        self.sport = sock[1]
        self.srv = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.srv.bind((self.sip, self.sport))
        self.srv.listen(5)

    
    def BindonConnectionMethod(self, onConnection):
        self.onConn = onConnection


    def recvConnection(self):
        conn = self.srv.accept()
        self.onConn(conn)