'''
Delete A Row Using A Key=Value
Find A Row Using A Key=Value
Add A Row Using A Key=Value pairs




'''


class Table:
    def __init__(self, name):
        self.name = name
        self.cursor = None
    def removeRow(self, **keyval):
        schema = ""
        for key,value in keyval.items():
            if len(schema) > 0:
                schema += " AND "
            schema += key + "=" + value
        deletequery="DELETE FROM {tablename} WHERE {schema}".format(tablename=self.name, schema=schema)
        self.cursor.execute(deletequery)
        self.cursor.commit()

    def findARow(self, **keyvalue):
        schema = ""
        findquery = "SELECT FROM {tablename} WHERE {schema}".format(tablename=self.name, schema=schema)
        self.cursor.execute(findquery)
        
