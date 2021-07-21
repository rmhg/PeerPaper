from threading import Thread
from core.Handler import Handler

class ServiceWorkers:
    def __init__(self, count):
        self.Workers = []
        self.srvList = None
        self.idleList = None
        self.Working = False
        for i in range(0, count):
            self.Workers.append(Thread(target=self.__Work))

    
    def BindLists(self, srvList, idleList):
        self.srvList = srvList
        self.idleList = idleList
    
    def Start(self):
        self.Working = True
        for worker in self.Workers:
            worker.start()
    
    def Stop(self):
        self.Working = False
        for workers in self.Workers:
            workers.join()
        
    def __Work(self):
        while self.Working:
            print("In Server Thread")
            cli = self.srvList.getClient()
            print("In Thread", cli)
            hnd = Handler.Handler(cli)
            status = hnd.serviceClient()
            print("Status " , "Closed" if status == Handler.CLOSE_CLIENT else "IDLE")
            if status == Handler.CLOSE_CLIENT:
                cli.close()
            elif status == Handler.IDLE_CLIENT:
                self.idleList.addClient(cli)


    