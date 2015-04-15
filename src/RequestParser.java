import java.util.*;

/**
    Author: Bharat Verma
*/

public class RequestParser implements Parser{

    /**
	    This method parses the string of http request and sets the values in a hashmap for their easy retrieval
	   @param requestString  : String sent by client , it contains HTTP request  + Body
	   @return a hashMap of contents such as Method name , controller name , action name and request parameters etc
    */	
   public HashMap<String,Object> parse(String requestString){
         System.out.println("Request String ====> \n"+ requestString+ "\n\n");
   
         //check for empty request String
		 if(requestString==null || requestString.equals("")){
		    return null;
		 }
		 
		 //otherwise make a hashmap which will contain request contents
         HashMap<String, Object> requestMap = new HashMap<String, Object>();
		 
         try{
		    StringTokenizer lineTokenizer = new StringTokenizer(requestString, "\n");
			String firstLineOfRequest = lineTokenizer.nextToken();
			
			//getting the name of http method, controller and action which will be space separated
			//and putting them in a requestMap
			StringTokenizer stk2 = new StringTokenizer(firstLineOfRequest," /");
			
			String method = stk2.nextToken();    //GET , PUT etc
			requestMap.put("method", method);
			
			String controller = stk2.nextToken();     // api (in our case)
			requestMap.put("controller", controller);
			
			String actionPlusParams = stk2.nextToken();
			
			
			//actionPlusParams can have request parameters associated with it self 
			//separated by a '?' mark , And multiple parameters will be concatenated
			//by '&' character, we will store the request parameters in a hashMap
			HashMap<String, String> params = new HashMap<String, String>();
			StringTokenizer stk3 = new StringTokenizer(actionPlusParams,"?&");
			
			//action name
			String action = stk3.nextToken();     //request, serverStatus or kill
			requestMap.put("action", action);
			
			//request parameters,  connId, timeout etc
			StringTokenizer stk4;                       
			while(stk3.hasMoreTokens()){
			   String parameter = stk3.nextToken();
			   System.out.println("parameter==> " + parameter);
			   
			   //each parameter will be associated with its corresponding value by '=' character
			   stk4 = new StringTokenizer(parameter, "=");
			   String key = stk4.nextToken();
			   String value = stk4.nextToken();
			   params.put(key,value);
			}
			//check if params contains anything or not
			if(params.size()>0){
			   requestMap.put("params", params);   //put them in requestMap
			}
			
			
			//In case of PUT method of http , we should expect some payload with the request
			if(method.equalsIgnoreCase("put")){
			    String payLoad = "";
				
				//since we are expecting only one line here, therefore only last line of the request String will be the payLoad
			    while(lineTokenizer.hasMoreTokens()){
				   payLoad = lineTokenizer.nextToken().trim();
				}
				
				//check if payLoad exists
				if(!payLoad.equals("")){
				   System.out.println("payload=====> "+ payLoad);
				   StringTokenizer payLoadTokens = new StringTokenizer(payLoad, "{,}");
				   ArrayList<String> killList = new ArrayList<String>();
				   
				   while(payLoadTokens.hasMoreTokens()){
				      String connectionToBeKilled = payLoadTokens.nextToken();
					  System.out.println("connectionToBeKilled=====> "+ connectionToBeKilled);
					  StringTokenizer stk5 = new StringTokenizer(connectionToBeKilled, ": ");
					  
					  if(stk5.hasMoreTokens()){
					     //we don't need to store the first string, because it is just a text => "connId"
						 stk5.nextToken();
					  
						 //we are interested in only second value which is a Id for process to be killed
						 String conId = stk5.nextToken();
						 System.out.println("conId=====> "+ conId);
						 killList.add(conId);
					  }
				   }
				   
				   requestMap.put("killList", killList);
				}
				else{
				   System.out.println("no payload found");
				}
			}
		 }
		 
		 //most possible chances are for NoSuchElementException , which comes
		 //when we are running nextToken() method of StringTokenizer without even checking
		 //the return value of hasMoreTokens() first. In this way we will come to know whether the http request 
		 //is valid or not. Because if we don't get what we expect than that means the request is invalid
		 catch(Exception e){
		     e.printStackTrace();
			 return null;
		 }
		 
		 //if everything is fine , then return the requestMap
		 return requestMap;
   }

}