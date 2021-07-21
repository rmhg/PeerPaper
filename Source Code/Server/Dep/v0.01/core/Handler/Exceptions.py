class AuthFailed (Exception):
    def __init__(self):
        self.message = "Wrong Authentication Details"
    
class InvalidObj (Exception) :
    def __init__(self, objname):
        self.message = "Invalid " + objname + " Object"


class PeerNotFound (Exception) :
    def __init__(self):
        self.message = "Peer Not Found"