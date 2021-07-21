from core.utils.validator import validateObj
from .Client import Client
from ..utils import validator, cook
from ..utils.validator import validateObj
from . import Exceptions

CLOSE_CLIENT = 1
ACTIVE_CLIENT = 0


   
class HandleContext:
    def __init__(self):
        self.status = CLOSE_CLIENT
        self.sndobj = None
        self.sock = ""
    
class CliHandler:
    def __init__(self, cli):
        self.AllListInstance = None
        self.ActiveListInstance = None
        self.cli = cli
        self.hndc = HandleContext()
    
    def __defaultExceptionHandle(self):
        self.hndc.status = CLOSE_CLIENT
        self.hndc.sndobj = cook.cstatusObj(cook.SOME_ERROR_OCCURED)
    
    def __RegisterClient(self, regobj):
        try:
            validateObj(regobj)
            self.AllListInstance.addClient(regobj)
            self.hndc.status = CLOSE_CLIENT
            self.hndc.sndobj = cook.cstatusObj(cook.SUCESS)
        except Exceptions.InvalidObj:
            self.hndc.sndobj = cook.cstatusObj(cook.MALFORMED_OBJ)
        except :
            self.__defaultExceptionHandle()

    def __AuthClient(self, authobj):
        try:
            validateObj(authobj)
            authobj["sock"] = self.cli.addr[0] + ":" + str(self.cli.addr[1])
            tokenobj = self.ActiveListInstance.clientLogin(authobj)
            self.hndc.status = ACTIVE_CLIENT
            self.hndc.sndobj = tokenobj
        except Exceptions.InvalidObj:
            self.hndc.status = CLOSE_CLIENT
            self.hndc.sndobj = cook.cstatusObj(cook.MALFORMED_OBJ)
        except Exceptions.AuthFailed:
            self.hndc.status = CLOSE_CLIENT
            self.hndc.sndobj = cook.cstatusObj(cook.AUTH_FAILED)
        except :
            self.__defaultExceptionHandle()

    def __FindPeerClient(self, findpeerobj):
        try:
            validateObj(findpeerobj)
            self.ActiveListInstance.AuthClient(findpeerobj["tokenobj"])
            pobj = self.AllListInstance.FindPeerByUID(findpeerobj["uid"])
            if not pobj["discover"] :
                raise Exceptions.PeerNotFound()
            self.hndc.sndobj = cook.Obj("peerinfoobj", cook.SUCESS, {
                "puid" : pobj["uid"],
                "pbinfo" : {
                    "name" : pobj["pbinfo_name"],
                    "about" : pobj["pbinfo_about"]
                }
            })
        except Exceptions.PeerNotFound as e:
            self.hndc.sndobj = cook.statusObj({
                "code" : 404,
                "msg" : e
            })
        except Exceptions.AuthFailed:
            self.hndc.status = CLOSE_CLIENT
            self.hndc.sndobj = cook.cstatusObj(cook.AUTH_FAILED)
        except :
            self.__defaultExceptionHandle()

    def __ConnectPeerClient(self, connectpeerobj):
        '''
        Validate Connect Peer Obj
        Autheticate The Client
        Search For Peer In Active List
        If Found Send Peer Then
        Set PeerConnReq In Client ActiveList To PeerID And Check For Peer If PeerConnReq For Peer Is Client If So Then
        Send Both Peer ConnectionObj
        Else Send Peer A ConnPeerReq
        And Send Client To Wait Until Peer Answer The Request.
        '''
        try:
            validateObj(connectpeerobj)
            row = self.ActiveListInstance.AuthClient(connectpeerobj["tokenobj"])
            if row["uid"] == connectpeerobj["puid"] :
                Exceptions.PeerNotFound()
            aprow = self.ActiveListInstance.FindPeerByUID(connectpeerobj["puid"])
            if aprow["puid"] == row["uid"] :
                #Send ConnectionObj To Both Client
                pass
            else:
                #Send Peer A COnnection Obj
                #Send Client A On Hold StatusObj
                pass
        except Exceptions.InvalidObj:
            pass
        except Exceptions.PeerNotFound:
            pass
        except :
            self.__defaultExceptionHandle()
            
    
    def __UpdateClient(self, updobj):
        '''
        Validate updobj
        Authenticate Client
        And Update If Nessary

        Its Not Necesssary For Server To Reply,
        To That Object Just Update The Last Active Field Without
        Authentication
        
        '''

    def __ConfigClient(self, configobj):
        pass

    def HandleObj(self,obj):
        hnd = {
            "reg" : self.__RegisterClient,
            "upd" : self.__UpdateClient,
            "conf" : self.__ConfigClient,
            "findpeer" : self.__FindPeerClient,
            "connect" : self.__ConnectPeerClient ,
            "auth" : self.__AuthClient
            
        }
        func = hnd.get(obj["type"], None)
        sobj = None
        if func :
            func(obj["obj"][obj["reg"]["obj"]["objname"]])
        else:
            self.hndc.sndobj = cook.cstatusObj(cook.UNKNOWN_TYPE)
        


    def handle(self):
        self.cli.setRecvTimeout(0.2)
        obj = self.cli.recvObj()
        
        
        self.HandleObj(obj)
        self.cli.setCliHandleContext(self.hndc)
        if self.cli.hndc.sendobj :
            self.cli.sendObj(self.cli.hndc.sendobj)

        return self.hndc.status
        


        
