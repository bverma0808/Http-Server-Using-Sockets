import java.io.*;
import java.net.*;
import java.util.*;

/**
   Author: Bharat Verma
*/

public class HKServer{
   //Socket object for our HKServer
   ServerSocket serverSocket;        
   
   //we can store all the objects whose scope is application wide, in applicationContext
   HashMap<String,Object> applicationContext;
   
   /**
      Initialise server instance 
	  Initialise applicationContext
	  @param port  => an integer value specifying the port number on which the server will start
   */
   public HKServer(int port){
      System.out.println("Server initiated at port number :=> " + port);
      try{
	     serverSocket = new ServerSocket(port);
	     applicationContext = new HashMap<String,Object>();
	  }
	  catch(Exception e){
	     e.printStackTrace();
	  }
   }
   
   /**
        This method will start our tcp server which accepts connections from client sockets
		and hand over each client socket to a separate clientHandler thread
   */
   void startServer(){
      try{
         while(true){ 
		    //new client arrives
            Socket clientSocket = serverSocket.accept(); 
            System.out.println("New connection established");
			
			//make a brand new clientThread for new client and hand over client to that thread
            Runnable handle = new ClientHandler(clientSocket, applicationContext);
            Thread t = new Thread(handle); 
             t.start();
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
   
   
   //********************************************************************//
   //********************** MAIN METHOD *************************//
   //********************************************************************//
   public static void main(String []arg){ 
        try{
		   //setting default port to 5050
           int port = 5050;
		   
		   //check if user entered port number via command line arguments
           if(arg.length>0){
              port = Integer.parseInt(arg[0]);
           }
           
		   //checking if the port is within valid range
           if(port>=1024 && port<=65535){
		       //instantiate and start a new server
               HKServer server = new HKServer(port);
               server.startServer();
           } 
		   //otherwise print error message
           else{
               System.out.println("ERROR: PORT number should lie in range [1024,65535]");
           }
        }
        catch(Exception ex){
           System.out.println("ERROR: PORT number should be numeric");
        }
   }
}
