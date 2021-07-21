from . import Exceptions

types = ["reg" , "upd" ,"config" ,"findpeer" ,"connect","auth"]
regexs = {
    "status" : "[\d]+"
} 
def validate(obj):
    type = obj["type"]
    if type not in types:
        raise Exceptions.TypeError()
        
    return type

