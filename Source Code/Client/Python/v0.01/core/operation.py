from . import cook, conn
import threading



class Operation:
    userinfo = {
        "tokenvalid" : False,
        "loginvalid" : False,
        "uid" : "",
        "password" : "",
        "pbinfo" : {},
        "discover" : False,
        "tokenobj" : {}
    }
    peerdata = {}
    RECIVEDMESSAGE = False
    MESSAGETYPE = None
    MESSAGESTATUS_SUCESS = False
    StatusString = ""
    MISC_STRING = ""
    RESET = False

    def __init__(self):
        self.ServerConnectionInstance = conn.ServerConn("localhost", 5000)
        self.ServerConnectionInstance.onRecvObjCallback = self.onRecv
        self.ServerConnectionInstance.unexpectedCallback = self.ErrorConn
        self.sem = 0
        self.recvLock = threading.Lock()
        self.recvCV = threading.Condition(self.recvLock)
        self.rediector = {
            "status" : self.__onStatusRecived,
            "peerinfo" : self.__onPeerRecived,
            "token" : self.__onTokenRecvied,
            "reqpeer" : self.__onReqPeer, 
            "connection" : self.__onConnectionPeer
        }


    def notify(self):
        with self.recvCV:
            self.sem += 1
            self.recvCV.notifyAll()

    def wait(self):
        self.sem -= 1
        if self.sem < 0:
            with self.recvCV:
                while self.sem < 0:
                    self.recvCV.wait()

    def handleLogin(self, loginobj):
        self.sem = 0
        self.ServerConnectionInstance.connect()
        self.ServerConnectionInstance.sendObj(cook.loginObj(loginobj))
    
    def ErrorConn(self):
        Operation.RESET = True
        self.notify()

    def handleRegistration(self, regobj):
        self.sem = 0
        self.ServerConnectionInstance.connect()
        self.ServerConnectionInstance.sendObj(cook.registrationObj(regobj))
        self.ServerConnectionInstance.disconnect()

    def handleFindpeer(self, findpeerobj):
        findpeerobj["tokenobj"] = Operation.userinfo["tokenobj"]
        self.ServerConnectionInstance.sendObj(cook.findpeerObj(findpeerobj))

    def handleConnectpeer(self, connectpeerobj):
        connectpeerobj["tokenobj"] = Operation.userinfo["tokenobj"]
        self.ServerConnectionInstance.sendObj(cook.connectObj(connectpeerobj))

    
    def handleConfig(self, configobj):
        configobj["type"] = "chg"
        so = cook.configObj(configobj)
        self.ServerConnectionInstance.sendObj(so)


    def handleLogout(self):
        self.ServerConnectionInstance.sendObj(cook.configObj({
            "type" : "logout",
            "tokenobj" : Operation.userinfo["tokenobj"]
        }))


    def __onTokenRecvied(self, obj):
        Operation.userinfo["loginvalid"] = True
        Operation.userinfo["tokenvalid"] = True
        Operation.userinfo["tokenobj"].update(obj["obj"]["tokenobj"])

    def __onPeerRecived(self, obj):
        pass
    
    def __onStatusRecived(self, obj):
        Operation.StatusString = obj["obj"]["statusobj"]["msg"]

        if obj["status"] in range(200, 300):
            Operation.MESSAGESTATUS_SUCESS = True
            if obj["status"] == 201:
                Operation.userinfo["uservalid"] = True
            elif obj["status"] == 202 :
                Operation.userinfo["tokenvalid"] = False
                self.ServerConnectionInstance.disconnect()
        elif obj["status"] in range(400, 500):
            Operation.MESSAGETYPE_SUCESS = False
            if not (obj["status"] % 2):
                self.ServerConnectionInstance.disconnect()

    def __onReqPeer(self, obj):
        Operation.peerdata[obj["obj"]["peerinfo"]["puid"]] = obj
    def __onConnectionPeer(self, obj):
        Operation.peerdata[obj["obj"]["connection"]["puid"]] = obj


    def onRecv(self, obj):
        Operation.MISC_STRING = str(obj)
        type = obj["type"]
        if type not in ["reqpeer", "connection"]:
            self.notify()
        call = self.rediector.get(type, None)
        call(obj)
        Operation.RECIVEDMESSAGE = True
        Operation.MESSAGETYPE = type
    
    def close(self):
        self.ServerConnectionInstance.close()


