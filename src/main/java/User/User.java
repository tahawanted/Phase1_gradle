package User;

import ConfigSettings.Main_config_file;
import Hero.Hero;
import LoggingModule.LoggingClass;
import Utility.FileFunctions;
import org.json.simple.JSONObject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import static ColorCommandLine.ColorCommandLine.*;
import static Utility.SerializationFunctions.*;

public class User implements Serializable {
    private String username;
    private long userID;
    private String hashedPassword;
    private int walletBalance;
    private ArrayList<Hero> heroes = new ArrayList<>();
    private int currentHeroIndex;
    User(String username, long userID, String hashedPassword){
        this.username = username;
        this.userID = userID;
        this.hashedPassword = hashedPassword;
        walletBalance = 100;
        heroes.add(new Hero("Mage", "The sorceress of hearthstone. At her hand, all non-mage spells " +
                "cost 2 mana less.",true,
                "Fireblast", 2, "Deal 1 damage."));
        heroes.add(new Hero("Rogue", "The thief of these wastelands. All non-neutral and non-rogue " +
                "cards cost 2 less for her.", true,
                "Her Hand", 3, "Pick a random card from the " +
                "opponents deck and add it to your hand. If the hero is equipped with a weapon," +
                " pick a random card from your own deck as well"));
        heroes.add(new Hero("Warlock", "The sacrificing demon around here. He will sacrifice cards " +
                "and health points in order to win. The gods have gifted him with 5 more health points",
                true, "Sacrifice thai life", 2,
                "Reduce 2 health points and then choose one of the following actions to do:" +
                        " 1.Give a random minion +1/+1 2.Add a card to your hand. "));
        currentHeroIndex = 0;
    }



    public int changeHero(String heroName, Logger user_logger){
        int index = -1;
        for (int i = 0; i<heroes.size(); i++){
            if (heroes.get(i).getName().equals(heroName)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            user_logger.info("Error. The provided hero name is incorrect.");
            return 404;
        }
        if (currentHeroIndex == index) {
            user_logger.info("Error/Warning. The provided hero is already selected. Returning.");
            return 400;
        }
        currentHeroIndex = index;
        user_logger.info("Successfully changed hero to " + heroes.get(index).getName());
        return 200;
    }

    public int buyCard(String cardName){
        return heroes.get(currentHeroIndex).buyCard(cardName, this);
    }
    public int sellCard(String cardName){
        return heroes.get(currentHeroIndex).sellCard(cardName, this);
    }
    public int addToDeck(String cardName){
        return heroes.get(currentHeroIndex).addToDeck(cardName);
    }
    public int removeFromDeck(String cardName){
        return heroes.get(currentHeroIndex).removeFromDeck(cardName);
    }

    public void serializeUser(){
        Serialize(this, Main_config_file.returnUserSaveDataLocation(username, userID));
    }
    public static User deserializeUser(String username, long userID){
        User userObject = userDeserialize(Main_config_file.returnUserSaveDataLocation(username, userID));
        return userObject;
    }

    public int getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        assert walletBalance >= 0;
        this.walletBalance = walletBalance;
    }
    public String getCurrentHeroName(){
        return heroes.get(currentHeroIndex).getName();
    }
    public ArrayList<String> getAllCards(){
        return heroes.get(currentHeroIndex).getAllCards();
    }
    public ArrayList<String> getAvailableCards(){
        return heroes.get(currentHeroIndex).getAvailableCards();
    }
    public ArrayList<String> getComplementAvailableCards(){
        return heroes.get(currentHeroIndex).getComplementAvailableCards();
    }
    public String getCardsInDeckString(){
        JSONObject deckCards = heroes.get(currentHeroIndex).getDeckCards();
        return FileFunctions.prettyJsonString(deckCards);
    }
    public int getHeroLevel(){
        return heroes.get(currentHeroIndex).getHeroLevel();
    }
    public int numberOfCardsOwnedByHero(){
        return heroes.get(currentHeroIndex).getNumberOfCardsOwnedByHero();
    }
    public String heroDescription(){
        return heroes.get(currentHeroIndex).getDescription();
    }
    public int currentNumberOfCardsInDeck(){
        return heroes.get(currentHeroIndex).getCurrentNumberOfCardsInDeck();
    }
    public int getDeckSize(){
        return heroes.get(currentHeroIndex).getDeckSize();
    }
    public void printCardInformation(String cardName){
        Logger user_logger = LoggingClass.getUserLogger();
        user_logger.info(FileFunctions.prettyJsonString(
                cardDeserialize(Main_config_file.returnCardSaveDataLocation(cardName)).getCardJsonObject()));
    }
    public void printCardInformation(ArrayList<String> cardNames){
        for (String cardName: cardNames) printCardInformation(cardName);
    }
    public void printDeckCardsWithDetails(){
        for (Object st:heroes.get(currentHeroIndex).getDeckCards().keySet()){
            printCardInformation(st.toString());
        }
    }
    public void printAllHeroesInformation(){
        Logger user_logger = LoggingClass.getUserLogger();
        for (Hero tempHero: heroes){
            user_logger.info(GREEN + "Hero" + RESET + " name: " + tempHero.getName());
            user_logger.info("Description:\n" + tempHero.getDescription());
        }
    }

    public void printAvailableCards(){
        printCardInformation(getAvailableCards());
    }
    public void printBuyableCards(){
        printCardInformation(getComplementAvailableCards());
    }
    public ArrayList<String> getAddableCards(){
        return heroes.get(currentHeroIndex).getAddableCards();
    }
    @Override
    public User clone() throws CloneNotSupportedException {
        return deserializeUser(username, userID);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
    public ArrayList<String> getHeroNames(){
        ArrayList<String> names = new ArrayList<>();
        for(Hero tempHero:heroes){
            names.add(tempHero.getName());
        }
        return names;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public int getCurrentHeroIndex() {
        return currentHeroIndex;
    }
}
