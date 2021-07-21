class CError(Exception):
    def __init__(self):
        self.code = 400
        self.msg = "Failed"

class TypeError(CError):
    def __init__(self):
        super().__init__()
        self.code = 403
        self.msg = "Unknown Type!"

class MalformedReqError(CError):
    def __init__(self):
        super().__init__()
        self.code = 405
        self.msg = "Malformed Req Error!!"

class UnknownError(CError):
    def __init__(self):
        super().__init__()
        self.code = 400
        self.msg = "Some Error Occured"

class LoginError(CError):
    def __init__(self):
        super().__init__()
        self.code = 407
        self.msg = "Login Failed!! "

class LoginCredError(LoginError):
    def __init__(self):
        super().__init__()
        self.msg += "Login Credential Are Incorrect"


class LoginTimeOut(LoginError):
    def __init__(self):
        super().__init__()
        self.msg += "Login Timeout Retry Later"

class LoginErrorBlocked(LoginError):
    def __ini__(self):
        super().__init__()
        self.msg = ""

class InvalidToken(CError):
    def __init__(self):
        super().__init__()
        self.code = 409
        self.msg = "Invalid Tokens !! "


class TokenExpired(InvalidToken):
    def __init__(self):
        super().__init__()
        self.msg += "Token Expired"

class PeerNotExist(CError):
    def __init__(self):
        super().__init__()
        self.code = 404
        self.msg = "Peer Not Exist"

class RegistrationErrorUIDExist(CError):
    def __init__(self):
        super().__init__()
        self.code = 411
        self.msg = "Registration Error!!"

