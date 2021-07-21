import json

class Client:
    def __init__(self, conn):
        self.addr = conn[0]
        self.connfd = conn[1]
        self.left = ""
        self.bufsize = 1024
        self.timeout = 2
        self.lastactive = 0
        self.isLoggedIn = False
        self.ClientRecord = {}
        self.lastObj = {}
    
    def sendObj(self, obj):
        pass

    def close(self):
        pass

    def recvObj(self):
        connfd = self.connfd
        self.connfd.settimeout(self.timeout)
        data = connfd.recv(self.buffsize).decode()
        ipos = 0
        num = "" 
        for ch in data:
            if ch == '\n':
                break
            num += ch
            ipos += 1

        mlen = int(num)
        off = ipos + 1
        msg = self.left
        msg += data[off : ]
        if len(msg) < mlen :
            msg += connfd.recv(mlen - len(msg)).decode()
        elif len(msg) > mlen :
            left = msg[mlen-1:]
            msg = msg[:mlen-1]

        self.left = left
        self.lastObj = json.loads(msg)

        return self.lastObj