package src;

import spark.Filter;
import spark.Spark;
import src.controller.HomeController;
import src.controller.LoginController;
import src.controller.WelcomeController;

public class Main {
    /**
     * @param args the command line arguments
     */
    
    public static final UserCollection users = new UserCollection();
    
    public static final int HTTP_RES_CONTINUE = 100;
    public static final int HTTP_RES_SWITCH_PROTOCOL = 101;
    public static final int HTTP_RES_PROCESSING = 102;
    
    public static final int HTTP_RES_OK = 200;
    public static final int HTTP_RES_CREATED = 201;
    public static final int HTTP_RES_ACCEPTED = 202;
    public static final int HTTP_RES_UNRELIABLE_INFO = 203;
    public static final int HTTP_RES_NO_INFO = 204;
    public static final int HTTP_RES_REFRESH = 205;
    public static final int HTTP_RES_PARTIAL_INFO = 206;
    
    public static final int HTTP_RES_MANY_CHOICES = 300;
    public static final int HTTP_RES_MOVED_PERMANENT = 301;
    public static final int HTTP_RES_SEND_GET_TO_NEW_URL = 302;
    public static final int HTTP_RES_SEND_POST_TO_NEW_URL = 303;
    
    public static final int HTTP_RES_BAD_REQUEST = 400;
    public static final int HTTP_RES_UNAUTHORIZED = 401;
    public static final int HTTP_RES_FORBIDDEN = 403;
    public static final int HTTP_RES_NOT_FOUND = 404;
    
    
    public static void main(String[] args) {
        
//        Spark.staticFileLocation("/client");
        Spark.port(3000);
        
        setRoutes();
    }
    
    static void setRoutes(){
        Spark.before(addTrailingSlashes);
        
        Spark.get(Routes.PATH_HOME, (req, res) -> HomeController.render(req, res));
        Spark.get(Routes.PATH_WELCOME, (req, res) -> WelcomeController.render(req, res));
        Spark.get(Routes.PATH_LOGIN, (req, res) -> HomeController.render(req, res));
        Spark.get(Routes.PATH_LOGOUT, (req, res) -> WelcomeController.render(req, res));
        
        Spark.post(Routes.PATH_LOGIN, (req, res) -> LoginController.logIn(req, res));
        Spark.post(Routes.PATH_LOGOUT, (req, res) -> LoginController.logOut(req, res));
        
    }
    
    public static Filter addTrailingSlashes = (req, res) -> {
        if (!req.pathInfo().endsWith("/")) {
            res.redirect(req.pathInfo() + "/");
        }
    };
    
}
