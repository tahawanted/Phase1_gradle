package ConfigSettings;

public class Main_config_file {
    //public static String base_location = System.getProperty("user.dir");
    private static String logFolder = "src\\logs";
    private static String user_list_location = "src\\logs\\users.txt";
    private static String allCardsJSONFile = "src\\Card\\allCards.txt";

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