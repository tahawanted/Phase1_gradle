package User;

import Card.Cards;
import ConfigSettings.Main_config_file;
import Hero.Hero;
import LoggingModule.LoggingClass;
import Utility.FileFunctions;
import org.json.simple.JSONObject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import static Utility.SerializationFunctions.*;

public class User implements Serializable {
    private String username;
    private long userID;
    private String hashedPassword;
    private int walletBalance;
    private ArrayList<Hero> heroes = new ArrayList<>();
    private int currentHeroIndex;
    transient Logger main_logger = LoggingClass.getMainLoggerInstance();
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

    public int buyCard(String cardName){
        return heroes.get(currentHeroIndex).buyCard(cardName, this);
    }
    public int sellCard(String cardName){
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
    public String getCardsInDeck(){
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
    public void printCardInformation(String cardName){
        System.out.println(FileFunctions.prettyJsonString(
                cardDeserialize(Main_config_file.returnCardSaveDataLocation(cardName)).getCardJsonObject()));
    }
    public void printCardInformation(ArrayList<String> cardNames){
        for (String cardName: cardNames) printCardInformation(cardName);
    }
    public void printDeckCards(){
        for (Object st:heroes.get(currentHeroIndex).getDeckCards().keySet()){
            printCardInformation(st.toString());
        }
    }
    public void printAvailableCards(){
        printCardInformation(getAvailableCards());
    }
    public void printBuyableCards(){
        printCardInformation(getComplementAvailableCards());
    }
}
