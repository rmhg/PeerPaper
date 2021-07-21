from threading import Lock, Condition

class List:
    def __init__(self):
        self.lock = Lock()
        self.cv = Condition(self.lock)
        self.list = []
    

    def addClient(self, cli):
        with self.cv:
            self.list.append(cli)
            self.cv.notify_all()

    def getClient(self):
        cli = None
        with self.cv:
            while len(self.list) == 0:
                self.cv.wait()
            cli = self.list[0]
            self.list.remove(cli)
        return cli

