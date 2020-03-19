package ConfigSettings;

public class Main_config_file {
    //public static String base_location = System.getProperty("user.dir");
    private static String logFolder = "src\\main\\java\\logs";
    private static String user_list_location = "src\\main\\java\\logs\\users.txt";
    private static String allCardsJSONFile = "src\\main\\java\\Card\\allCards.txt";
    private static String userBaseSaveLocation = "src\\main\\java\\SaveData";
    private static String gameBaseCardSaveLocation = "src\\main\\java\\ConfigSettings\\Cards";

    private static String getUserBaseSaveLocation() {
        return userBaseSaveLocation;
    }

    private static String getGameBaseCardSaveLocation() {
        return gameBaseCardSaveLocation;
    }

    public static String returnUserSaveDataLocation(String username, int userID){
        return getUserBaseSaveLocation() + "\\" + username + "-" + userID + ".ser";
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