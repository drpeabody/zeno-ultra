package src.controller;

import spark.Request;
import spark.Response;
import spark.Session;
import src.Main;
import src.Routes;
import src.UserCollection;
import src.UserCollection.User;

/**
 * @since Jul 13, 2018
 * @author Abhishek
 */
public class LoginController {
    public static final String ATRIB_USERNAME = "currentUserName";
    
    //Add User Expiry for lost connections handling
    
    public static boolean ensureLoggedIn(Request req, Response res){
        //If the user is logged in, return a null String otherwise redirect to login path.
        System.out.println("LOGINCONTROLLER: Ensure Logged In Called.");
        Session session = req.session();
        String username = session.attribute(ATRIB_USERNAME);
        if(username == null || username.isEmpty()) return false;
        else{
            System.out.println("LOGINCONTROLLER: Ensured Username Exists in Session.");
            if(Main.users.contains(username)){
                System.out.println("LOGINCONTROLLER: Corresponding User found on server.");
                //Similar user found => User is logged in
                return true;
            }
        }
        return false;
    }
    
    public static String logIn(Request req, Response res){
        if(ensureLoggedIn(req, res)){
            //User is already logged in
            res.redirect(Routes.PATH_WELCOME);
            System.out.println("LOGINCONTROLLER: User is already logged in");
        }
        
        String usernameCandidate = req.queryParams("username");
        if(usernameCandidate == null || usernameCandidate.isEmpty()){
            res.redirect(Routes.PATH_HOME);
            System.out.println("LOGINCONTROLLER: Username is Empty");            
        }
        Session session = req.session();
        UserCollection.DataResponse result = Main.users.insert(session.id(), usernameCandidate);
        
        if(result.success){
            session.attribute(ATRIB_USERNAME, usernameCandidate);
            res.redirect(Routes.PATH_WELCOME);
            System.out.println("LOGINCONTROLLER: User is login success");
        }
        else{
            session.attribute("Test", result.msg);
            res.redirect(Routes.PATH_HOME);
            System.out.println("LOGINCONTROLLER: User is login failed");
        }
        
        return null;
    }
    
    
    public static String logOut(Request req, Response res){
        if(!ensureLoggedIn(req, res)){
            res.redirect(Routes.PATH_HOME);
        }
        Session session = req.session();
        String username = session.attribute(ATRIB_USERNAME);
        
        UserCollection.DataResponse result = Main.users.removeByUsername(username);
        if(result.success){
            session.removeAttribute(ATRIB_USERNAME);
            res.redirect(Routes.PATH_WELCOME);
        }
        else{
            session.attribute("Test", result.msg);
            res.redirect(Routes.PATH_HOME);
        }
        return null;
    }

}
