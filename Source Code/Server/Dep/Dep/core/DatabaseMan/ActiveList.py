import secrets

def calcTokenID():
    return secrets.token_urlsafe(15)
def calcTokenStr():
    return secrets.token_urlsafe(15)
sid = 0
class ActiveListRow:
    def __init__(self, *args):
        global sid
        self.sid = sid
        self.uid = args[0]
        self.puid = self.uid
        self.sock = args[1]
        self.lastupdate = 0
        self.tid = args[2]
        self.tstr = args[3]
        sid+=1

class ActiveList:
    def __init__(self, alllist):
        self.List = []
        self.allList = alllist
    def Login(self, lobj, sock):
        row = self.allList.MatchAuth(lobj)
        tokenobj = None
        print(row, " ", self.FindByUID(lobj["uid"]))
        if row and not self.FindByUID(lobj["uid"]):
            tokenobj = {}
            tokenobj["tid"] = calcTokenID()
            tokenobj["tstr"] = calcTokenStr()
            self.List.append(ActiveListRow(lobj["uid"], sock, tokenobj["tid"], tokenobj["tstr"]))
        return tokenobj
    def Logout(self, tobj):
        row = self.MatchAuth(tobj)
        if row :
            self.List.remove(row)
            return True
        return False

    def Update(self, tobj):
        pass

    def MatchAuth(self, tobj):
        for row in self.List:
            print(row.uid, row.tid, row.tstr)
            if row.tid == tobj["tid"] and row.tstr == tobj["tstr"]:
                return row
            return None

    def FindByUID(self, uid):
        for row in self.List:
            if row.uid == uid:
                return row
        return None

    def addPUIDtoUID(self, uid, puid):
        for i in range(0, len(self.List)):
            if self.List[i].uid == uid:
                self.List[i].puid = puid


    




