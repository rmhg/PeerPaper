import cook
import objConn
import os
import socket 
import time
import threading

Conn = None
tokencred = {
    "done" : False,
    "uid" : "",
    "tid" : "",
    "tstr" : ""
}
usercred = {
    "done" : False,
    "uid" : "",
    "pass" : ""
}

def updMessage(Conn):
    while tokencred["done"] :
        try:    
            Conn.SendAndRecv(cook.Obj("upd", 0, {
                "type" : "update",
                "uid" : tokencred["uid"],
                "tid" : tokencred["tid"],
                "tstr" : tokencred["tstr"]
            }))
            time.sleep(1)
        except:
            pass

def connectpeer(Conn):
    os.system("cls")
    connectpeerobj = {
        "puid" : input("Enter Peer ID : "),
        "msg" : input("Enter Message(optional) : "),
        "uid" : tokencred["uid"],
        "tid" : tokencred["tid"],
        "tstr" : tokencred["tstr"]
    }
    recvobj = Conn.SendAndRecv(cook.Obj("connectpeer", 0, connectpeerobj))
    if recvobj["status"] == 200:
        #sucess
        pass
    else:
        print("Failed,",recvobj["obj"]["statusobj"]["errormsg"] , " Press Any Key To Go Back")
        input()
        UserMenu(Conn)

def logout(Conn):
    updateobj = {
        "type" : "logout",
        "uid" : tokencred["uid"],
        "tid" : tokencred["tid"],
        "tstr" : tokencred["tstr"]
    }
    Conn.SendAndRecv(cook.Obj("upd", 0, updateobj))
    tokencred["done"] = False
    thud.join()
    mainUI(Conn)

def UserMenu(Conn):
    os.system("cls")
    print("1. Find Peer")
    print("2. Connected Peer")
    print("3. Logout")
    print("4. Exit")

    inp = int(input())
    if inp == 4:
        exit()
    menu = [findpeer, connectpeer, logout]
    menu[inp - 1](Conn)
    
def findpeer(Conn):
    os.system("cls")
    findpeerobj = {
        "puid" : input("Enter Peer UID : "),
        "uid" : tokencred["uid"],
        "tid" : tokencred["tid"],
        "tstr" : tokencred["tstr"]
    }
    try:
         recvobj = Conn.SendAndRecv(cook.Obj("findpeer", 0, findpeerobj))
         if recvobj["status"] == 200:
             print("Peer User Id : ", recvobj["obj"]["peerinfo"]["uid"])
             print("Peer Name : ", recvobj["obj"]["peerinfo"]["pbinfo"]["name"])
             print("Peer About : ", recvobj["obj"]["peerinfo"]["pbinfo"]["about"])
         else:
            print("Failed ", recvobj["obj"]["statusobj"]["errormsg"], " Press Any Key To Go Back")
    except:
        pass
    finally:
        input("Press Any Key To Continue")
        UserMenu(Conn)
def login(Conn):
    os.system("cls")
    loginobj = {
        "uid" : usercred["uid"] if usercred["done"] == True else input("Enter UID : "),
        "pass" : usercred["pass"] if usercred["done"] == True else input("Enter Password : ")
    }
    try:
        recvobj = Conn.SendAndRecv(cook.Obj("auth", 0, loginobj))

        if recvobj["status"] == 200 :
            tokencred["done"] = True
            tokencred["uid"] = loginobj["uid"]
            tokencred["tid"] = recvobj["obj"]["tokenobj"]["tid"]
            tokencred["tstr"] = recvobj["obj"]["tokenobj"]["tstr"]
            thud.start()
            UserMenu(Conn)
        else:
            usercred["done"] = False
            print("Login Failed",recvobj["obj"]["statusobj"]["errormsg"] , " Press Any Key To Go Back")
            input()
            mainUI(Conn)
    except Exception as e:
        Conn.conn.isconnected = False
        print(e)
    
def registration(Conn):
    os.system("cls")
    regobj = {
        "uid" : input("Enter UID : "),
        "visible" : True if input("Do You Want To Be Discover (Y/N) : ") == 'Y' else False,
        "pass" : input("Enter A Password : "),
        "pbinfo" : {
            "name" : input("Your Name (public) : "),
            "about" : input("About (Public) : ")
        }
    }
    
    recvobj = Conn.SendAndRecv(cook.Obj("reg", 0, regobj))  
    if recvobj["status"] == 200 :
        usercred["done"] = True
        usercred["uid"] = regobj["uid"]
        usercred["pass"] = regobj["pass"]
        print("Registration Was Sucessful!, Press Any Key To Go Back")
        input()
        mainUI(Conn)
    else:
        print("Registration Failed,",recvobj["obj"]["statusobj"]["errormsg"] , " Press Any Key To Go Back")
        input()
        mainUI(Conn)


    


def mainUI(Conn):
    global thud
    os.system("cls")
    print("1. Login")
    print("2. Registration")
    print("3. Exit")
    inp = int(input())
    if inp == 3:
        exit()
    thud = threading.Thread(target=updMessage, args=(Conn,))
    
    menu = [login, registration]
    menu[inp - 1](Conn)


