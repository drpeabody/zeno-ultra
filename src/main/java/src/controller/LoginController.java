package src.controller;

import spark.Request;
import spark.Response;
import spark.Session;
import src.Main;
import src.Routes;

/**
 * @since Jul 13, 2018
 * @author Abhishek
 */
public class LoginController {
    public static final String ATTRIB_USERNAME = "currentUserName";
    public static final String ATTRIB_RES_MSG = "msg";
    
    //Add User Expiry for lost connections handling
    
    public static boolean ensureLoggedIn(Request req, Response res){
        String username = req.session().attribute(ATTRIB_USERNAME);
        return Main.users.contains(username);
    }
    
    public static String logIn(Request req, Response res){
        if(ensureLoggedIn(req, res)) {
            res.redirect(Routes.PATH_WELCOME);
            return null;
        }
        
        String usernameCandidate = req.queryParams("username");
        Session session = req.session();
        
        if(usernameCandidate == null || usernameCandidate.isEmpty()){
            session.attribute(ATTRIB_RES_MSG, "Username is invalid.");
            res.status(Main.HTTP_RES_BAD_REQUEST);
        } 
        else if (Main.users.insert(session.id(), usernameCandidate).success) {
            session.attribute(ATTRIB_USERNAME, usernameCandidate);
            res.redirect(Routes.PATH_WELCOME);
            return null;
        } 
        else {
            session.attribute(ATTRIB_RES_MSG, "Username is already taken.");
            res.status(Main.HTTP_RES_UNAUTHORIZED);
        }

        return HomeController.renderUnsafe(req);
    }
    
    public static String logOut(Request req, Response res){
        if(!ensureLoggedIn(req, res)){
            res.redirect(Routes.PATH_HOME);
            return null;
        }
        
        Session session = req.session();
        String username = session.attribute(ATTRIB_USERNAME);
        
        if (Main.users.removeByUsername(username).success) {
            session.removeAttribute(ATTRIB_USERNAME);
            res.redirect(Routes.PATH_HOME);
            return null;
        } 
        else {
            session.attribute(ATTRIB_RES_MSG, "Internal Error Occoured");
            res.status(Main.HTTP_RES_UNAUTHORIZED);
        }

        return WelcomeController.render(req, res);
    }

}
