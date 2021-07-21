


def __createObj(type, obj):
    return {
        "type" : type,
        "status" : 0,
        "obj" : obj
    }

def registrationObj(regObj):
    return __createObj("reg", {
        "objname" : "regobj",
        "regobj" : regObj
    })

def loginObj(loginObj):
    return __createObj("auth", {
        "objname" : "authobj",
        "authobj" : {
            "type" : "new",
            "uid" : loginObj["uid"],
            "password" : loginObj["password"]
        }
    })

def renewObj(tokenObj):
    return __createObj("auth", {
        "objname" : "authobj",
        "authobj" : {
            "type" : "renew",
            "tokenobj" : tokenObj
        }
    })


def updateObj(updObj):
    return __createObj("upd",{
        "objname" : "updobj",
        "updobj" : updObj
    })

def configObj(confObj):
    return __createObj("config",{
        "objname" : "configobj",
        "configobj" : confObj
     })

def findpeerObj(findpeerObj):
    return __createObj("findpeer",{
        "objname" : "findpeerobj",
        "findpeerobj" : findpeerObj
    }) 

def connectObj(connectObj):
    return __createObj("connect", {
        "objname" : "connectobj",
        "connectobj" : connectObj
    })
