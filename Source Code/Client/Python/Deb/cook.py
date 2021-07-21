import json

def calcNounce():
    return 0

def Obj(type, status, obj):
    cookobj = {
        "reg" : regObj,
        "auth" : authObj,
        "upd" : upd_statusObj,
        "findpeer" : findPeerObj,
        "connectpeer" : connectPeerObj
    }
    cobj = cookobj.get(type, None)(obj)
    return {
        "type" : type,
        "status" : status,
        "obj" : cobj
    }

def authObj(obj):
    return {
        "objname" : "authobj",
        "authobj" : {
            "uid" : obj["uid"],
            "pass" : obj["pass"],
            "nounce" : calcNounce()
        }
    }

def regObj(obj):
    return {
        "objname" : "regobj",
        "regobj" : {
            "uid" : obj["uid"],
            "visible" : obj["visible"],
            "pass" : obj["pass"],
            "pbinfo" : obj["pbinfo"]
        }
    }


def upd_statusObj(obj):
    return {
        "objname" : "updobj",
        "updobj" : {
            "type" : obj["type"],
            "uid" : obj["uid"],
            "tid" : obj["tid"],
            "tstr" : obj["tstr"]
        }
    }

def findPeerObj(obj):
    return {
        "objname" : "findpeerobj",
        "findpeerobj" : {
            "puid" : obj["puid"],
            "uid" : obj["uid"],
            "tid" : obj["tid"],
            "tstr" : obj["tstr"]
        }
    }

def connectPeerObj(obj):
    return {
        "objname" : "connectpeerobj",
        "connectpeerobj" : {
            "puid" : obj["puid"],
            "uid" : obj["uid"],
            "tid" : obj["tid"],
            "tstr" :  obj["tstr"]
        }
    }

def getBody(type, status, obj):
    bjson = json.dumps(Obj(type, status, obj))
    return str(len(bjson)) + "\n" + bjson