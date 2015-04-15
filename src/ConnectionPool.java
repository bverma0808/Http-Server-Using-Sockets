import java.util.*;

/**
   Author: Bharat Verma
   A single object of this class will contain the pool of active connections.
   Whenever a process completes ,then the corresponding connection object 
   will be removed from the pool
*/

public class ConnectionPool {
   HashMap<String, Connection>  pool  ;
   
   ConnectionPool(){
      pool = new HashMap<String, Connection>();
   }
   
   /**
      Adds a new Connection Object in the pool
   */
   void add(Connection con){
      String key =  String.valueOf(con.id);
      pool.put(key, con);
   }
   
   
   /**
      Returns true if the Connection Object already present in the pool
   */
   boolean contains(Connection con){
      return pool.containsKey(String.valueOf(con.id));
   }
   
   
   /**
      Returns the Connection Object with the requested id
   */
   Connection getConnection(long id){
      return pool.get(String.valueOf(id));
   }
   
   
   /**
      Removes the requested Connection object from the pool
   */
   void remove(Connection con){
      pool.remove(String.valueOf(con.id));
   }
   
   /**
      Returns the iterator to iterate over Connection Objects
   */
   Iterator iterator(){
      return pool.values().iterator();
   }
   
   /**
      returns the current pool size
   */
   int size(){
      return pool.size();
   }
}