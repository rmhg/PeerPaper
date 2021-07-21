
MALFORMED_OBJ = 407
UNKNOWN_TYPE = 408
NOT_FOUND = 404
NOT_AUTHORISE = 403
FORBIDDEN = 401
SUCESS = 200
SOME_ERROR_OCCURED = 410
AUTH_FAILED = 402


def codeToString(code):
    codes = {
        200 : "Operation Was Sucessfull",
        402 : "Authentication Failed",
        401 : "Request FORBIDDEN",
        403 : "NOT_AUTHORISED",
        404 : "NOT_FOUND",
        407 : "MALFORMED Message",
        408 : "UNKNOWN TYPE",
        410 : "SOME ERROR OCCURED"
    }
    return codes.get(code, "SOME ERROR OCCURED")

def tokenObj(obj):
    return {
        "objname" : "tokenobj",
        "tokenobj" : {
            "tid" : obj["tid"],
            "tstr" : obj["tstr"]
        } 
    }

def connectReqObj(obj):
    return {
        "objname" : "connectreqobj",
        "connectreqobj" : {
            "ruid" : obj["ruid"],
            "msg" : obj["msg"]
        }
    }

def connectionObj(obj):
    return {
        "objname" : "connectionobj",
        "connectionobj" : {
            "shared" : obj["shared"],
            "ipobj" : {
                "uid" : obj["uid"],
                "ip" : obj["ip"],
                "port" : obj["port"]
            }
        }
    }


def statusObj(obj):
    return {
        "objname" : "statusobj",
        "statusobj" : {
            "errorcode" : obj["code"],
            "errormsg" : obj["msg"]
        }
    }
def cstatusObj(code):
    return statusObj({
        "code" : code,
        "msg" : codeToString(code)
    })
def peerinfoObj(obj):
    return {
        "objname" : "peerinfo",
        "peerinfo" : {
            "uid" : obj["uid"],
            "pbinfo" : obj["pbinfo"]
        }
    }

def Obj(type, status, obj):
    objs = {
        "token" : tokenObj,
        "connectreq" : connectReqObj,
        "connection" : connectionObj,
        "status" : statusObj,
        "peerinfo" : peerinfoObj 
    }
    if status == 200:
        func = objs.get(type, None)
    else:
        func = statusObj
    return {
        "type" : type,
        "status" : status,
        "obj" : func(obj)
    }

