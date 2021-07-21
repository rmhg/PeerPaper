from core.Handler import Exceptions
import secrets
from .Validator import validate
from .Lists.AllList import AllList
from .Lists.ActiveList import ActiveList
from . import cook

import traceback

CLOSE_CLIENT = 1
IDLE_CLIENT = 2
SERVICE_CLIENT = 3




class Handler:
    AllListInstance = AllList()
    ActiveListInstance = ActiveList(AllListInstance)

    def __init__(self, cli):
        self.cli = cli
        self.htypes = {
            "reg" : self.__cmRegistration,
            "upd" : self.__cmUpdate,
            "config" : self.__cmConfigruation,
            "findpeer" : self.__cmFindPeer,
            "connect" : self.__cmConnect, 
            "auth" : self.__cmAuthentication
        }
    
    def serviceIdleClient(self):
        obj = self.cli.recvObj()
        try:
            type = validate(obj)
            self.cli.readLastRecv = True
            return SERVICE_CLIENT
        except:
            return CLOSE_CLIENT

    def serviceClient(self):
        obj = None
        if self.cli.readLastRecv : 
            obj = self.cli.lastRecv
            self.cli.readLastRecv = False
        else:
            obj = self.cli.recvObj()
        try:
            type = validate(obj)
            mhandler = self.htypes.get(type)
            return mhandler(obj["obj"][obj["obj"]["objname"]])
        except Exception as e:
            self.cli.sendObj(cook.statusObj(400, "Failed"))
            traceback.print_exc()
            print("Failed At Service Client", e)
            return CLOSE_CLIENT

    def __cmRegistration(self, obj):
        sndobj = None
        try:
            Handler.AllListInstance.registerClient(obj)
            sndobj = cook.sRegistrationSucess()
        except Exceptions.CError as e:
            sndobj = cook.statusObj(e.code, e.msg)
        self.cli.sendObj(sndobj)
        return CLOSE_CLIENT
    
    def __cmFindPeer(self, obj):
        sndobj = None
        try:
            Handler.ActiveListInstance.authClient(obj["tokenobj"])
            cpobj = Handler.AllListInstance.findUID(obj["puid"])
            sndobj = cook.peerinfoObj(cpobj)
       
        except Exceptions.InvalidToken as e:
            self.cli.sendObj(cook.statusObj(e.code, e.msg))
            return CLOSE_CLIENT
        except Exceptions.CError as e:
            sndobj = cook.statusObj(e.code, e.msg)
        self.cli.sendObj(sndobj)
        return IDLE_CLIENT
            
    
    def __cmAuthentication(self, obj):
        type = obj["type"]
        try:
            if type == "new":
                loginobj = {
                    "uid" : obj["uid"],
                    "password" : obj["password"],
                    "sock" : "{0}:{1}".format(self.cli.addr[0], self.cli.addr[1])
                }
                tobj = Handler.ActiveListInstance.loginClient(loginobj, self.cli)
            else:
                tobj = Handler.ActiveListInstance.renewClient(obj["tokenobj"])
            self.cli.sendObj(cook.tokenObj(tobj))
        except Exceptions.CError as e:
            self.cli.sendObj(cook.statusObj(e.code, e.msg))
            return CLOSE_CLIENT
        return IDLE_CLIENT
            


    def __cmUpdate(self, obj):
        pass
    
    def __cmConnect(self, obj):
        try:
            cobj = Handler.ActiveListInstance.authClient(obj["tokenobj"])
            puid = obj["puid"]
            Handler.ActiveListInstance.setPuid(cobj["uid"], puid)
            (pcli, cpobj) = Handler.ActiveListInstance._findClient(puid)
            if cobj["uid"] == cpobj["puid"] : 
                usecret = secrets.token_urlsafe(10)
                psecret = secrets.token_urlsafe(10)
                uconnobj = cook.connectionObj(puid, usecret, psecret, "{0}:{1}".format(pcli.addr[0],pcli.addr[1]))
                pconnobj = cook.connectionObj(cobj["uid"], psecret, usecret, "{0}:{1}".format(self.cli.addr[0],self.cli.addr[1]))
                self.cli.sendObj(uconnobj)
                pcli.sendObj(pconnobj)
            else:
                pcli.sendObj(cook.reqpeerObj(cobj["uid"], obj["pmsg"]))
        except Exceptions.InvalidToken as e:
            self.cli.sendObj(cook.statusObj(e.code, e.msg))
            return CLOSE_CLIENT
        except Exceptions.CError as e:
            self.cli.sendObj(cook.statusObj(e.code, e.msg))
        return IDLE_CLIENT


    def __cmConfigruation(self, obj):
        type = obj["type"]
        try:
            cobj = Handler.ActiveListInstance.authClient(obj["tokenobj"])
            if type == "logout" :
                Handler.ActiveListInstance._terminateUID(cobj["uid"])
                self.cli.sendObj(cook.sLogoutSucess())
                return CLOSE_CLIENT
            elif type == "chg" :
                Handler.AllListInstance.modifyClient(obj)
                self.cli.sendObj(cook.sConfigSucess())
        except Exceptions.InvalidToken as e:
            self.cli.sendObj(cook.statusObj(e.code, e.msg))
            return CLOSE_CLIENT
        except Exceptions.CError as e:
            self.cli.sendObj(cook.statusObj(e.code, e.msg))
        return IDLE_CLIENT



