def __createObj(type, status, obj):
    return {
        "type" : type,
        "status" : status,
        "obj" : obj
    }


def statusObj(code, msg):
    return __createObj("status", code, {
        "objname" : "statusobj",
        "statusobj" :  {
            "code" : code,
            "msg" : msg
        }
    })

def peerinfoObj(pobj):
    return __createObj("peerinfo", 200, {
        "objname" : "peerinfoobj",
        "peerinfoobj" : pobj
    })

def tokenObj(tobj):
    return __createObj("token", 200, {
        "objname" : "tokenobj",
        "tokenobj" : tobj
    })

def reqpeerObj(puid, msg):
    return __createObj("reqpeer", 200, {
        "objname" : "reqpeerobj",
        "reqpeerobj" : {
            "puid" : puid,
            "pmsg" : msg
        }
    })

def connectionObj(puid, usecret, psecret, sock):
    return __createObj("connection", 200,{
        "objname" : "connectionpeerobj",
        "connectionpeerobj": {
            "puid" : puid,
            "usecret" : usecret,
            "psecret" : psecret,
            "psockaddr" : sock
        }

    })


def sRegistrationSucess():
    return statusObj(201, "Sucess!! Registration Sucessful")

def sLogoutSucess():
    return statusObj(202, "Sucess!! Logout Sucessful")

def sConfigSucess():
    return statusObj(203, "Sucess!! Config Sucessful")