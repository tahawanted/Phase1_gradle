package Card;

import ConfigSettings.Main_config_file;
import Utility.FileFunctions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class InitiateCards {
    public static void main(String[] args) {
        String []rarity = {"Common", "Rare", "Epic", "Legendary"};
        String []heroClass = {"Neutral", "Mage", "Rogue", "Warlock"};
        JSONObject tempCardObject;
        JSONArray cardArray = new JSONArray();
        ArrayList<Cards.card> neutralCards = new ArrayList<>();
        ArrayList<Cards.card> mageCards = new ArrayList<>();
        ArrayList<Cards.card> rogueCards = new ArrayList<>();
        ArrayList<Cards.card> warlockCards = new ArrayList<>();
        neutralCards.add(new Cards.minion("Goldshire Footman", 1, 2, 1, heroClass[0],
                rarity[0], "Taunt"));
        neutralCards.add(new Cards.minion("Stonetusk Boar", 1, 1, 1, heroClass[0],
                rarity[0], "Charge"));
        neutralCards.add(new Cards.minion("Shieldbearer", 1, 4, 0, heroClass[0],
                rarity[0], "Taunt"));
        neutralCards.add(new Cards.minion("Worgen Infiltrator", 1, 1, 2, heroClass[0],
                rarity[0], "Stealth"));
        neutralCards.add(new Cards.minion("Depth Charge", 1, 5, 0, heroClass[0],
                rarity[0], "At the start of your turn, deal 5 damage to all minions"));
        neutralCards.add(new Cards.minion("Bloodfen Raptor", 2, 2, 3, heroClass[0],
                rarity[0], ""));
        neutralCards.add(new Cards.minion("Novice Engineer", 2, 1, 1, heroClass[0],
                rarity[0], "Battlecry: Draw a card."));
        neutralCards.add(new Cards.minion("Raid Leader", 3, 2, 2, heroClass[0],
                rarity[0], "Your other minions have +1 attack"));
        neutralCards.add(new Cards.minion("Sunreaver Warmage", 5, 4, 4, heroClass[0],
                rarity[0], "Battlecry: If you're holding a spell that costs 5 or more, deal 4 damage."));
        neutralCards.add(new Cards.minion("Lord of the Arena", 6, 5, 6, heroClass[0],
                rarity[0], "Taunt"));
        neutralCards.add(new Cards.minion("Stormwind Champion", 7, 6, 6, heroClass[0],
                rarity[0], "Your other minions have +1/+1"));
        neutralCards.add(new Cards.minion("Mosh'Ogg Enforcer", 8, 14, 2, heroClass[0],
                rarity[0], "Taunt\n Divine shield"));
        neutralCards.add(new Cards.minion("Living Monument", 10, 10, 10, heroClass[0],
                rarity[0], "Taunt"));

        neutralCards.add(new Cards.spell("Regenerate", 3, heroClass[0], rarity[0],
                "Restore 3 health"));
        neutralCards.add(new Cards.spell("Divine Spirit", 3, heroClass[0], rarity[0],
                "Double a minions health"));
        neutralCards.add(new Cards.spell("Holy Fire", 6, heroClass[0], rarity[1],
                "Deal 5 damage. Restore 5 health to your here"));
        neutralCards.add((new Cards.spell("Sap", 2, heroClass[0], rarity[0],
                "Return an enemy's minion to their hand.")));
        neutralCards.add(new Cards.spell("Sprint", 7, heroClass[0], rarity[1], "Draw 4 cards."));
        neutralCards.add(new Cards.spell("Arcane Missiles", 2, heroClass[0], rarity[0],
                "Deal 3 damage to random enemies."));
        neutralCards.add(new Cards.spell("Fireball", 5, heroClass[0], rarity[0],
                "Deal 6 damage."));
        neutralCards.add(new Cards.spell("Flamestrike", 8, heroClass[0], rarity[2],
                "Deal 4 damage to all enemy minions."));

        mageCards.add(new Cards.spell("Polymorph", 4, heroClass[1], rarity[0],
                "Transform a minion into a 1/1 sheep"));
        mageCards.add(new Cards.minion("Twilight Flamecaller", 3, 2, 2, heroClass[1],
                rarity[0], "Battlecry: Deal 1 damage to all enemy minions"));

        rogueCards.add(new Cards.spell("Friendly Smith", 1, heroClass[2], rarity[0],
                "Discover a weapon from any class and add it to your deck with +2/+2"));
        rogueCards.add(new Cards.minion("Plagubringer", 4, 3, 3, heroClass[2],
                rarity[1], "Battlecry: Give a friendly minion poisonous."));
        rogueCards.add(new Cards.weapon("Shadowblade", 3, 2, 3, heroClass[2],
                rarity[1], "Battlecry: Your hero is immune this turn."));

        warlockCards.add(new Cards.minion("Dreadscale", 3, 2, 4, heroClass[3],
                rarity[0], "At the end of your turn, deal 1 damage to all other minions"));
        warlockCards.add(new Cards.spell("Twisting Nether", 8, heroClass[3],
                rarity[1], "Destroy all minions."));
        warlockCards.add(new Cards.weapon("Skull of the Man'ari", 5, 3, 0,
                heroClass[3], rarity[0], "At the start of your turn, summon a minion from your hand"));


        for (Cards.card instance: neutralCards)
            cardArray.add(instance.getCardJsonObject());
        for (Cards.card instance: mageCards)
            cardArray.add(instance.getCardJsonObject());
        for (Cards.card instance: rogueCards)
            cardArray.add(instance.getCardJsonObject());
        for (Cards.card instance: warlockCards)
            cardArray.add(instance.getCardJsonObject());

        FileFunctions.saveJsonArray(cardArray, Main_config_file.getAllCardsJSONFile());

    }



}

