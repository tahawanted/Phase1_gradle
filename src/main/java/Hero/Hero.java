package Hero;

import Card.Cards;
import ConfigSettings.Main_config_file;
import LoggingModule.LoggingClass;
import User.User;
import Utility.FileFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import static Utility.SerializationFunctions.cardDeserialize;

public class Hero implements Serializable {
    private String name;
    private int heroLevel;
    private int numberOfCardsOwnedByHero;
    private String description;
    private int maxHealth;
    private int currentHealth;
    private int shield;
    private int numberOfCardsToKeepInDeck = 20;
    private int currentNumberOfCardsInDeck = 20;
    private int maximumInstanceOfEachCard = 2;
    private ArrayList<String> allCards = new ArrayList<>(); // This will hold all neutral cards plus this heros
    // special cards.
    private ArrayList<String> availableCards = new ArrayList<>(); // Cards open for the current hero.
    private JSONObject deckCards = new JSONObject();
    transient private Logger main_logger = LoggingClass.getMainLoggerInstance();
    Powers.heroPowers heroPower;
    public Hero(String name, String description, boolean setAvailableCardsDefault, String heroPowerName,
                int heroPowerMana, String heroPowerDescription) {
        this.name = name;
        this.description = description;
        heroLevel = 1;
        numberOfCardsOwnedByHero = 10;
        setHealth();
        setAllCards();
        if (setAvailableCardsDefault) {
            setAvailableCards();
            for(String cardName: availableCards)
            deckCards.put(cardName, maximumInstanceOfEachCard);
        }
        heroPower = new Powers.heroPowers(heroPowerName, heroPowerMana, heroPowerDescription);
    }
    // Used for checking if the card name is correct
    public boolean cardInAllCards(String cardName){
        return allCards.contains(cardName);
    }
    // used for checking if the user is for example allowed to add the card to their deck or for example if the card
    // is already in the availableCards list, he must not be shown this card for buying.
    public boolean cardInAvailableCards(String cardName){
        return availableCards.contains(cardName);
    }
    public boolean cardInComplementAvailableCards(String cardName){
        ArrayList<String> complementAvailable = getComplementAvailableCards();
        return complementAvailable.contains(cardName);
    }
    public int cardInDeck(String cardName){
        if(deckCards.get(cardName) == null) return 0;
        return (int) deckCards.get(cardName);
    }


    public int buyCard(String cardName, User user){
        // The current implementation uses the fact that complement cards are created when this command is called
        // and using this, prevents buying cards already in you available cards
         if (!cardInAllCards(cardName)){
             main_logger.info("Error. The entered card name is not in the list of cards of the game.");
             return 404;
         }
        if (!cardInComplementAvailableCards(cardName)){
            main_logger.info("Error. You already own this card");
            return 400;
        }

        Cards.card tempCard = cardDeserialize(Main_config_file.returnCardSaveDataLocation(cardName));
        // CHECK WALLET BALANCE
        int price = tempCard.getPrice();
        int currentBalance = user.getWalletBalance();
        if (price > currentBalance){
            main_logger.info("Error. Insufficient balance");
            return 402;
        }
        // If wallet balance is also ok, reduce the card cost from the balance
        user.setWalletBalance(currentBalance - price);
        availableCards.add(cardName);
        main_logger.info("Purchase successful. Card added to your collection.");
        return 200;
    }
    public int sellCard(String cardName, User user){
        if (!cardInAllCards(cardName)){
            main_logger.info("Error. The entered card name is not in the list of cards of the game.");
            return 404;
        }
        if (!cardInAvailableCards(cardName)){
            main_logger.info("Error. You currently don't own this card.");
            return 401;
        }
        if (cardInDeck(cardName) > 0){
            main_logger.info("Error. The card you want to sell is in your deck. First remove it from your deck");
            return 403;
        }
        // ADD TO WALLET BALANCE
        Cards.card tempCard = cardDeserialize(Main_config_file.returnCardSaveDataLocation(cardName));
        user.setWalletBalance(user.getWalletBalance() + tempCard.getPrice());
        availableCards.remove(cardName);
        return 200;
    }
    public boolean addToDeck(String cardName){
        if (!cardInAllCards(cardName)){
            main_logger.info("Error. The entered card name is not in the list of cards of the game.");
            return false;
        }
        if(!cardInAvailableCards(cardName)){
            main_logger.info("You do not own this card. You can buy it at the store.");
            return false;
        }
        if (currentNumberOfCardsInDeck == numberOfCardsToKeepInDeck) {
            main_logger.info("The deck is at full capacity. First remove a card.");
            return false;
        }
        int recurrence = cardInDeck(cardName);
        if (recurrence == 2){
            main_logger.info("There are already 2 instances of this card in your deck");
            return false;
        }
        deckCards.put(cardName, recurrence + 1);
        return true;
    }
    public boolean removeFromDeck(String cardName){
        if (!cardInAllCards(cardName)){
            main_logger.info("Error. The entered card name is not in the list of cards of the game.");
            return false;
        }
        if(!cardInAvailableCards(cardName)){
            main_logger.info("You do not own this card. You can buy it at the store.");
            return false;
        }
        if (currentNumberOfCardsInDeck == 0) {
            main_logger.info("The deck is already empty.");
            return false;
        }
        int recurrence = cardInDeck(cardName);
        if (recurrence == 0){
            main_logger.info("There are no instances of this card in your deck.");
            return false;
        }
        if (recurrence ==1) {
            deckCards.remove(cardName);
            main_logger.info("Removed card from deck.");
            return true;
        }
        deckCards.remove(cardName);
        deckCards.put(cardName,1);
        main_logger.info("Removed 1 instance of the card from the deck. 1 remaining.");
        return true;
    }

