package User;

import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.logging.Logger;

public class DeleteUser {
    static Logger main_logger = LoggingClass.getMainLoggerInstance();
    public static void deleteuser(String username, long userID, String password){
        // I HAVE NOT CHECKED TO  SEE IF THE USER IS ALREADY DELETED OR NOT AND HAVE ASSUMED THAT THIS WON'T HAPPEN
        String passwordIsOk = PasswordUsername.PasswordValidityCheck(username, userID, password);
        if (passwordIsOk.equals("Correct")) {
            main_logger.info("Deleting user " + username);
            /////// First I shall add the deletion time to the userLog:
            String oldLine = "Deleted At:";
            String newLine = "Deleted At: " + LocalDateTime.now().toString();
            Utility.FileFunctions.replaceLine(username, userID, oldLine, newLine);

            // Now I must also change the status in userList
            JSONArray array = User.getUserArray();
            JSONObject userToDelete = User.getUserFromArray(array, username, userID);
            userToDelete.remove("Deleted At");
            userToDelete.put("Deleted At", LocalDateTime.now().toString());
            Utility.FileFunctions.SaveJsonUserListArray(array);

        }
    }

    public static void main(String[] args) {
        deleteuser("taha", 0, "vetsihatred");
    }
}
