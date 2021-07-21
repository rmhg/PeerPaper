from . import DbConn

def keyval(sup, where):
    swhere = ""
    for key, vals in where.items():
        swhere += "{0}={1}{sup}".format(key, ("\'" + vals + "\'") if  type(vals) is str else vals, sup="{sup}")
    swhere = swhere[:len(swhere) - 5]
    return swhere.format(sup=sup)

def more(**kwargs):
    return kwargs



class Table:
    dbconn = DbConn.connect("localhost", 5432, "postgres", "root","peerpaper")
    cursor = dbconn.cursor()
    def __init__(self, name, schema):
        self.name = name
        self.schema = schema

    def addRow(self, **regobj):
        cols = ""
        vals = ""
        for key, values in regobj.items():
            cols += key + ","
            vals += ("\'" + str(values) + "\'" if type(values) is str else str(values)) + ","
        cols = cols[:len(cols) - 1]
        vals = vals[:len(vals) - 1]
        query = "INSERT INTO {tablename} ({cols}) VALUES ({vals})".format(tablename=self.name, cols=cols, vals=vals)
        try:
            Table.cursor.execute(query)
            Table.dbconn.commit()
        except Exception as e:
            Table.dbconn.rollback()
            print(e)
            return False
        return True

    def fetchRow(self, **where):
        query = "SELECT * FROM {tablename} WHERE {where}".format(tablename=self.name, where=keyval(" AND ", where))
        Table.cursor.execute(query)
        rows = Table.cursor.fetchone()
        if rows :
            cobj = {}
            for i,values in enumerate(rows):
                cobj[self.schema[i]] = values
            if len(cobj) :
                return cobj
        return None

    def updateRow(self, updval,  swhere):
        query = "UPDATE {tablename} SET {updValues} WHERE {where}".format(tablename=self.name, updValues=keyval(",", updval), where=keyval(" AND ", swhere))
        try:
            Table.cursor.execute(query)
            Table.dbconn.commit()
            return True
        except Exception as e:
            Table.dbconn.rollback()
            print(e)
        return False

    def deleteRow(self, **swhere):
        query = "DELETE FROM {tablename} WHERE {where}".format(tablename=self.name, where=keyval(" AND ", swhere))
        try:
            Table.cursor.execute(query)
            Table.dbconn.commit()
        except Exception as e:
            Table.dbconn.rollback()
            return False
        return True


