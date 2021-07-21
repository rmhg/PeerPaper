from core.server.server import Server
from core.Handler.Client import Client
from core.Lists.ServiceList import ServiceList

'''
Lock If Service List Is Empty
Until Fill(Producer And Consumer)
'''


def onConnection(conn):
    cli = Client(conn)
    ServiceListInstance.addnewClient(cli)

def MainServerThread():
    global ServiceListInstance

    ServiceListInstance = ServiceList()
    
    srv = Server(("localhost", 5000))
    srv.BindonConnectionMethod(onConnection)
    while True:
        srv.recvConnection()


MainServerThread()