import ui
import objConn
def main():
    Conn = objConn.ConnObj()
    Conn.setIP("localhost", 5000)
    Conn.connect()
    ui.mainUI(Conn)
    
main()