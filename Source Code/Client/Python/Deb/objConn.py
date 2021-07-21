import conn, socket
import json


class ConnObj:
    def __init__(self):
        self.conn = conn.Cli()
        self.retry_a = 5
        self.isconnected = True
    def setIP(self, ip, port):
        self.conn.setSock(ip, port)
    
    def retry(self):
        retry_a = self.retry_a
        while retry_a or self.isconnected:
            self.conn.reconnect()
            retry_a -= 1
        if not self.isconnected :
            raise Exception("Cant Connect To Server")
    def reconnect(self):
        try:
            self.conn.reconnect()
            self.isconnected = True
        except :
            self.isconnected = False
    def connect(self):
        try:
            self.conn.connect()
            self.isconnected = True
        except Exception as e:
            print(e)
            self.isconnected = False

    def close(self):
        self.isconnceted = False
        try:
            self.conn.cli.close()
        except Exception as e:
            print(e)
    def recvObj(self):
        try:
            data = self.conn.recvData(1024)
            if data :
                data = data.decode()
            else:
                raise Exception("Not Valid Data Recived")
            num = ""
            for ch in data:
                if ch == '\n':
                    break
                num += ch
            sizeobj = int(num)
            nextread = sizeobj + len(num) + 1 - 1024
            body = data[len(num):]
            if nextread > 0:
                body += self.conn.recvData(nextread).encode()
            return json.loads(body)
        except socket.error :
            self.isconnected = False
            return None
        except Exception as e:
            return None
    
    def SendAndRecv(self, objsend):
        self.sendObj(objsend)
        recvobj = self.recvObj()
        if not self.isconnected :
            self.reconnect()
            self.sendObj(objsend)
            recvobj = self.recvObj()
        return recvobj



    def sendObj(self, obj):
        js = json.dumps(obj)
        data = str(len(js)) + "\n" + js
        try:
            self.conn.sendToServer(data)
            return True
        except Exception as e:
            return False