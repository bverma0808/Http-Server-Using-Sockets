# Http-Server-Using-Sockets
# author: Bharat Verma

***************************
How to compile :=>
***************************
Go into the /src/ directory , and run command :=>
   javac *.java   
   
   
*******************
How to run:=>
*******************
The class containing main method is named as "HKServer"
   So you can run the following command to start a server :
          java HKServer
    with the above command the server will start at port number 5050 we can say it the default port of our server.
	
   But if you want to run it on some other port then you can use the following command:
         java HKServer <port number in numeric between 1024 to 65535>
		 
		 e.g. java HKServer 7575
		 And the server will be started at port 7575

		 
***********************************************************
Testing Commands to test the running server:=>
***********************************************************
open 3 or 4 terminals, and then run the following commands , please make sure to use same port number on which server is running:=>
 api/request
 ===========
  i.   curl localhost:5050/api/request?connId=1\&timeout=90
  ii.  curl localhost:5050/api/request?connId=2\&timeout=150    
  iii.  curl localhost:5050/api/request?connId=3\&timeout=180    
  
  and so on...
  In all of the above terminals you should wait for the time specified by you in timeout
  
 api/serverStatus 
 ============
Then in some other terminal, run command:=>
    curl localhost:5050/api/serverStatus
  you will get the status of all the running processes
  
  api/kill
  =====
  curl --request PUT localhost:5050/api/kill -d {"connId":1}
  
  it will kill the desired process , if exists otherwise send invalid connId message

  
  
****************************************
Server functioning unwrapped :=> 
****************************************

1. When the server is started then it will go into an infinite loop waiting for the client sockets to connect. 
2. Whenever a new client socket connection arrives at the server , then it makes a new Object of  "ClientHandler" class to handle that client socket and process it in a separate thread. So that it can accepts further new client connections.
3. Inside ClientHandler thread, the input stream of the client's socket is read and then the request string is read.
4. The request is then parsed by RequestParser class's method parse() which returns a HashMap of request contents (e.g. method name GET, PUT etc, controller names: api etc,  action name and request parameters etc )
5. If there occurs some problem in parsing the request then the parse() method will return null, and we just close the client socket connection
6. Otherwise we pass the requestMap to the route() method of RequestRouter class, which routes the request to the particular action.
7. If the api is of "request" type , then a new Connection class's object is created and is held in a pool (ConnectionPool class) , if the pool already contains a Connection object with the same id, then in that case instead of storing the Connection in the pool, appropriate message is sent to the client and his connection is closed.
8. If the request is of "serverStatus" type then , the status of the currently active connections is sent to the client, by reading it from connectionPool
9. If the request is of "kill" type then the corresponding connection (if found) is deleted, otherwise appropriate message is sent to the client


*******************************************
Server's Replaceable Modules :=> 
*******************************************
Some modules in the server can be easily replaceable, such as RequestParser , RequestRouter, ConnectionPool, means their is no such dependency of the code over these modules. The code is not tightly coupled.
> To replace the RequestParser we just need to make a new class that implements the Parser interface
> To replace the RequestRouter we just need to make a new class that implements the Router interface
> Right now ConnectionPool uses a HashMap for storing the Connections, later on we can even use some different data-structure depending on our need.
