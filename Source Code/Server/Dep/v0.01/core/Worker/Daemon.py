'''

Daemon Will Scan ActiveList Clients
For A Message Other Than Update And
Check There Last_Active_Time, 
If The Last_Active_Time Was Long Enough
Daemon Closes The Client And Remove It From Active List,
And If A Client Send An Object Other Than Update,
Then Will Be Move To Service List. And May Be Send A 
OnHold Message.

And Daemon Will Be Go To Sleep If ActiveList Is Empty,
And Wakeup When List Will Be Populated

'''

class Daemon:
    def __init__():
        pass