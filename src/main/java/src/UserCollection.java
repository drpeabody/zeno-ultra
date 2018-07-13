package src;

import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

/**
 * @since Jul 12, 2018
 * @author Abhishek
 */
public class UserCollection {
    
    private final CopyOnWriteArrayList<User> data;
    
    public UserCollection(){
        data = new CopyOnWriteArrayList<>();
    }
    
    public int count(){
        return data.size();
    }
    
    public boolean contains(String username){
        ListIterator itr = data.listIterator();
        while(itr.hasNext()){
            User u = (User) itr.next();
            if(u.username.equals(username)) return true;
        }
        return false;
    }
    
    public User getBySessionID(String sessionID){
        Stream t = data.stream().filter((b) -> b.sessionID.equals(sessionID));
        if(t.count() < 1) return null;
        return (User) t.findFirst().get();
    }
    
    public DataResponse insert(String sessionID, String username){
        User user;
        try{
            user = new User(sessionID, username);
        } catch(Exception e){
            System.out.println(e);
            return DataResponse.failedResponse("Error Creating User");
        }
        
        if(!contains(user.username)){
            data.add(user);
            return DataResponse.successResponse("User logged in success.");
        }
        else{
            return DataResponse.failedResponse("User already present in Collection.");
        }
    }
    public DataResponse removeByUsername(String username){
        if(!contains(username)){
            return DataResponse.failedResponse("User not logegd in.");
        }
        
        if(data.removeIf((b) -> b.username.equals(username))){
            return DataResponse.successResponse("User Logged out.");
        }
        else{
            return DataResponse.failedResponse("Error Removing User");
        }
    }
    
    public static class User{
        private String sessionID;
        private String username;

        protected User(String id, String name){
            if(id == null || name == null){
                throw new IllegalArgumentException("Can't create User with Null Properties.");
            }
            if(id.isEmpty() || name.isEmpty()){
                throw new IllegalArgumentException("Can't create User with Empty Properties.");
            }
            sessionID = id;
            username = name;
        }
        
        public String getSessionID() {
            return sessionID;
        }
        public String getUsername() {
            return username;
        }
        
    }
    
    public static class DataResponse{
        public final String msg;
        public final boolean success;
        
        public DataResponse(String msg, boolean success){
            this.msg = msg;
            this.success = success;
        }
        
        public static DataResponse successResponse(String msg){
            return new DataResponse(msg, true);
        }
        public static DataResponse failedResponse(String msg){
            return new DataResponse(msg, false);
        }
    }

}
