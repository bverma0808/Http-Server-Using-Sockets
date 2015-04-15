 import java.io.*;
 import java.net.*;
 import java.util.*;

 /**
   Author: Bharat Verma
*/
 
 public class RequestRouter  implements Router {
    /**
	   Route the request according to the type of HTTP method used (GET , PUT etc)
	   @param requestMap       ;  HashMap containing request content
	   @param out                      ;  Output Stream of client
	   @param applicationContext ; Application wide HashMap to store things which will remain common throughout the application
	*/
    public void route (HashMap<String, Object>  requestMap,  PrintWriter out, HashMap<String,Object> applicationContext){
	    System.out.println("Routing the request");
	    String method = (String)requestMap.get("method");
		System.out.println("method name:=> "+method);
		
		try{
		    if(method.equalsIgnoreCase("get")){
			    doGet(requestMap, out, applicationContext);
		    }
		
		    else if(method.equalsIgnoreCase("put")){
		        doPut(requestMap, out, applicationContext);
		    }
		
		    else{
			     System.out.println("Method "+ method +" not supported");
		         out.println("Method "+ method +" not supported");
		    }
	    }
		catch(Exception e){
		     e.printStackTrace();
		}
	}
	
	
	/**
	   If HTTP method is GET , then this method is invoked
	   @param requestMap       ;  HashMap containing request content
	   @param out                      ;  Output Stream of client
	   @param applicationContext ; Application wide HashMap to store things which will remain common throughout the application
	*/
	public void doGet (HashMap<String, Object>  requestMap,  PrintWriter out, HashMap<String,Object> applicationContext) throws IOException{
	     
		 String controllerName = (String)requestMap.get("controller");
		 String actionName = (String)requestMap.get("action");
		 System.out.println("controller name:=> "+controllerName);
		 System.out.println("action name:=> "+actionName);
		 
		 HashMap<String,String> params = (HashMap<String,String>)requestMap.get("params");
		 
	     if(controllerName.equalsIgnoreCase("api")){  
			    //accessing object of api controller
			    Api api = (Api)applicationContext.get("apiObject");
			   
			    //If not present , then make a new object
			    if(api==null){
			        api = new Api();
				    applicationContext.put("apiObject", api);
			    }
			   
			   
			    // Invoking actions of api controller
			   
			    // api/request 
			    if(actionName.equalsIgnoreCase("request")){
				     System.out.println("Invoking request action of api cntroler");
			         long connectionId = Long.parseLong((String)params.get("connId"));
			         long timeout = Long.parseLong((String)params.get("timeout"));
			     	 System.out.println("con id:=> " + connectionId);
					 System.out.println("timeout:=> " + timeout);
                	 api.request(connectionId, timeout, out, applicationContext);
			    }
			   
			    // api/serverStatus
			    else if(actionName.equalsIgnoreCase("serverStatus")){
				     System.out.println("Invoking serverStatus action of api controller");
                     api.serverStatus(out, applicationContext);
			    }
				   
				else{
			         System.out.println("404 Not Found");
		             out.println("404 Not Found");
		        }
		}
	}
	
	
	/**
	   If HTTP method is PUT , then this method is invoked
	   @param requestMap       ;  HashMap containing request content
	   @param out                      ;  Output Stream of client
	   @param applicationContext ; Application wide HashMap to store things which will remain common throughout the application
	*/
	public void doPut(HashMap<String, Object>  requestMap,  PrintWriter out, HashMap<String,Object> applicationContext) throws IOException{
	    String controllerName = (String)requestMap.get("controller");
		String actionName = (String)requestMap.get("action");
		System.out.println("controller name:=> "+controllerName);
		System.out.println("action name:=> "+actionName);
		
	    if(controllerName.equalsIgnoreCase("api")){
	        //accessing object of api controller
		   Api api = (Api)applicationContext.get("apiObject");
			   
            //If not present , then make a new object
			if(api==null){
			   api = new Api();
			   applicationContext.put("apiObject", api);
			}
			   
			// api/kill
			if(actionName.equalsIgnoreCase("kill")){
			    //long connectionId = Long.parseLong((String)params.get("connId"));
			    System.out.println("Running kill action");
			    ArrayList<String> killList = (ArrayList<String>)requestMap.get("killList");
				if(killList!=null){
				    System.out.println("killList found");
				    Iterator iterator = killList.iterator();
					while(iterator.hasNext()){
						String id = (String)iterator.next();
						System.out.println("killing process with connId => " + id);
						Long connectionId = Long.parseLong(id);
                        api.kill(connectionId, out, applicationContext);
					}
				}
				else{
					api.kill(new Long(-1), out,  applicationContext);
				}
			}
			else{
			    System.out.println("404 Not Found");
		        out.println("404 Not Found");
            }
	    }
	}
 }