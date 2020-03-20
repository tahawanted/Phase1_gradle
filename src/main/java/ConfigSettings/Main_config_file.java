package ConfigSettings;

import java.io.File;
import java.util.ArrayList;

public class Main_config_file {
    //public static String base_location = System.getProperty("user.dir");
    private static String base1 = "src";
    private static String base2 = base1 + "\\main";
    private static String base3 = base2 + "\\java";
    private static String base4 = base3 + "\\SaveData";
    private static String logFolder = base4 + "\\logs";
    private static String gameBaseCardSaveLocation = base4 + "\\Cards";
    private static String usersFolder = base4 + "\\users";

    // Add the above base directories to the below arrayList;
    private static ArrayList<File> locationFiles = new ArrayList<>();
    static {
        locationFiles.add(new File(base1));
        locationFiles.add(new File(base2));
        locationFiles.add(new File(base3));
        locationFiles.add(new File(base4));
        locationFiles.add(new File(logFolder));
        locationFiles.add(new File(gameBaseCardSaveLocation));
        locationFiles.add(new File(usersFolder));
    }
    private static String user_list_location = usersFolder + "\\users.txt";
    private static String allCardsJSONFile = gameBaseCardSaveLocation + "\\allCards.txt";

    public static boolean createRequiredDirectories(){
        ArrayList<Boolean> flags = new ArrayList<Boolean>();
        int i = 0;
        while(true){
            System.out.println();
            if (i == locationFiles.size()) break;
            if(locationFiles.get(i).exists()) {
                i++;
                continue;
            }
            flags.add(locationFiles.get(i).mkdir());
            i++;
        }
        for(Boolean flag: flags){
            if (! flag) return false;
        }
        return true;
    }

    private static String getBase4() {
        return base4;
    }

    private static String getGameBaseCardSaveLocation() {
        return gameBaseCardSaveLocation;
    }

    public static String getUsersFolder() {
        return usersFolder;
    }

    public static String returnUserSaveDataLocation(String username, long userID){
        return getUsersFolder() + "\\" + username + "-" + userID + ".ser";
    }
    public static String returnCardSaveDataLocation(String cardName){
        return getGameBaseCardSaveLocation() + "\\" + cardName + ".ser";
    }

    public static String getLogFolder() {
        return logFolder;
    }

    public static String getUser_list_location() {
        return user_list_location;
    }

    public static String getAllCardsJSONFile() {
        return allCardsJSONFile;
    }
}