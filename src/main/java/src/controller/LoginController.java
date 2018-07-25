package src.controller;

import spark.Request;
import spark.Response;
import spark.Session;
import src.Main;
import src.Routes;
import src.UserCollection.User;
import src.util.Result;

/**
 * @since Jul 13, 2018
 * @author Abhishek
 */
public class LoginController {
    public static final String ATTRIB_USERNAME = "currentUserName";
    public static final String ATTRIB_RES_MSG = "msg";
    
    //Add User Expiry for lost connections handling
    
    public static boolean isSessionLoggedIn(Request req, Response res){
        String name = req.session().attribute(ATTRIB_USERNAME);
        return name != null && !name.isEmpty();
    }
    
    public static String logIn(Request req, Response res){
        if(isSessionLoggedIn(req, res)) {
            res.redirect(Routes.PATH_WELCOME);
            return null;
        }
        
        String usernameCandidate = req.queryParams("username");
        String plainTextpwd = req.queryParams("pass");
        Session session = req.session();
        
        if(usernameCandidate == null || usernameCandidate.isEmpty()){
            session.attribute(ATTRIB_RES_MSG, "Username is invalid.");
            res.status(Main.HTTP_RES_BAD_REQUEST);
        } 
        
        Result r = Main.users.logIn(usernameCandidate, plainTextpwd);
        
        if (r.success) {
            session.attribute(ATTRIB_USERNAME, usernameCandidate);
            res.redirect(Routes.PATH_WELCOME);
            return null;
        } 
        else {
            session.attribute(ATTRIB_RES_MSG, r.msg);
            res.status(Main.HTTP_RES_UNAUTHORIZED);
        }

        return HomeController.renderUnsafe(req);
    }
    
    public static String register(Request req, Response res){
        if(isSessionLoggedIn(req, res)) {
            res.redirect(Routes.PATH_WELCOME);
            return null;
        }
        
        String usernameCandidate = req.queryParams("username");
        String plainTextpwd = req.queryParams("pass");
        Session session = req.session();
        
        if(usernameCandidate == null || usernameCandidate.isEmpty()){
            session.attribute(ATTRIB_RES_MSG, "Username is invalid.");
            res.status(Main.HTTP_RES_BAD_REQUEST);
        } 
        
        Result r = Main.users.insert(usernameCandidate, plainTextpwd);
        if (r.success) {
            session.attribute(ATTRIB_RES_MSG, "Registration Successful.");
        } 
        else {
            session.attribute(ATTRIB_RES_MSG, r.msg);
            res.status(Main.HTTP_RES_UNAUTHORIZED);
        }
        
        return HomeController.renderUnsafe(req);
    }

    
    public static String logOut(Request req, Response res){
        if(!isSessionLoggedIn(req, res)){
            res.redirect(Routes.PATH_HOME);
            return null;
        }
        
        Session session = req.session();
        String username = session.attribute(ATTRIB_USERNAME);
        User u = Main.users.get(username);
        
        if (u != null) {
            u.logOut();
            session.removeAttribute(ATTRIB_USERNAME);
            res.redirect(Routes.PATH_HOME);
            return null;
        } 
        else {
            session.attribute(ATTRIB_RES_MSG, "Error, Can't Logout.");
            res.status(Main.HTTP_RES_UNAUTHORIZED);
        }

        return WelcomeController.render(req, res);
    }

}
