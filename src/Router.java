import java.util.*;
import java.io.*;

/**
   Author: Bharat Verma
*/

public interface Router {
   
   void route (HashMap<String, Object>  requestMap,  PrintWriter out, HashMap<String,Object> applicationContext);

   //handle GET request   
   void doGet(HashMap<String, Object>  requestMap,  PrintWriter out, HashMap<String,Object> applicationContext) throws IOException;
   
   //handle PUT request
   void doPut(HashMap<String, Object>  requestMap,  PrintWriter out, HashMap<String,Object> applicationContext) throws IOException;
}