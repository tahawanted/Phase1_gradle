package User;

import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;


class HashLib {
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
}

public class PasswordUsername {
    private static Logger main_logger = LoggingClass.getMainLoggerInstance();
    static public String passwordFormat = "The password must contain lower case and upper " +
            "case\n english characters and must also include numbers. It must also be at least 8 characters long.";

    public static String PasswordValidityCheck(String username, long userID, String password){
        main_logger.info("Checking password validity");
        String hashedPassword = "";
        try {
            hashedPassword = HashLib.toHexString(HashLib.getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            main_logger.info(e.getMessage());
        }
        JSONArray array = UserFunctions.getUserArray();
        for (int i=0; i<array.size(); i++){
            JSONObject temp_user = (JSONObject) array.get(i);
            if (temp_user.get("Username").equals(username) && (((long)temp_user.get("UserID")) == userID)){
                if(temp_user.get("Password").equals(hashedPassword)){
                    return "Correct";
                } else {
                    main_logger.info("Incorrect password entered for user " + username);
                    return "Incorrect";
                }
            } else {
                main_logger.info("User Does not exist");
                return "UserNonExistent";
            }
        }
        return "Incorrect";
    }
    public static boolean PasswordFormatCheck(String password){
        boolean lengthFlag = password.length() >= 8;
        boolean numericFlag = password.matches(".*\\d.*");
        char ch;
        boolean upperCaseFlag = false;
        for (int i = 0; i<password.length(); i++){
            ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                upperCaseFlag = true;
                break;
            }
        }
        if (!numericFlag || !upperCaseFlag || !lengthFlag){
            main_logger.info("Incorrect password format. " + passwordFormat);
            return false;
        }
        return true;
    }
    public static void ChangePassword(String username, long userID, String currentPassword, String newPassword){
        String passwordValidity = PasswordValidityCheck(username, userID, currentPassword);
        if (passwordValidity.equals("Correct")){
            JSONArray array = UserFunctions.getUserArray();
            JSONObject user = UserFunctions.getUserFromArray(array, username, userID);
            String oldLine = "Password:.*";
            String newPasswordHash = null;
            try {
                newPasswordHash = HashLib.toHexString(HashLib.getSHA(newPassword));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                main_logger.info(e.getMessage());
            }

            String newLine = "Password: " + newPasswordHash;
            Utility.FileFunctions.replaceLine(username, userID, oldLine, newLine);

            user.remove("Password");
            user.put("Password", newPasswordHash);
            Utility.FileFunctions.saveJsonArray(array, Main_config_file.getUser_list_location());
            main_logger.info("Password change successful.");
        } else {
            main_logger.info("Warning. Password incorrect. Password change failed.");
        }


    }
    public static boolean CheckUsernameValidity(String username){
        File file = new File(Main_config_file.getUser_list_location());
        if(file.exists()) {
            JSONArray array = UserFunctions.getUserArray();
            for (int i = 0; i < array.size(); i++) {
                JSONObject temp_user = (JSONObject) array.get(i);
                if (temp_user.get("Username").equals(username) && temp_user.get("Deleted At").equals("None")) {
                    main_logger.info("Error. The current username is taken. Chose another username");
                    return false;
                }
            }
        }
        return true;
    }
}

