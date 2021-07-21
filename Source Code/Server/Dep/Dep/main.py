from core.server import Server
from core.obj.BodyHandler import BodyHandler
from core.DatabaseMan import ActiveList, AllList
import json
import os
import socket
import threading
import traceback
srv = Server()
allList = AllList.AllList()
activeList = ActiveList.ActiveList(allList)
ActiveConnection = []



def parseBody(connfd):
    
    size = 0
    no = ''
    ch = ''
    while ch != "\n":
        ch = connfd.recv(1).decode()
        no += ch
    size = int(no)
    body = connfd.recv(size).decode()
    return body



def sendObj(connfd, obj):
    js = json.dumps(obj)
    data = str(len(js)) + "\n" + js
    connfd.send(data.encode())



def onConnection(conn):
    global allList
    global activeList
    connfd = conn[0]
    connfd.settimeout(2)
    try:
        print("Recivied A Connection")
        body = ""
        body = json.loads(parseBody(connfd))
        
        bh = BodyHandler((conn[1], body), ActiveConnection)
        res = bh.openOperation((allList, activeList))
        print("Print Response", res)
        sendObj(connfd, res)
        uid = body["obj"][body["obj"]["objname"]].get("uid", None)
        if uid and body["type"] != "reg":
            ActiveConnection.append({
                "uid" : uid,
                "sock" : conn
            })
        else:
            connfd.close()
    except Exception as e:
        traceback.print_exc()
        print(e)
        connfd.close()






def ConnectionManager():
    while srv.isrun:
        for conn in ActiveConnection:
            try:
                body = ""
                addr = conn["sock"][1]
                connfd = conn["sock"][0]
                connfd.settimeout(5)
                req = parseBody(connfd)
                body = json.loads(req)
                bh = BodyHandler((addr , body), ActiveConnection)
                res = bh.openOperation((allList, activeList))
                sendObj(connfd, res)
            except socket.timeout:
                conn["sock"][0].close()
                print("Connection Closed")
                ActiveConnection.remove(conn)
            except Exception as e:
                print(e)
                print(req)
        




def main(): 
    os.system("cls")
    print("Server Started")
    
    srv.onConnection = onConnection
    srv.start() 
    cmth = threading.Thread(target=ConnectionManager)
    cmth.start()
    while input() != 'e':
        pass
    srv.stop()
    cmth.join()

main()