package src;

import java.util.Arrays;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Spark;
import src.controller.HomeController;
import src.controller.LoginController;
import src.controller.WelcomeController;

public class Main {
    
    public static final UserCollection users = new UserCollection();
    
    public static final String CMD_EXIT = "exit";
    public static final String DB_USER = "user";
    public static final String CMD_DB_LIST = "list";
    public static final String CMD_DB_PRINT = "print";
    
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
        input();
//        
//        String pwd = "YOLO", salt = BCrypt.gensalt(), pwdCheck = "YOLO";
//        
//        String hash = BCrypt.hashpw(pwd, salt);
//        System.out.println(BCrypt.checkpw(pwdCheck, hash));
//        
    }
    
    static void input(){
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Starting CLI in Main Thread");
        try{
            Runnable r = () -> {
                Scanner sc = new Scanner(System.in);
                while(true){
                    String cmd = sc.nextLine().toLowerCase().trim();
                    if(cmd.startsWith(DB_USER)){
                        logger.info("Database has " + Main.users.count() + " entries.");
                        if(cmd.endsWith(CMD_DB_LIST)) logger.info(Main.users.getAllUsernames().toString());
                        else if(cmd.endsWith(CMD_DB_PRINT)) logger.info(Main.users.print());
                        else logger.info("Unknown Option.");
                    }
                    else if(cmd.startsWith(CMD_EXIT)){
                        logger.info("Stopping System...");
                        System.exit(0);
                    }
                    else logger.info("Unknown Command \"" + cmd + "\".");
                    
                }
            };
            r.run();
        }catch(Exception e){}
    }
    
    static void setRoutes(){
        Spark.before((req, res) -> {
            if (!req.pathInfo().endsWith("/")) res.redirect(req.pathInfo() + "/");
        });
        
        Spark.get(Routes.PATH_HOME, (req, res) -> HomeController.render(req, res));
        Spark.get(Routes.PATH_WELCOME, (req, res) -> WelcomeController.render(req, res));
        Spark.get(Routes.PATH_LOGIN, (req, res) -> HomeController.render(req, res));
        Spark.get(Routes.PATH_LOGOUT, (req, res) -> WelcomeController.render(req, res));
        
        Spark.post(Routes.PATH_LOGIN, (req, res) -> LoginController.logIn(req, res));
        Spark.post(Routes.PATH_LOGOUT, (req, res) -> LoginController.logOut(req, res));
        Spark.post(Routes.PATH_REGISTER, (req, res) -> LoginController.register(req, res));
        
    }
    
}
