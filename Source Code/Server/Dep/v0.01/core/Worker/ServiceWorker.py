from ..Handler.handler import CliHandler
from ..Handler import handler

class ServiceWorker:
    def __init__(self):
        self.ServiceListInstance = None
        self.ActiveListInstance = None
        self.IdleListInstance = None
        
    def BindServiceList(self, ServiceList):
        self.ServiceListInstance = ServiceList
    
    def BindActiveList(self, ActiveList):
        self.ActiveListInstance = ActiveList
    
    def start(self):
        pass
    
    def putToSleep(self):
        pass

    def WakeUp(self):
        pass

    def Work(self):
        '''
        Fetch A Client From The Service List
        Service Client And Either Put That Client To
        IdleList Or Close Client

        '''
        try:
            cli = self.ServiceListInstance.getNewClient()
            if not cli :
                self.putToSleep()
            clihandler = CliHandler(cli)
            clistatus = clihandler.Handle()
        
            ''' 
            status = Closed : The Client Is Serviced And Closed,
            status = Active : Move To IdleList
            '''

            if clistatus == handler.CLOSE_CLIENT: 
                cli.close()
            elif clistatus == handler.ACTIVE_CLIENT :
                self.IdleListInstance.addClient(cli)
        except :
            cli.close()