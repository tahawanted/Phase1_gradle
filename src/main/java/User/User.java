package User;

import ConfigSettings.Main_config_file;
import Hero.Hero;
import LoggingModule.LoggingClass;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import static Utility.SerializationFunctions.Serialize;

public class User implements Serializable {
    private String username;
    private int userID;
    private String hashedPassword;
    private int walletBalance;
    private ArrayList<Hero> heroes = new ArrayList<>();
    private int currentHeroIndex;
    Logger main_logger = LoggingClass.getMainLoggerInstance();
    User(String username, int userID, String hashedPassword){
        this.username = username;
        this.userID = userID;
        this.hashedPassword = hashedPassword;
        walletBalance = 100;
        heroes.add(new Hero("Mage", "The sorceress of hearthstone. At her hand, all non-mage spells " +
                "cost 2 mana less.",true));
        heroes.add(new Hero("Rogue", "The thief of these wastelands. All non-neutral and non-rogue " +
                "cards cost 2 less for her.", true));
        heroes.add(new Hero("Warlock", "The sacrificing demon around here. He will sacrifice cards " +
                "and health points in order to win. The gods have gifted him with 5 more health points",
                true));
        currentHeroIndex = 0;
    }
    public void changeHero(String heroName){
        int index = -1;
        for (int i = 0; i<heroes.size(); i++){
            if (heroes.get(i).getName().equals(heroName)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            main_logger.info("Error. The provided hero name is incorrect.");
            return;
        }
        if (currentHeroIndex == index) {
            main_logger.info("Error/Warning. The provided hero is already selected. Returning.");
            return;
        }
        currentHeroIndex = index;
        main_logger.info("Successfully changed hero to " + heroes.get(index).getName());
    }

    public boolean buyCard(String cardName){
        return heroes.get(currentHeroIndex).buyCard(cardName, this);
    }
    public boolean sellCard(String cardName){
        return heroes.get(currentHeroIndex).sellCard(cardName, this);
    }
    public boolean addToDeck(String cardName){
        return heroes.get(currentHeroIndex).addToDeck(cardName);
    }
    public boolean removeFromDeck(String cardName){
        return heroes.get(currentHeroIndex).removeFromDeck(cardName);
    }

    public void serializeUser(){
        Serialize(this, Main_config_file.returnUserSaveDataLocation(username, userID));
    }
    public static User deserializeUser(String username, int userID){
        User userObject = deserializeUser(username, userID);
        return userObject;
    }

    public int getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        assert walletBalance >= 0;
        this.walletBalance = walletBalance;
    }
}
