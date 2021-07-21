import socket
import json
import threading
import traceback
import select
import time






class ServerConn:
    def __init__(self, ip, port):
        self.conn = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.ip = ip
        self.port = port
        self.leftOver = ""
        self.lastRecv = None
        self.isConnected = False
        self.onRecvObjCallback = callable
        self.unexpectedCallback = callable
        self.thread = threading.Thread(target=self.thread_work)
        self.connectlock = threading.Lock()
        self.threadcv = threading.Condition(self.connectlock)
        self.running = True
        self.conn.settimeout(2)
        self.thread.start()



    def connect(self):
        if not (type(self.conn) is socket):
            self.conn = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.conn.settimeout(2)
        with self.threadcv:
            self.conn.connect((self.ip, self.port))
            self.isConnected = True
            self.threadcv.notify_all()

    def disconnect(self):
        self.isConnected = False
        self.conn.close()

    def close(self):
        self.running = False
        self.isConnected = False
        self.conn.close()
        with self.threadcv:
            self.threadcv.notify_all()

    def setRecvTimeout(self):
        pass


    def sendObj(self, obj):
        try:
            msg = json.dumps(obj)
            msg = str(len(msg)) + "\n" + msg
            self.conn.send(msg.encode())
            return True
        except :
            self.isConnected = False
        return False


    def thread_work(self):
        while self.running:
            with self.threadcv:
                while (not self.isConnected) and self.running:
                    self.threadcv.wait()
            if not self.running :
                break
            try:
                self.__recvObj()
            except socket.timeout:
                pass
            except Exception as e:
                self.disconnect()
                self.unexpectedCallback()




    def __recvObj(self):
        readnxt = 1024
        msg = ""
        while readnxt > 0 :
            data = self.conn.recv(readnxt).decode()
            if len(data) == 0:
                raise Exception("Connection Must Be Disconnected") 
            data = self.leftOver + data
            slen = ""
            for ch in data:
                if ch == '\n':
                    break
                slen += ch
            nlen = int(slen)
            msg += data[len(slen) + 1:]
            if len(msg) > nlen :
                self.leftOver = msg[nlen:]
                msg = msg[:nlen]
            readnxt = nlen - readnxt
        self.lastRecv = json.loads(msg)
        self.onRecvObjCallback(self.lastRecv)
        