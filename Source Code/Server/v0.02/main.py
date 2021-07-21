from core.Server.Server import Server
from core.cStructure.Client import Client
from core.cStructure.List import List
from core.Workers.ServiceWorkers import ServiceWorkers
from core.Workers.Daemon import Daemon


ServiceListInstance = List()
IdleListInstance = List()


def onConn(conn):
    cli = Client(conn)
    print("A Connection Recvied ")
    ServiceListInstance.addClient(cli)


def MainServerThread():
    ServiceWorkersInstance = ServiceWorkers(3)
    DaemonInstance = Daemon()
    ServiceWorkersInstance.BindLists(ServiceListInstance, IdleListInstance)
    ServiceWorkersInstance.Start()
    DaemonInstance.Start(ServiceListInstance, IdleListInstance)
    srv = Server("", 5000)
    srv.BindOnConnection(onConn)
    srv.start()

MainServerThread()
