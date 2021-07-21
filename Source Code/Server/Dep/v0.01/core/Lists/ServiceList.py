
from threading import Lock

class ServiceList:
    def __init__(self):
        self.lock = Lock()
        self.list= []

    def addNewClient(self, cli):
        self.lock.acquire(blocking=True)
        self.list.append(cli)
        self.lock.release()

    def getCli(self):
        self.lock.acquire(blocking=True)
        cli = None
        try:
            cli = self.list[0]
        except:
            cli = None
        self.lock.release()
        return cli