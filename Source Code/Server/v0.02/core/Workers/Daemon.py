from threading import Thread
from core.Handler import Handler
import socket

class Daemon :
    def __init__(self):
        self.Working = False
        self.IdleList = None
        self.srvList = None
        self.thread = Thread(target=self.run)

    def Start(self, srvList,  idleList):
        self.Working = True
        self.IdleList = idleList
        self.srvList = srvList
        self.thread.start()

    def Stop(self):
        self.Working = False
        self.thread.join()

    def run(self):
        print("Daemon Started")
        while self.Working:
            print("In Daemon", len(self.IdleList.list))
            cli = self.IdleList.getClient()
            try:
                hnd = Handler.Handler(cli)
                status = hnd.serviceIdleClient()
                if status == Handler.SERVICE_CLIENT:
                    print("In Daemon Put To Service")
                    self.srvList.addClient(cli)
                elif status == Handler.CLOSE_CLIENT:
                    print("In Daemon Close Connection")
                    cli.close()
            except socket.timeout as e:
                print("In Daemon", e)
                self.IdleList.addClient(cli)
            except Exception as e:
                print("In Daemon CLosing", e)
                cli.close()



    