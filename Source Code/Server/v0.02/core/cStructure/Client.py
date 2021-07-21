import json
import traceback

class Client:
    def __init__(self, conn):
        self.addr = conn[1]
        self.connsd = conn[0]
        self.status = 0
        self.uid = ""
        self.lastRecv = None
        self.readLastRecv = False
        self.leftOver = ""
        self.connsd.settimeout(2)

    def sendObj(self, obj):
        msg = json.dumps(obj)
        msg = str(len(msg)) + "\n" + msg 
        print("Sent to {uid}".format(uid=self.uid), msg)
        self.connsd.send(msg.encode())

    def recvObj(self):
        readnxt = 1024
        msg = ""
        while readnxt > 0:
            data = self.connsd.recv(readnxt).decode()
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
        print("Recvied ", self.lastRecv)
        return self.lastRecv


    def close(self):
        print("Connection CLosed For", self.uid)
        self.connsd.close()
