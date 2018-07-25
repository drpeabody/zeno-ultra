package src.controller;

import spark.Request;
import spark.Response;
import src.Main;
import src.Routes;
import src.util.ViewUtil;

/**
 * @since Jul 13, 2018
 * @author Abhishek
 */
public class WelcomeController {
    public static final String ATTRIB_RES_MSG = "msg";
    public static final String ATTRIB_IS_IN_A_ROOM = "isInRoom";
    
    public static String render(Request req, Response res){
        if(!LoginController.isSessionLoggedIn(req, res)){
            res.redirect(Routes.PATH_HOME);
            return null;
        }
        
        ViewUtil.setProperty("username", req.session().attribute(LoginController.ATTRIB_USERNAME));
        ViewUtil.setProperty("num", Main.users.count());
        return ViewUtil.renderTemplate("welcome.vtl");
    }
    
    public static String renderUnsafe(Request req, Response res){
        ViewUtil.setProperty("username", req.session().attribute(LoginController.ATTRIB_USERNAME));
        ViewUtil.setProperty("num", Main.users.count());
        return ViewUtil.renderTemplate("welcome.vtl");
    }
}