    private void setHealth(){
        switch (name){
            case "Warlock":
                currentHealth = 35;
                maxHealth = 35;
                break;
            default:
                currentHealth = 30;
                maxHealth = 30;
        }
        shield = 0;
    }
    private void setAllCards(){
        JSONArray array = FileFunctions.loadJsonArray(Main_config_file.getAllCardsJSONFile());
        JSONObject temp_obj;
        for (int i = 0; i<array.size(); i++){
            temp_obj = (JSONObject) array.get(i);
            String heroClass = (String) temp_obj.get("heroClass");
            if (heroClass.equals("Neutral") || heroClass.equals(name)){
                allCards.add((String) temp_obj.get("name"));
            }

        }
    }
    private void setAvailableCards(){
        if (name.equals("Mage")) {
            availableCards.add("Polymorph");
            availableCards.add("Goldshire Footman");
            availableCards.add("Stonetusk Boar");
            availableCards.add("Shieldbearer");
            availableCards.add("Novice Engineer");
            availableCards.add("Raid Leader");
            availableCards.add("Lord of the Arena");
            availableCards.add("Regenerate");
            availableCards.add("Holy Fire");
            availableCards.add("Arcane Missiles");

        } else if (name.equals("Rogou")){
            availableCards.add("Friendly Smith");
            availableCards.add("Goldshire Footman");
            availableCards.add("Twisting Nether");
            availableCards.add("Stonetusk Boar");
            availableCards.add("Worgen Infiltrator");
            availableCards.add("Bloodfen Raptor");
            availableCards.add("Sunreaver Warmage");
            availableCards.add("Sap");
            availableCards.add("Sunreaver Warmage");
            availableCards.add("Living Monument");

        } else if (name.equals("Warlock")){
            availableCards.add("Dreadscale");
            availableCards.add("Regenerate");
            availableCards.add("Mosh'Ogg Enforcer");
            availableCards.add("Stormwind Champion");
            availableCards.add("Stonetusk Boar");
            availableCards.add("Depth Charge");
            availableCards.add("Novice Engineer");
            availableCards.add("Sunreaver Warmage");
            availableCards.add("Goldshire Footman");
            availableCards.add("Shieldbearer");

        }
    }

    public String getName() {
        return name;
    }
    public int getHeroLevel() {
        return heroLevel;
    }
    public int getNumberOfCardsOwnedByHero() {
        return numberOfCardsOwnedByHero;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<String> getAllCards() {
        return allCards;
    }
    public ArrayList<String> getAvailableCards() {
        return availableCards;
    }
    public JSONObject getDeckCards() {
        return deckCards;
    }
    public ArrayList<String> getComplementAvailableCards(){
        ArrayList<String> complementAvailable = new ArrayList<>(allCards);
        complementAvailable.removeAll(availableCards);
        return complementAvailable;
    }
    public int getCurrentNumberOfCardsInDeck() {
        return currentNumberOfCardsInDeck;
    }
}
