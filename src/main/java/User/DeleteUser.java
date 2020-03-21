package User;

import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class DeleteUser {
    private static Logger main_logger = LoggingClass.getMainLoggerInstance();
    public static void deleteUser(String username, long userID){
        // I HAVE NOT CHECKED TO  SEE IF THE USER IS ALREADY DELETED OR NOT AND HAVE ASSUMED THAT THIS WON'T HAPPEN
        main_logger.info("Deleting user " + username);
        /////// First I shall add the deletion time to the userLog:
        String oldLine = "Deleted At:";
        String newLine = "Deleted At: " + LocalDateTime.now().toString();
        Utility.FileFunctions.replaceLine(username, userID, oldLine, newLine);

        // Now I must also change the status in userList
        JSONArray array = UserFunctions.getUserArray();
        JSONObject userToDelete = UserFunctions.getUserFromArray(array, username, userID);
        userToDelete.remove("Deleted At");
        userToDelete.put("Deleted At", LocalDateTime.now().toString());
        Utility.FileFunctions.saveJsonArray(array, Main_config_file.getUser_list_location());


    }

}
