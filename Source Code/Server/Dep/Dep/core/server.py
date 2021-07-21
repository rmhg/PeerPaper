import socket
import threading

class Server:
    def __init__(self):
        self.srv = socket.socket()
        self.srv.bind(("localhost", 5000))
        self.srv.listen(10)
        self.th = threading.Thread(target=self.listen)
        self.onConnection = callable

        self.isrun = False

    def start(self):
        self.isrun = True
        self.th.start()

    def stop(self):
        self.isrun = False
        self.th.join()
    def listen(self):
        while self.isrun:
            conn = self.srv.accept()
            self.onConnection(conn)

    
