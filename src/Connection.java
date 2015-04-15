/**
   Author : Bharat Verma
   one object of this class represents one process in memory
*/

class Connection {
   long id;                //connection ID
   long time;            //time available for process; this will keep on decreasing
   boolean killed;    //set to true if killed forcefully
   
   Connection(long id, long time){
      System.out.println("New connection process is born; id=> "+ id+" ; time:=> "+ time);
      this.id = id;
	  this.time = time;
	  killed = false;
   }
   
   /**
      This method will run a timer for the specified time
	  @return true if the timer expired normally
	                  false if process is killed forcefully or some exception occurred
   */
   boolean timeout(){
       System.out.println("Running timeout function");
       try{
          while(time>0){
		     if(killed){
			    System.out.println("Process killed , ID:=> "+ id);
			    return false;
		     }
	         Thread.sleep(1000);
			 time--;
			 System.out.println("Process Id:=> "+ id +"   ;   Remaing time=> " + time);
	      }
		  return true;
	   }
	   catch(Exception e){
	      e.printStackTrace();
	   }
	   return false;
   }
   
   /**
     It will return the remaining time the process has 
   */
   long getTime(){
      return time;
   }
   
   /**
     It will return the id of connection
   */
   long getId(){
      return id;
   }
   
   
   /**
      destroy the current connection
	*/
   boolean destroy(){
      killed = true;
	  return killed;
   }

}