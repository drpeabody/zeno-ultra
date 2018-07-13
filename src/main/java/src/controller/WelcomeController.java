package src.controller;

import java.util.HashMap;
import spark.Request;
import spark.Response;
import src.Routes;
import src.util.ViewUtil;

/**
 * @since Jul 13, 2018
 * @author Abhishek
 */
public class WelcomeController {
    
    public static String render(Request req, Response res){
        if(!LoginController.ensureLoggedIn(req, res)){
            res.redirect(Routes.PATH_HOME);
            return null;
        }
        HashMap<String, String> p = new HashMap();
        p.put("username", req.session().attribute(LoginController.ATRIB_USERNAME));
        return ViewUtil.renderTemplate(p, "welcome.vtl");
    }

}
