package src.controller;

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
        System.out.println("HomeController: Safe Render");
        if(LoginController.ensureLoggedIn(req, res)){
            res.redirect(Routes.PATH_WELCOME);
            return null;
        }
        ViewUtil.setProperty(LoginController.ATTRIB_RES_MSG, req.session().attribute(LoginController.ATTRIB_RES_MSG));
        return ViewUtil.renderTemplate("home.vtl");
    }
    
    public static String renderUnsafe(Request req){
        ViewUtil.setProperty(LoginController.ATTRIB_RES_MSG, req.session().attribute(LoginController.ATTRIB_RES_MSG));
        System.out.println("HoneController: Unsafe Render");
        return ViewUtil.renderTemplate("home.vtl");
    }

}
