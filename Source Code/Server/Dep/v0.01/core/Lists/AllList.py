from ..Handler import Exceptions
import time

class AllList:
    def __init__(self):
        self.AllListTableInstance = None
    
    
    def registerClient(self, regobj):
        status = self.AllListTableInstance.addRow(
            uid=regobj["uid"],
            password=regobj["pass"],
            discovery=regobj["discovery"],
            pb_name=regobj["pbobj"]["name"],
            pb_about=regobj["pbobj"]["about"],
            timeadded=int(time.time()),
            blocked=0
        )
        if not status:
            raise Exceptions.UID_ALEADY_EXIST() 
        return status

    def terminateClient(self, uid):
        self.AllListTableInstance.removeRow(
            uid=uid
        )
    
    def getPbClientByUID(self, uid):
        row = self.AllListTableInstance.fetchRowByUID(
            uid=uid
        )
        if row[3] == False:
            raise Exceptions.Client_Dont_Exist()
        pbclientobj = {}
        pbclientobj["uid"] = row[1]
        pbclientobj["pbobj"] = {}
        pbclientobj["pbobj"]["name"] = row[4]
        pbclientobj["pbobj"]["about"] = row[5]
        return pbclientobj

    def checkClient(self, loginobj):
        row = self.AllListTableInstance.fetchRow(
            uid=loginobj["uid"],
            password=loginobj["pass"]
        )

        if row[7] > int(time.time()):
            raise Exceptions.ClientIsBlocked()
        return row


    def deleteClient(self, loginobj):
        status = self.AllListTableInstance.removeRow(
            uid=loginobj["uid"],
            password=loginobj["pass"]
        )
        return status