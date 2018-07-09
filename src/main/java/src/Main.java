package src;

import static spark.Spark.*;

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello World.");
        get("/hello", (req, res) -> "Hello World");
    }
}
