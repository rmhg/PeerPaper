
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

