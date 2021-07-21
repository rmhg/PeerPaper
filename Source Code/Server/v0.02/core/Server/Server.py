import socket

class Server:
    def __init__(self, ip, port):
        self.ip = ip
        self.port = port
        self.srv = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.srv.bind((self.ip, self.port))
        self.srv.listen(5)
        self.onConn = callable


        
    def BindOnConnection(self, onConn):
        self.onConn = onConn


    def __onConnection(self, conn):
        self.onConn(conn)

    def start(self):
        while True:
            (addr, connsd) = self.srv.accept()
            self.__onConnection((addr, connsd))    
