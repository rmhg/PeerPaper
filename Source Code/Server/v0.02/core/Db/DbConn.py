import psycopg2 as ps

def connect(ip, port,user, password, dbname):
    try:
        conn = ps.connect(database=dbname, user=user, password=password ,host=ip, port=port)
    except Exception as e:
        print("Cant Connect To Database " + e)
    return conn