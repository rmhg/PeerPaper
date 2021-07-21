from core.Db.DbConn import connect
import time
from ...Db import Table
from core.Handler import Exceptions


class AllList:
    def __init__(self):
        self.AllListTableInstance = Table.Table("AllList", ["sid", "uid", "password", "discover", "pbname", "pbabout", "tsblocked", "tsadded"])

    def registerClient(self, regobj):
        nregobj = {
            "uid" : regobj["uid"],
            "passphrase" : regobj["password"],
            "discover" : regobj["discover"],
            "pbname" : regobj["pbinfo"]["name"],
            "pbabout" : regobj["pbinfo"]["about"],
            "tsblocked" : 0
        }
        if not self.AllListTableInstance.addRow(**nregobj) :
            raise Exceptions.RegistrationErrorUIDExist()

    def authClient(self, loginobj):
        cobj = self.AllListTableInstance.fetchRow(uid=loginobj["uid"], passphrase=loginobj["password"])
        if cobj :
            blockts = int(cobj["tsblocked"])
            cts = int(time.time())
            if blockts >= cts:
                raise Exceptions.LoginErrorBlocked()
            return cobj
        raise Exceptions.LoginCredError()
    
    def modifyClient(self, confobj):
        mconfobj = {}
        mutable = ["password", "pbinfo", "discover"]
        for keys, value in confobj.items():
            if keys in mutable:
                if "password" == keys:
                    mconfobj["passphrase"] = value
                elif "pbinfo" == keys:
                    for k, v in value.items():
                        mconfobj["pb" + k] = v
                else:
                    mconfobj[keys] = value
        status = self.AllListTableInstance.updateRow(mconfobj, Table.more(uid=confobj["uid"]))
        if not status :
            raise Exceptions.UnknownError()

    def findUID(self, uid):
        cobj = self.AllListTableInstance.fetchRow(uid=uid)
        if not cobj:
            raise Exceptions.PeerNotExist()
        return {
            "puid" : cobj["uid"],
            "pbinfo" : {
                "name" : cobj["pbname"],
                "about" : cobj["pbabout"]
            }
        }

    def deleteClient(self, loginobj):
        pass

    def clientExist(self, uid):
        pass

    def terminateUID(self, uid):
        self.AllListTableInstance.deleteRow(uid=uid)

