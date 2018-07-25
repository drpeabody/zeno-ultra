package src;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.mindrot.jbcrypt.BCrypt;
import src.util.Result;

/**
 * @since Jul 12, 2018
 * @author Abhishek
 */
public class UserCollection {
    
    private final ConcurrentHashMap<String, User> data;
    
    public UserCollection(){
        data = new ConcurrentHashMap<>();
    }
    
    public String print(){
        return Arrays.asList(data).toString();
    }
    
    public int count(){
        return data.size();
    }
    public Collection getAllUsernames(){
        return data.keySet();
    }
    public boolean contains(String username){
        if(username == null || username.isEmpty()) return false;
        return data.containsKey(username);
    }
    public User get(String username){
        return data.get(username);
    }
    
    public Result insert(String username, String plainTextPwd){
        User user;
        try{
            user = new User(username, BCrypt.hashpw(plainTextPwd, BCrypt.gensalt()));
        } catch(Exception e){
            System.out.println(e);
            return Result.failedResponse("Error Creating User");
        }
        
        if(!contains(user.username)){
            data.put(username, user);
            return Result.successResponse("User registration is success.");
        }
        else{
            return Result.failedResponse("User already taken.");
        }
    }
    public Result remove(String username){
        if(!contains(username)){
            return Result.failedResponse("User not logegd in.");
        }
        
        if(data.remove(username) != null){
            return Result.successResponse("User Logged out.");
        }
        else{
            return Result.failedResponse("Error Removing User");
        }
    }
    
    public Result authAndDo(String username, String plainTextPwd, Consumer<User> cons){
        if(username == null || username.isEmpty()){
            return Result.failedResponse("Empty Username.");
        }
        
        User u = data.get(username);
        if(u == null) {
            return Result.failedResponse("Username does not exist");
        }
        if(BCrypt.checkpw(plainTextPwd, u.passHash)){
            cons.accept(u);
            return Result.successResponse("Access Granted");
        }
        return Result.failedResponse("Access Denied");
    }
    
    public Result logIn(String username, String plainTextPwd){
        return authAndDo(username, plainTextPwd, (u) -> u.logIn());
    }
    
    
    public static class User{
        final private String username;
        final private String passHash;
        private boolean isLoggedIn;

        protected User(String name, String hash){
            if(name == null || name.isEmpty()){
                throw new IllegalArgumentException("Can't create User with Null Properties.");
            }
            if(hash == null || hash.isEmpty()){
                throw new IllegalArgumentException("Can't create User with Null Properties.");
            }
            username = name;
            passHash = hash;
            isLoggedIn = false;
        }
        
        public boolean isLoggedIn() {
            return isLoggedIn;
        }
        public void logIn(){
            isLoggedIn = true;
        }
        public void logOut(){
            isLoggedIn = false;
        }

        @Override
        public String toString() {
            return "{" + username + ", " + passHash + ", " + isLoggedIn + " }";
        }
        
    }
}
