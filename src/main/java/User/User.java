package User;

import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import Utility.FileFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class User {
    static Logger main_logger = LoggingClass.getMainLoggerInstance();
    // FOR LOGIN, CHECK THAT THE USER IS NOT DELETED.
    public static boolean userLogIn(String username, String password){
        long userID = findUserID(username);
        if (userID == -1){
            main_logger.info("Error. No such user or user has been deleted.");
            return false;
        }
        String passwordvalid = PasswordUsername.PasswordValidityCheck(username, userID, password);
        if (passwordvalid.equals("Incorrect")){
            main_logger.info("Password incorrect.");
            return false;
        }
        if (passwordvalid.equals("UserNonExistent")){
            main_logger.info("What the hell!? I found this user's userID through searching the" +
                    " user list but now I couldn't find it.");
            return false;
        }
        // instantiate the user logger.
        Logger user_logger = LoggingClass.getUserLogger(username, userID);
        try {
            user_logger.info("Log in.");
        } catch (Exception e){
            main_logger.info("The user logger could not be instantiated.");
            return false;
        }

        //SAVE USER LAST LOG IN TIME.
        //LOAD THE GOD DAMN USER DATA.




        return true;
    }
    public static void userLogOut(String username){
        Logger user_logger = LoggingClass.getUserLogger();
        user_logger.info("Log out at user request.");
        // SAVE THE USER DATA. ALSO SAVE THE LOG OUT TIME.

        LoggingClass.closeUserLogger();
    }

    public static long findUserID(String username){
        JSONArray array = getUserArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject temp_user = (JSONObject) array.get(i);
            if (temp_user.get("Username").equals(username) && temp_user.get("Deleted At").equals("None")) {
                return (long)temp_user.get("UserID");
            }
        }
        return -1;
    }


    public static JSONArray getUserArray() {
        return FileFunctions.loadJsonArray(Main_config_file.getUser_list_location());
    }
    public static JSONObject getUserFromArray(String username, long userID) {
        JSONArray array = User.getUserArray();
        JSONObject result = null;
        boolean flag = false;
        for (int i = 0; i < array.size(); i++) {
            JSONObject temp_user = (JSONObject) array.get(i);
            if (temp_user.get("Username").equals(username) && (((long) temp_user.get("UserID")) == userID)
                    && temp_user.get("Deleted At").equals("None")) {
                flag = true;
                result = (JSONObject) array.get(i);
                break;
            }
        }
        if (!flag) main_logger.info("Error. Could not find the user in the user list.");
        return result;
    }
    public static JSONObject getUserFromArray(JSONArray array, String username, long userID) {
        JSONObject result = null;
        boolean flag = false;
        for (int i = 0; i < array.size(); i++) {
            JSONObject temp_user = (JSONObject) array.get(i);
            if (temp_user.get("Username").equals(username) && (((long) temp_user.get("UserID")) == userID)
                    && temp_user.get("Deleted At").equals("None")) {
                flag = true;
                result = (JSONObject) array.get(i);
                break;
            }
        }
        if (!flag) main_logger.info("Error. Could not find the user in the user list.");
        return result;
    }
}