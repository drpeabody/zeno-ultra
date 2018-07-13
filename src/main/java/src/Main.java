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
    
    public static void main(String[] args) {
        
//        Spark.staticFileLocation("/client");
        Spark.port(3000);
        
        setRoutes();
    }
    
    static void setRoutes(){
        Spark.before(addTrailingSlashes);
        
        Spark.get(Routes.PATH_HOME, (req, res) -> {
            System.out.println("Home called");
            return HomeController.render(req, res);
        });
        Spark.get(Routes.PATH_WELCOME, (req, res) -> WelcomeController.render(req, res));
        Spark.get(Routes.PATH_LOGIN, (req, res) -> HomeController.render(req, res));
        Spark.get(Routes.PATH_LOGOUT, (req, res) -> {
            res.redirect(Routes.PATH_WELCOME);
            return null;
        });
        
        Spark.post(Routes.PATH_LOGIN, (req, res) -> {
            System.out.println("Login Button Pressed.");
            return LoginController.logIn(req, res);
        });
        Spark.post(Routes.PATH_LOGOUT, (req, res) -> LoginController.logOut(req, res));
        
    }
    
    public static Filter addTrailingSlashes = (req, res) -> {
        if (!req.pathInfo().endsWith("/")) {
            res.redirect(req.pathInfo() + "/");
        }
    };
    
}
