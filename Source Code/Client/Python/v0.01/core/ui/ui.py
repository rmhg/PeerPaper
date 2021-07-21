
import os
from ..operation import Operation

MAIN_MENU = 0
LOGIN_MENU = 1

STATE_RUNNING = 0
STATE_WAITING = 1


def clear():
    os.system("cls")
    pass

class UI:
    statusstring = ""
    MenuIndex = MAIN_MENU
    headstring = ""
    NotificationContext = None
    def __init__(self):
        self.Menus = [
            [
                #Main Menu
                ["Login", "Registration", "Exit"],
                [self.login, self.registration, self.exit]
            ],
            [
                #Login Menu
                ["Find Peer", "Connection Request", "Change Personal Information", "Check Notfication", "Logout"],
                [self.findpeer, self.connectpeer, self.configinfo, self.checknotify, self.logout]
            ]
        ]
        self.CurrentState = STATE_RUNNING
        self.OperationInstance = Operation()


    def login(self):
        clear()
        loginObj = {
            "uid" : Operation.userinfo["uid"],
            "password" : Operation.userinfo["password"]
        }
        if not Operation.userinfo["loginvalid"] :
            loginObj = {
                "uid" : input("Enter Your UID : "),
                "password" : input("Enter Your Password : ")
            }
        Operation.userinfo.update(loginObj)
        self.OperationInstance.handleLogin(loginObj)
        self.waitTillReply()
    
    def registration(self):
        clear()
        regObj = {
            "uid" : input("Enter UID : "),
            "password" : input("Enter Password : "),
            "discover" : True if input("Do You Want To Be Discoverity (Y/N)? : ") == "Y" else False,
            "pbinfo" : {
                "name" : input("Enter Your Name (Public) : "),
                "about" : input("Enter About (Public) : ")
            }
        }
        Operation.userinfo.update(regObj)
        self.OperationInstance.handleRegistration(regObj)
        self.waitTillReply()


    def exit(self):
        self.OperationInstance.close()
        exit()


    def findpeer(self):
        clear()
        findpeerObj = {
            "puid" : input("Enter Peer UID : ")
        }
        self.OperationInstance.handleFindpeer(findpeerObj)
        self.waitTillReply()

    def connectpeer(self):
        clear()
        connectpeerobj = {
            "puid" : input("Enter Peer UID : "),
            "pmsg" : input("Enter A Peer Message : ") 
        }
        self.OperationInstance.handleConnectpeer(connectpeerobj)

    def configinfo(self):
        clear()
        configObj = {}
        arr = ["password", "discovery", "name", "about"]
        while True:
            clear()
            print("1. Change Password\n2. Change Discoverbility\n3. Change Public Name\n4. Change Public About\n5. Back")
            opt = self.getOption(1, 5)
            if opt < 3:
                configObj[arr[opt-1]] = input("Enter Value : ")
            elif opt < 5:
                configObj["pbinfo"][arr[opt - 1]] = input("Enter Value : ")
            else:
                if len(configObj) > 0:
                    self.OperationInstance.handleConfig(configObj)
                    self.waitTillReply()



    def checknotify(self):
        for i, key in enumerate(Operation.peerdata.items()):
            print("{0}. Peer : {1} - Msg : {2}", i, key, Operation.peerdata[key])
        input("Enter Option : ")

    def logout(self):
        clear()
        self.OperationInstance.handleLogout()
        self.waitTillReply()

    def __checkReset(self):
        if Operation.RESET :
            Operation.userinfo["tokenvalid"] = False
            self.MenuIndex = MAIN_MENU
            Operation.RESET = False
            self.headstring = "Server Closed The Connection Unexpectedly"
        else:
            self.headstring = ""


    def waitTillReply(self):
        print("Waiting")
        self.OperationInstance.wait()
        Operation.RECIVEDMESSAGE = False
        self.__checkReset()
        if Operation.userinfo["tokenvalid"] and self.MenuIndex == MAIN_MENU:
            self.MenuIndex = LOGIN_MENU
        elif not Operation.userinfo["tokenvalid"] and self.MenuIndex == LOGIN_MENU:
            self.MenuIndex = MAIN_MENU

    def printCurrentMenu(self):
        for i, line in enumerate(self.Menus[self.MenuIndex][0]):
            print("{0}. {1}".format(i+1, line))

    def getOption(self, start, end):
        inp = input("Enter A Option : ")
        if inp.isnumeric() :
            opt = int(inp)
            if opt in range(start, end + 1):
                return opt

    def callForCurrentMenu(self):
        inp = input("Enter A Option : ")
        if inp.isnumeric() :
            opt = int(inp)
            if opt in range(1, len(self.Menus[self.MenuIndex][1]) + 1):
                self.Menus[self.MenuIndex][1][opt - 1]()

    def notification(self):
        if len(Operation.peerdata) > 0:
            self.headstring = "Notifications : {0}".format(len(Operation.peerdata))

    def mainUI(self):
        while True:
            clear()
            self.__checkReset()
            self.notification()
            print(self.headstring, Operation.StatusString , Operation.MISC_STRING, sep="\n")
            self.printCurrentMenu()
            if self.CurrentState == STATE_RUNNING :
                self.__checkReset()
                self.callForCurrentMenu()
            





