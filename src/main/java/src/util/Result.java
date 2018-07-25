package src.util;

/**
 * @since Jul 17, 2018
 * @author Abhishek
 */
public class Result{
    public final String msg;
    public final boolean success;

    public Result(String msg, boolean success){
        this.msg = msg;
        this.success = success;
    }

    public static Result successResponse(String msg){
        return new Result(msg, true);
    }
    public static Result failedResponse(String msg){
            return new Result(msg, false);
        }
    }
