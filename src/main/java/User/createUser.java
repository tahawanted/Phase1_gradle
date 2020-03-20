package User;
import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;



import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import static LoggingModule.LoggingClass.returnUserLoggerPath;


public class createUser {
    private static long userID = 0;
    private static Logger main_logger = LoggingClass.getMainLoggerInstance();

    private static JSONArray assignUserID(){
        JSONArray array = UserFunctions.getUserArray();
        JSONObject temp_user = (JSONObject) array.get(array.size() - 1);
        userID = (long) temp_user.get("UserID") + 1;
        main_logger.info("Assigning userID " + userID);
        return array;
    }
    private static void createUserLog(String username, long userID, String passwordHash, String creationTime){
        try {
            File file = new File(returnUserLoggerPath(username, userID));
            FileWriter fr = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fr);
            out.write("Username: " + username +"\nuser ID: " + userID + "\nPassword Hash:" + passwordHash
                    + "\nCreation Time: " + creationTime + "\nDeleted At:" + "\n\n\n\n\n\n\n\n\n\nUser logs:\n");
            out.close();

            /*
            FileHandler userFh;
            Logger user_logger = Logger.getLogger("user_logger");
            userFh = new FileHandler(returnUserLoggerPath(username, userID),true);

            user_logger.addHandler(userFh);
            SimpleFormatter formatter = new SimpleFormatter();
            userFh.setFormatter(formatter);
            user_logger.info("Created current user's logger.");
             */
        } catch (IOException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
    }

    public static User createAUser(String username, String password) {
        JSONObject user = new JSONObject();
        JSONArray array = null;
        // Replace the user password with its hash.

        try {
            password = HashLib.toHexString(HashLib.getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }

        File file = new File(Main_config_file.getUser_list_location());
        boolean exists = file.exists();

        if (!exists) {
            array = new JSONArray();
            main_logger.info("Assigning userID " + userID);
        } else {
            array = assignUserID();
        }
        // add the user to the json array.
        main_logger.info("Creating user. Username: " + username);
        user.put("Username", username);
        user.put("Password", password);
        user.put("UserID", userID);
        user.put("Created At", LocalDateTime.now().toString());
        user.put("Deleted At", "None");
        array.add(user);
        main_logger.info("Creating user log");
        createUserLog(username, userID, password, LocalDateTime.now().toString());
        main_logger.info("Creating user class instance");
        User newUser = new User(username, userID, password);
        main_logger.info("User class instantiated. Serializing");
        newUser.serializeUser();

        // write changes to file.
        Utility.FileFunctions.saveJsonArray(array, Main_config_file.getUser_list_location());
        return newUser;
    }
}

