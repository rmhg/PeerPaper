class DatabaseInterface:
    def __init__(self, sock, cred):
        self.ip = sock[0]
        self.port = sock[1]
        self.duser = cred[0]
        self.dpass = cred[1]

    def getARowAtrrisValue(attr, value):
        row = []
        return row
