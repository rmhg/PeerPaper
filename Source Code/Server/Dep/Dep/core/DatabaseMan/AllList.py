

sid = 0

class AllListRow:
    def __init__(self, *args):
        global sid
        self.sid = sid
        self.uid = args[0]
        self.pbinfo = args[3]
        self.passphrase = args[1]
        self.salt = ""
        self.visible = args[2]
        sid+=1

class AllList:
    def __init__(self):
        self.List = []
    def addRegObj(self, regobj):
        if not self.FindByUID(regobj["uid"]) :
          self.List.append(AllListRow(regobj["uid"], regobj["pass"],regobj["visible"], regobj["pbinfo"]))
          return True
        else:
            return False
    def FindByUID(self, uid):
        for row in self.List:
            if row.uid == uid :
                return row
        return None

    def MatchAuth(self, authobj):
        row = self.FindByUID(authobj["uid"])
        if row :
            if row.passphrase == authobj["pass"]:
                return row
        return None

    def ModifyRow(self, obj):
        pass
    
    def DeleteRowByUID(self, uid):
        pass