import java.io.*;
import java.net.*;
import java.util.*;

/**
   Author: Bharat Verma
   One object of this class handles one Client Socket connection
*/
class ClientHandler implements Runnable{
   Socket clientSocket;
   HashMap<String , Object>   applicationContext;
   
   ClientHandler(Socket clientSocket, HashMap<String , Object> applicationContext){
      System.out.println("New Client handler thread made");
      this.applicationContext = applicationContext;
      this.clientSocket = clientSocket;
   }

   public void run(){
      System.out.println("running new Client handler thread");
      try{
	     //getting inputstream from client's socket
         InputStream in = clientSocket.getInputStream();
		 
		 //getting outputstream from client's socket
		 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
		 
		 //reading from the input stream
	     byte[] buffer = new byte[2048];
         in.read(buffer);
		 String requestString  =  new String(buffer);
		 System.out.println("parsing request string");
		 
		 Parser requestParser = new RequestParser();
         HashMap<String, Object> parsedRequestObj = requestParser.parse(requestString.trim());
		 
		 //TODO : need to handle the case when http request is of invalid format
		 if(parsedRequestObj==null){
		    System.out.println("ERROR: Failed to parse the request");
	     }
		 //Since the request is successfully parsed , so ,Route the request 
		 else{
		     System.out.println("Request string parsed successfully");
			 Router router = new RequestRouter();
		     router.route(parsedRequestObj, out, applicationContext);
		 }
		 
		 //close the client soket connection
		 clientSocket.close();
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
}