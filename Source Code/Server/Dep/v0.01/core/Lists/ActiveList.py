import secrets
import time
from ..Handler import Exceptions 



class ActiveList:
    def __init__(self):
        self.AllListInstance = None
        self.ActiveListTable = None
        


    def clientLogin(self, loginobj):
        self.AllListInstance.checkClient(loginobj)
        tokenobj = self.__AddClient(loginobj)
        return tokenobj

    
    def __AddClient(self, loginobj):
        tokenobj = {}
        tokenobj["uid"] = loginobj["uid"]
        tokenobj["tid"] = secrets.token_urlsafe(15)
        tokenobj["tstr"] = secrets.token_urlsafe(15)
        self.ActiveListTable.addRow(
            uid=loginobj["uid"],
            tid=tokenobj["tid"],
            tstr=tokenobj["tstr"],
            timeadded=int(time.time()),
            validity=1800,
            sock=loginobj["sock"],
            puid=None
        )
        return tokenobj

    def clientAuthenticate(self, tokenobj):
        row = self.ActiveListTable.fetchRow(tokenobj)
        if not row:
            raise Exceptions.AuthFailed()
        if int(time.time()) - row[3] >= row[4] :
            self.ActiveListTable.removeRow(tokenobj)
            raise Exceptions.TimeOut()
        return row

    def clientRemove(self, tokenobj):
        self.ActiveListTable.removeRow(**tokenobj)
    
    def updateSockClient(self, updateobj):
        tokenobj = updateobj["tokenobj"]
        self.clientAuthenticate(self, tokenobj)
        self.ActiveListTable.updateRoWByUID(
            uid=tokenobj["uid"],
            sock=updateobj["sock"]
        )

    def clientRenew(self, tokenobj):
        row = self.clientAuthenticate(tokenobj)
        tokenobj = {}
        tokenobj["tid"] = secrets.token_urlsafe(15)
        tokenobj["tstr"] = secrets.token_urlsafe(15)
        status = self.ActiveListTable.updateRowByUID(
            uid=tokenobj["uid"],
            tid=tokenobj["tid"],
            tstr=tokenobj["tstr"],
            timeadded=int(time.time())
        )
        if not status :
            raise Exceptions.SOME_ERROR_OCCURED()
        return tokenobj

    def getActiveClient(self, uid):
        row = self.ActiveListTable.fetchRowByUID(uid)
        if row :
            return {
                "uid" : row[0],
                "sock" : row[5],
                "puid" : row[6]
            }
        return None
    
    def setClientPUID(self, uid, puid):
        status = self.ActiveListTable.updateRowByUID(
            uid=uid,
            puid=puid
        )
        return status

    def clientLogout(self, tokenobj):
        self.clientAuthenticate(tokenobj)
        self.clientRemove()


        

    
    