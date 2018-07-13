package src.controller;

import java.util.HashMap;
import spark.Request;
import spark.Response;
import src.Routes;
import src.util.ViewUtil;

/**
 * @since Jul 12, 2018
 * @author Abhishek
 */
public class HomeController {
    
    public static String render(Request req, Response res){
        if(LoginController.ensureLoggedIn(req, res)){
            System.out.println("HOMECONTROLLER: User is already Logged in");
            res.redirect(Routes.PATH_WELCOME);
            return null;
        }
//        HashMap<String, String> p = new HashMap<String, String>();
//        p.put("username", req.attribute(null))
        return ViewUtil.renderTemplate(new HashMap<>(), "home.vtl");
        
    }

}
