package src.util;

import java.util.HashMap;
import java.util.Properties;
import org.apache.velocity.app.VelocityEngine;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

/**
 * @since Jul 12, 2018
 * @author Abhishek
 */
public class ViewUtil {
    private static VelocityTemplateEngine engine = null;
    private static HashMap<String, Object> model = new HashMap<>();
    
    public static void setProperty(String prop, Object val){
        model.put(prop, val);
    }
    public static Object getProperty(String prop){
        return model.get(prop);
    }
    public static void clearProperties(){
        model.clear();
    }
    public static int numProperties(){
        return model.size();
    }
    public static boolean isModelEmpty(){
        return model.isEmpty();
    }
    
    public static VelocityTemplateEngine getEngine(){
        if(engine == null){
            Properties properties = new Properties();
            properties.setProperty("resource.loader", "class");
            properties.setProperty(
                "class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            engine = new VelocityTemplateEngine( new VelocityEngine(properties));
        }
        return engine;
    }
    
    
    public static String renderTemplate(String path){
        model.put("val", "This is Value of val.");
        return getEngine().render(new ModelAndView(model, path));
    }

}
