import json
from core.obj import cook
import secrets

class BodyHandler:
    def __init__(self, obj, activecon):
        self.body = obj[1]
        self.addr = obj[0]
        self.activeconn = activecon
    def Registration(self, obj, Lists):
        aL = Lists[0]
        if aL.addRegObj(obj) : 
            return cook.Obj("status", 200, {
                "code" : 200,
                "msg" : "sucess"
            })
        return cook.Obj("status", 400, {
                "code" : 400,
                "msg" : "not found"
            })

    def Auth(self, obj, Lists):
        activeL = Lists[1]
        token = activeL.Login(obj, self.addr[0] + ":" + str(self.addr[1]))
        if token :
            return cook.Obj("token", 200 ,token)
        return cook.Obj("status", 400, {
            "code" : 400,
            "msg" : "Wrong Details"
        })
    
    def Authenticate(self, obj, sucessfulcallback, Lists):
        if Lists[1].MatchAuth(obj) :
            return sucessfulcallback(obj, Lists) 
        return cook.Obj("status", 400 ,{
            "code" : 400,
            "msg" : "Cant Authenticate"
        })

    def FindPeer(self, obj, Lists):
        if Lists[1].MatchAuth(obj) :
            row = Lists[0].FindByUID(obj["puid"])
            if not row:
                return cook.Obj("status", 400, {
                    "code" : 400,
                    "msg" : "Cant Find Peer"
                })
            return cook.Obj("peerinfo", 200, {
                "uid" : row.uid,
                "pbinfo" : row.pbinfo
            })
        else:
            return cook.Obj("status", 400 ,{
            "code" : 400,
            "msg" : "Cant Authenticate"
        })


    def SendMessage(self,uid, obj):
        for sconn in self.activeconn:
            if sconn["uid"] == uid :
                js = json.dumps(obj)
                data = str(len(js)) + "\n" + js
                sconn["sock"][0].send(data.encode())
                

    def ConnectPeer(self, obj, Lists):
        if not Lists[1].MatchAuth(obj) :
            return cook.Obj("status", 400 ,{
            "code" : 400,
            "msg" : "Cant Authenticate"
        })
        urow = Lists[1].FindPeerByUID(obj["uid"])
        prow = Lists[1].FindPeerByUID(obj["puid"])
        if not urow and not prow:
            return cook.Obj("status", 400, {
                "code" : 400,
                "msg" : "Cant Find Any Active Peer With Given ID"
                }) 
        Lists[1].addPUIDtoUID(obj["uid"], obj["puid"])
        if prow.puid == urow.uid and urow.puid == prow.uid:
            shared = secrets.token_urlsafe(10)
            self.SendMessage(obj["uid"], cook.Obj("connection", 200, {
                "shared" : shared,
                "ipobj" : {
                    "ip" : prow.sock.split(":")[0],
                    "port" : prow.sock.split(":")[1]
                }
            }))
            self.SendMessage(obj["puid"], cook.Obj("connection", 200, {
                "shared" : shared,
                "ipobj" : {
                    "ip" : urow.sock.split(":")[0],
                    "port" : urow.sock.split(":")[1]
                }
            }))
        else:
            self.SendMessage(obj["puid"], cook.Obj("connectreq", 0, {
              "ruid" : obj["puid"],
              "msg" : obj["msg"]  
            }))

    def Logout(self, obj, Lists):
        if Lists[1].Logout(obj) :
            return cook.Obj("status", 200, {
                "code" : 200,
                "msg" : "logout Sucessfully"
            })
        return cook.Obj("status", 400, {
                "code" : 400,
                "msg" : "Failed !logout "
        })
        

    def update(self, obj,Lists):
        return cook.Obj("status", 200, {
            "code" : 200,
            "msg" : "Done"
        })
    def Update(self, obj, Lists):
        activeL = Lists[1]
        if not Lists[1].MatchAuth(obj):
            return cook.Obj("status", 400 ,{
            "code" : 400,
            "msg" : "Cant Authenticate"
        }) 
        objs = {
            "logout" : self.Logout,
            "update" : self.update
        }
        ops = objs.get(obj["type"], None)
        if ops :
            return ops(obj, Lists)
        return cook.Obj("status", 400, {
            "code" : 400,
            "msg" : "Type! Error"
        })


    def openOperation(self, Lists):
        op = {
            "reg" : self.Registration,
            "auth" : self.Auth,
            "findpeer" : self.FindPeer,
            "connectpeer" : self.ConnectPeer,
            "upd" : self.Update
        }
        ops = op.get(self.body["type"], None)
        
        if ops :
            return ops(self.body["obj"][self.body["obj"]["objname"]], Lists) 
        return cook.Obj("status", 400, {
            "code" : 400,
            "msg" : "Type! Error"
        })
