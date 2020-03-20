package LoggingModule;
import ConfigSettings.Main_config_file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingClass {
    private static final Logger main_logger = Logger.getLogger("main_logger");
    private static final Logger user_logger = Logger.getLogger("user_logger");
    private static FileHandler fh, userFh;
    static SimpleFormatter formatter = new SimpleFormatter();
    static {
        try {
            fh = new FileHandler(returnMainLoggerPath());
            main_logger.addHandler(fh);
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
    }
    private LoggingClass(){}
    public static Logger getUserLogger(String username, long userID) {
        if (user_logger.getHandlers().length == 0) {
            try {
                userFh = new FileHandler(returnUserLoggerPath(username, userID),true);
                user_logger.addHandler(userFh);
                SimpleFormatter formatter = new SimpleFormatter();
                userFh.setFormatter(formatter);
                user_logger.info("Instantiated current user's logger.");
            } catch (IOException e) {
                e.printStackTrace();
                main_logger.info(e.getMessage());
            }
            return user_logger;
        } else {
            main_logger.info("Error. Tried to get a second user logger whilst another is already present.");
            return null;
        }
    }
    public static String returnUserLoggerPath(String username, long userID){
        return Main_config_file.getLogFolder() + "\\" + username + "-" + userID + ".log";
    }
    public static String returnMainLoggerPath(){
        return Main_config_file.getLogFolder() + "\\mainlog" +
                "_" + LocalDateTime.now().toString().substring(5,10) + "_"
                + LocalDateTime.now().toString().substring(11,13) + "_"
                + LocalDateTime.now().toString().substring(14,16) + ".log";
    }
    public static Logger getUserLogger(){
        return user_logger;
    }
    public static void closeUserLogger(){
        user_logger.removeHandler(userFh);
    }
    public static void closeMainLogger(){
        main_logger.removeHandler(fh);
    }
    public static Logger getMainLoggerInstance(){
        return main_logger;
    }
}
