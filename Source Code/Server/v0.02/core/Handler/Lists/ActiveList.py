import secrets, time
from .. import Exceptions
from ...Db import Table



class ActiveList:
    TOKEN_LEN = 32
    def __init__(self, alllistinstance):
        self.AllListInstance = alllistinstance
        self.ActiveListTableInstance = Table.Table("ActiveList", ["sid", "uid", "tid", "tstr", "tsadded", "dvalid", "puid", "sock"])
        self.mapClient = {}
        

    def __generateTokenobj(self, uid):
        return {
            "uid" : uid,
            "tid" : secrets.token_urlsafe(ActiveList.TOKEN_LEN),
            "tstr" : secrets.token_urlsafe(ActiveList.TOKEN_LEN)
        }

    def loginClient(self, loginobj, client):
        cobj = self.AllListInstance.authClient(loginobj)
        row = self.ActiveListTableInstance.fetchRow(uid=cobj["uid"])
        if not row :
            tobj = self.__generateTokenobj(cobj["uid"])
            obj = tobj.copy()
            obj.update({
                "tsadded" : int(time.time()),
                "dvalid" : 30 * 60,
                "sock" : loginobj["sock"]
            })
            if not self.ActiveListTableInstance.addRow(**obj) :
                raise Exceptions.LoginError()
        else:
            tsadded = row["tsadded"]
            dvalid = row["dvalid"]
            curtime = int(time.time())
            if (tsadded + dvalid) > curtime :
                tobj = {
                    "uid" : row["uid"],
                    "tid" : row["tid"],
                    "tstr" : row["tstr"]
                }
                self.ActiveListTableInstance.updateRow(Table.more(sock=loginobj["sock"]),Table.more(uid=row["uid"]))
                client.uid = cobj["uid"]
                self.mapClient[cobj["uid"]] = client
                return tobj
            else: 
                self._terminateUID(row["uid"])
                raise Exceptions.LoginTimeOut()
        self.mapClient[cobj["uid"]] = client
        client.uid = cobj["uid"]
        return tobj
    
    def authClient(self, tobj):
        cobj = self.ActiveListTableInstance.fetchRow(uid=tobj["uid"])
        if cobj :
            if cobj["tid"] == tobj["tid"] and cobj["tstr"] == tobj["tstr"]:
                tsvalid = int(cobj["tsadded"]) + int(cobj["dvalid"])
                currtime = int(time.time())
                if tsvalid > currtime :
                    return cobj
                else:
                    self.ActiveListTableInstance.deleteRow(uid=tobj["uid"])
                    raise Exceptions.TokenExpired()    
        raise Exceptions.InvalidToken()
    
    def renewClient(self, tobj):
        cobj = self.ActiveListTableInstance.fetchRow(uid=tobj["uid"])
        if cobj :
            if cobj["tid"] == tobj["tid"] and cobj["tstr"] == tobj["tstr"]:
                tsvalid = int(cobj["addedts"]) + int(cobj["valid"] / 2)
                currtime = int(time.time())
                if tsvalid > currtime :
                    ntobj = self.__generateTokenobj(tobj["uid"])
                    if not self.ActiveListTableInstance.updateRow(ntobj, Table.more(uid=tobj["uid"])):
                        raise Exception("Some Thing Gone Wrong")
                    cobj.update(ntobj)
                    return cobj
                else:
                    raise Exception("Cant Renew Yet")
            else:
                raise Exception("User Not Authenticated")
        else:
            raise Exception("User Not Loged In")
    
    def _findClient(self, uid):
        client = self.mapClient.get(uid, None)
        cobj = self.ActiveListTableInstance.fetchRow(uid=uid)
        if not cobj or not client:
            raise Exceptions.PeerNotExist()
        return (client, cobj)
    
    def setPuidClient(self, connectpeerobj):
        tobj = connectpeerobj["tokenobj"]
        cobj = self.authClient(self, tobj)
        if not self.ActiveListTableInstance.updateRow(Table.more(puid=connectpeerobj["puid"]), Table.more(uid=tobj["uid"], )):
            raise Exception("Something Gone Wrong")
    
    def setPuid(self, uid, puid):
        if not self.ActiveListTableInstance.updateRow(Table.more(puid=puid), Table.more(uid=uid)):
            raise Exception("Something Gone Wrong")

    def updateClient(self, updateobj):
        tobj = updateobj["tokenobj"]
        self.authClient(tobj)
        self.ActiveListTableInstance.updateRow(Table.more(sock=updateobj["sock"]), Table.more(uid=tobj["uid"]))

    def logoutClient(self, tobj):
        self.authClient(tobj)
        self._terminateUID(tobj["uid"])
    
    def _terminateUID(self, uid):
        self.ActiveListTableInstance.deleteRow(uid=uid)
        try:
            self.mapClient.pop(uid)
        except:
            pass