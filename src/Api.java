import java.io.*;
import java.net.*;
import java.util.*;

/**
  Author: Bharat Verma
  This is our controller class, which has main methods which are 
   invoked by user . We will make only one object of this class throughout 
   the application, and will store that object in applicationContext
*/
class Api{

      Api(){
	     System.out.println("New object of Api class formed");
	  }

      /**
	     @param connectionId          => a long value
		 @param time                         =>  time for which the process should last
		 @param out                           =>  output stream of clientSocket
		 @param applicationContext  => to store the client process' statuses
		 @return status of the request
	  */
      void request(Long connectionId, Long time, PrintWriter out, HashMap<String,Object> applicationContext) throws IOException{
	         System.out.println("Inside request action of api controller");
	
         	 //make a new connection
          	 Connection con = new Connection(connectionId, time);
			 
			 ConnectionPool connectionPool = (ConnectionPool)applicationContext.get("connectionPool");
			 if(connectionPool==null){
			    connectionPool = new ConnectionPool();
				applicationContext.put("connectionPool", connectionPool);
			 }
			 
			 //prior to adding the new connection to the pool we should check its pre existence
			 if(connectionPool.contains(con)){
			    out.println("Process with id => " + connectionId + " already exists in the pool");
				return;
		     }
			
			 //otherwise add the new connection to the pool
             connectionPool.add(con);
			
			 //return ok status if timed out normally
			 if(con.timeout()){
			    out.println("{\"status\":\"ok\"}") ;
			 }
			 //otherwise killed status
			 else{
			    out.println("{\"status\":\"killed\"}") ;
			 }
			 
			 //remove it from pool
			 connectionPool.remove(con);
	  }
	  
	  
	  /**
		 @param out                            =>  output stream of clientSocket
		 @param applicationContext   =>  HashMap to store the client process' statuses
		 @return status of the request
	  */
	  void serverStatus(PrintWriter out, HashMap<String,Object> applicationContext) throws IOException{
	       ConnectionPool connectionPool = (ConnectionPool)applicationContext.get("connectionPool");
		   
		   //there is no active connection ,so returning empty response
		   if(connectionPool==null){
			    connectionPool = new ConnectionPool();
				applicationContext.put("connectionPool", connectionPool);
		   }	
		   
		   //otherwise 
		   else{
		       String status = "";
			   Iterator iterator  = connectionPool.iterator();
			   while(iterator.hasNext()){
			       Connection con = (Connection)iterator.next();
				   long id = con.getId();
				   long time = con.getTime();
				   status +=  "\""+id+"\":\""+time+"\",";
			   }
			   status = "{" + status.substring(0,status.length()-1) + "}";
			   out.println(status);
		   }
	  }
	  
	  
	  /**
	     @param connectionId   => a long value
		 @param out                   =>  output stream of clientSocket
		 @param applicationContext  => to store the client process' statuses
		 @return status of the request
	  */
	  void kill(Long connectionId, PrintWriter out, HashMap<String,Object> applicationContext) throws IOException{
           ConnectionPool connectionPool = (ConnectionPool)applicationContext.get("connectionPool");
		   
		   //there is no active connection ,so returning empty response
		   if(connectionPool==null){
			    System.out.println("connList not found , inside kill method");
			    out.println("{ \"status\": \"invalid connection Id : "+ connectionId +"\"}");
			    return;
		   }	
		   
		   //find the connection process having the same ID as connectionId
		   Connection connection = connectionPool.getConnection(connectionId);
		   
		   //if connection not found return status "INVALID"
		   if(connection==null){
		      out.println("{ \"status\": \"invalid connection Id : "+ connectionId +"\"}");
			  return;
		   }
		   
		   //otherwise kill the process and send the "OK" status
		   connection.killed=true;
		   out.println("{ \"status\": \"ok\"}");
		   System.out.println("Process with id:=> "+connectionId+" is killed successfully");
	  }
}